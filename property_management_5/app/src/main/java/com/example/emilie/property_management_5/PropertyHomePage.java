package com.example.emilie.property_management_5;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PropertyHomePage extends AppCompatActivity {

    public static final String PROPERTY_NAME = "property name";
    public static final String PROPERTY_ID = "property id";
    public static final String PROPERTY_LOCATION = "property location";
    public static final String PROPERTY_TYPE = "property type";
    public static final String PROPERTY_RENTAL = "property rental";
    private TextView editTextName;
    private TextView editTextLocation;
    private  TextView editTextRental;
    private Spinner spinnerProperty;
    private Button buttonAddProperty;
    private ListView listViewProperties;

    //a list to store all the property from firebase database
    private ArrayList<Property> properties = new ArrayList<>();

    //our database reference object
    private DatabaseReference databaseProperties;

    //Adapter for individual item view
    private PropertyList propertyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_property_home_page);

        //getting the reference of properties node
        //databaseProperties = FirebaseDatabase.getInstance().getReference("properties");
        databaseProperties = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //getting views
        editTextName = (TextView) findViewById(R.id.editTextName);
        editTextLocation = (TextView)findViewById(R.id.textViewPropertyLocation);
        editTextRental = (TextView)findViewById(R.id.textViewPropertyRental);
        spinnerProperty = (Spinner) findViewById(R.id.spinnerProperties);


        listViewProperties = (ListView) findViewById(R.id.listViewProperties);

        buttonAddProperty = (Button) findViewById(R.id.buttonAddProperties);

        //list to store properties
        //properties = new ArrayList<>();

        //creating adapter
        propertyAdapter = new PropertyList(PropertyHomePage.this, properties);
        //attaching adapter to the listview
            listViewProperties.setAdapter(propertyAdapter);

            //set color of spinner
        spinnerProperty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //adding an onclicklistener to button
        buttonAddProperty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addProperty()
                //the method is defined below
                //this method is actually performing the write operation
                addProperty();
            }
        });

        //attaching listener to listview
        listViewProperties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected property
                Property property = properties.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), ViewProperty.class);

                //putting property name and id to intent
                intent.putExtra(PROPERTY_ID, property.getPropertyID());
                intent.putExtra(PROPERTY_NAME, property.getPropertyName());
                intent.putExtra(PROPERTY_LOCATION, property.getPropertyLocation());
                intent.putExtra(PROPERTY_RENTAL, property.getPropertyRental());
                intent.putExtra(PROPERTY_TYPE,property.getPropertyType());

                //starting the activity with intent
                startActivity(intent);
            }
        });

        listViewProperties.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Property property = properties.get(i);
                showUpdateDeleteDialog(property.getPropertyID(), property.getPropertyName());
                return true;
            }
        });


    }

    private void showUpdateDeleteDialog(final String propertyId, String propertyName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextName = (EditText) dialogView.findViewById(R.id.editTextName);
        final EditText editTextLocation = (EditText)dialogView.findViewById(R.id.textViewPropertyLocation_ud);
        final EditText editTextRental =(EditText)dialogView.findViewById(R.id.textViewRental_ud);
        final Spinner spinnerProperty = (Spinner) dialogView.findViewById(R.id.spinnerProperties);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateProperty);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteProperty);

        dialogBuilder.setTitle(propertyName);
        final AlertDialog b = dialogBuilder.create();
        b.show();


        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTextName.getText().toString().trim();
                String location = editTextLocation.getText().toString().trim();
                String rental = editTextRental.getText().toString().trim();
                String type = spinnerProperty.getSelectedItem().toString();

                if (!TextUtils.isEmpty(name)) {
                    updateProperty(propertyId, name, location, rental, type);
                    b.dismiss();
                }
            }
        });


        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProperty(propertyId);
                b.dismiss();
            }
        });
    }

    private boolean updateProperty(String id, String name, String location, String rental, String type) {
        //getting the specified property reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //updating property
        Property property = new Property(id, name, location, rental, type);
        dR.child("properties").child((id)).setValue(property);
        Toast.makeText(getApplicationContext(), "Property Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteProperty(String id) {
        //getting the specified property reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //removing property
        dR.child("properties").child(id).removeValue();
        Toast.makeText(getApplicationContext(), "Property Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        //Query from database tree of userID > properties
        databaseProperties.child("properties").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous property list
                properties.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting property
                    Property property = postSnapshot.getValue(Property.class);
                    //adding property to the list
                    properties.add(property);
                }
                propertyAdapter.notifyDataSetChanged();

//                //creating adapter
//                PropertyList propertyAdapter = new PropertyList(PropertyHomePage.this, properties);
//                //attaching adapter to the listview
//                listViewProperties.setAdapter(propertyAdapter);
            }

            @Override
            public void onCancelled( DatabaseError databaseError )
            {
                Toast.makeText( PropertyHomePage.this,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
    * This method is saving a new property to the
    * Firebase Realtime Database
    * */
    private void addProperty() {
        //getting the values to save
        String name = editTextName.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String type = spinnerProperty.getSelectedItem().toString();
        String rental = editTextRental.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Property
            String id = databaseProperties.push().getKey();

            //creating an Property Object
            Property property = new Property(id, name, location, rental, type);

            //Saving the Property
            databaseProperties.child("properties").child(id).setValue(property);

            //setting edittext to blank again
            editTextName.setText("");
            editTextLocation.setText("");
            editTextRental.setText("");

            //displaying a success toast
            Toast.makeText(this, "Property added", Toast.LENGTH_LONG).show();
        } else {
            //if the value is not given displaying a toast
            Toast.makeText(this, "Please enter a name", Toast.LENGTH_LONG).show();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);

        MenuItem item = menu.findItem(R.id.menuSearch);
        SearchView searchView = (SearchView)item.getActionView();


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String searchedText) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String searchedText) {
                searchedText = searchedText.toLowerCase();

                ArrayList<Property> newList = new ArrayList<>();

                for (Property property : properties)
                {
                    /* Choose which string to compare */
                    String search_by_property_name = property.getPropertyName().toLowerCase();

                    if ( search_by_property_name.contains( searchedText ) )
                    {
                        newList.add(property);
                    }
                }

                propertyAdapter.setFilter(newList);
//                PropertyList searchAdapter = new PropertyList(PropertyHomePage.this, properties);
//                searchAdapter.getFilter().filter(searchedText);
//                adapter.getFilter().filter(s);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}
