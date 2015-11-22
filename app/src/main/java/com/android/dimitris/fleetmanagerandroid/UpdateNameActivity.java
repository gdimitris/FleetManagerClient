package com.android.dimitris.fleetmanagerandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class UpdateNameActivity extends AppCompatActivity implements View.OnClickListener{

    private Button updateServerButton;
    private EditText nameText;
    private EditText surnameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_name);

        updateServerButton = (Button) findViewById(R.id.updateServerButton);
        updateServerButton.setOnClickListener(this);

        nameText = (EditText) findViewById(R.id.nameEditText);
        surnameText = (EditText) findViewById(R.id.surnameEditText);
        initializeTextFields();
    }

    private void initializeTextFields(){
        String name = PublicHelpers.loadFromPreferences(this, "Name");
        String surname = PublicHelpers.loadFromPreferences(this, "Surname");

        nameText.setText(name);
        surnameText.setText(surname);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_update_name, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.updateServerButton){
            String name = nameText.getText().toString();
            String surname = surnameText.getText().toString();

            updateServerWithName(name, surname);
            saveInPreferences(name, surname);

            finish();
        }
    }

    private void updateServerWithName(String name, String surname) {
        String deviceId = PublicHelpers.getDeviceUniqueID(getContentResolver());
        String params = deviceId+"/register_full_name?name=" + name + "&surname=" + surname;
        String baseUrl = getResources().getString(R.string.server_url);

        HttpRequestTask requestTask = new HttpRequestTask();
        requestTask.execute(baseUrl, params );
    }

    private void saveInPreferences(String name, String surname){
        PublicHelpers.saveInPreferences(this, "Name", name);
        PublicHelpers.saveInPreferences(this, "Surname", surname);
    }
}
