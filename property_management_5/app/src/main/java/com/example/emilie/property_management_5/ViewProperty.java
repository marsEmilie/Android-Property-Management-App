package com.example.emilie.property_management_5;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class ViewProperty extends AppCompatActivity {

    private TextView editTextName, editTextLocation, editTextType, editTextRental;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_property);

        Intent intent = getIntent();
        editTextName = (TextView)findViewById(R.id.name_et);
        editTextLocation = (TextView)findViewById(R.id.location_et);
        editTextType = (TextView)findViewById(R.id.type_et);
        editTextRental = (TextView)findViewById(R.id.rental_et);

        editTextName.setText(intent.getStringExtra(PropertyHomePage.PROPERTY_NAME));
        editTextLocation.setText(intent.getStringExtra(PropertyHomePage.PROPERTY_LOCATION));

        editTextType.setText(intent.getStringExtra(PropertyHomePage.PROPERTY_TYPE));
        editTextRental.setText(intent.getStringExtra(PropertyHomePage.PROPERTY_RENTAL));

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("VIEW PROPERTY");
    }
}
