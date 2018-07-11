package com.example.emilie.property_management_5;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.*;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class TenantHomePage extends AppCompatActivity {
    private static final int IMPORT_CONTACT_REQUEST = 1;

    public static final String TENANT_NAME = "tenant name";
    public static final String TENANT_ID = "tenant id";
    public static final String TENANT_CONTACT = "tenant contact";
    public static final String TENANT_GENDER = "tenant gender";
    public static final String TENANT_PAYMENT_DATE = "tenant payment date";
    public static final String TENANT_EMAIL ="tenant email";
    private EditText editTextTenantName;
    private EditText editTextContact;
    private  EditText editTextEmail;
    private TextView dateView;
    private Spinner spinnerGender;
    private Button buttonAddTenant, buttonImportTenant, buttonPickDate;
    private ListView listViewTenants;
    private int dayOfMonth;
    private AlarmManager alarmManager;
    private PendingIntent pendingIntent;

    private Calendar calendar;
    private int year, month, day;

    //a list to store all the property from firebase database
    private ArrayList<Tenant> tenants = new ArrayList<>();

    //our database reference object
    private DatabaseReference databaseTenants;

    //Adapter for individual item view
    private TenantList tenantAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tenant_home_page);

        //getting the reference of properties node
        //databaseProperties = FirebaseDatabase.getInstance().getReference("properties");
        databaseTenants = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //getting views
        editTextTenantName = (EditText) findViewById(R.id.editTextName_tenant);
        editTextContact = (EditText)findViewById(R.id.textViewContact_tenant);
        editTextEmail = (EditText)findViewById(R.id.textViewEmail_tenant);
        dateView =(TextView) findViewById(R.id.textViewPaymentDate_tenant);
        spinnerGender = (Spinner) findViewById(R.id.spinnerGender);
        buttonImportTenant = (Button) findViewById(R.id.buttonImportTenant);
        buttonImportTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                contactPicker();
            }
        });

        listViewTenants = (ListView) findViewById(R.id.listViewTenants);

        buttonAddTenant = (Button) findViewById(R.id.buttonAddTenant);

        buttonPickDate = (Button)findViewById(R.id.buttonPickDate);
        calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);

        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);
        showDate(year, month+1, day);

        //list to store properties
        //properties = new ArrayList<>();

        //creating adapter
        tenantAdapter = new TenantList(TenantHomePage.this, tenants);
        //attaching adapter to the listview
        listViewTenants.setAdapter(tenantAdapter);

        //set color of spinner
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                ((TextView) adapterView.getChildAt(0)).setTextColor(Color.WHITE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //adding an onclicklistener to button
        buttonAddTenant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //calling the method addTenant()
                addTenant();
            }
        });

        //attaching listener to listview
        listViewTenants.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //getting the selected property
                Tenant tenant = tenants.get(i);

                //creating an intent
                Intent intent = new Intent(getApplicationContext(), ViewTenant.class);

                //putting property name and id to intent
                intent.putExtra(TENANT_ID, tenant.getId());
                intent.putExtra(TENANT_NAME, tenant.getName());
                intent.putExtra(TENANT_CONTACT, tenant.getContactNumber());
                intent.putExtra(TENANT_GENDER, tenant.getGender());
                intent.putExtra(TENANT_EMAIL,tenant.getEmail());
                intent.putExtra(TENANT_PAYMENT_DATE,tenant.getPaymentDate());

                //starting the activity with intent
                startActivity(intent);
            }
        });

        listViewTenants.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Tenant tenant = tenants.get(i);
                showUpdateDeleteDialog(tenant.getId(), tenant.getName());
                return true;
            }
        });

    }

    private void showUpdateDeleteDialog(final String tenantId, String tenantName) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.update_dialog_tenant, null);
        dialogBuilder.setView(dialogView);

        final EditText editTenantName = (EditText) dialogView.findViewById(R.id.editTextName_tenant);
        final EditText editContact = (EditText)dialogView.findViewById(R.id.textViewContact_tenant_ud);
        final EditText editTextEmail =(EditText)dialogView.findViewById(R.id.textViewEmail_tenant_ud);
        final EditText editTextTenantPaymentDate = (EditText)dialogView.findViewById(R.id.textViewPaymentDate_tenant_ud);
        final Spinner spinnerGender = (Spinner) dialogView.findViewById(R.id.spinnerGender);
        final Button buttonUpdate = (Button) dialogView.findViewById(R.id.buttonUpdateTenant);
        final Button buttonDelete = (Button) dialogView.findViewById(R.id.buttonDeleteTenant);

        dialogBuilder.setTitle(tenantName);
        final AlertDialog b = dialogBuilder.create();
        b.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = editTenantName.getText().toString().trim();
                String contact = editContact.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String paymentDate = editTextTenantPaymentDate.getText().toString().trim();
                String gender = spinnerGender.getSelectedItem().toString();

                if (!TextUtils.isEmpty(name)) {
                    updateTenant(tenantId, name, gender, contact, email, paymentDate);
                    b.dismiss();
                }
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                deleteProperty(tenantId);
                b.dismiss();
            }
        });
    }

    private boolean updateTenant(String id, String name, String gender, String contact, String email, String paymentDate) {

        //getting the specified property reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //updating property
        Tenant tenant = new Tenant(id, name, gender, contact,email, paymentDate);
        dR.child("tenants").child((id)).setValue(tenant);
        Toast.makeText(getApplicationContext(), "Tenant Updated", Toast.LENGTH_LONG).show();
        return true;
    }

    private boolean deleteProperty(String id) {
        //getting the specified property reference
        DatabaseReference dR = FirebaseDatabase.getInstance().getReference(FirebaseAuth.getInstance().getCurrentUser().getUid());

        //removing property
        dR.child("tenants").child(id).removeValue();
        Toast.makeText(getApplicationContext(), "Tenant Deleted", Toast.LENGTH_LONG).show();

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        //attaching value event listener
        //Query from database tree of userID > properties
        databaseTenants.child("tenants").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                //clearing the previous property list
                tenants.clear();

                //iterating through all the nodes
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    //getting property
                    Tenant tenant = postSnapshot.getValue(Tenant.class);
                    //adding property to the list
                    tenants.add(tenant);
                }
                tenantAdapter.notifyDataSetChanged();

            }

            @Override
            public void onCancelled( DatabaseError databaseError )
            {
                Toast.makeText( TenantHomePage.this,
                        databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    /*
    * This method is saving a new property to the
    * Firebase Realtime Database
    * */
    private void addTenant() {
        if ( !fieldValidator() )
        {
            return;
        }

        //getting the values to save
        String name = editTextTenantName.getText().toString().trim();
        String contact = editTextContact.getText().toString().trim();
        String gender = spinnerGender.getSelectedItem().toString();
        String email = editTextEmail.getText().toString().trim();
        String paymentDate = dateView.getText().toString().trim();

        //checking if the value is provided
        if (!TextUtils.isEmpty(name)) {

            //getting a unique id using push().getKey() method
            //it will create a unique id and we will use it as the Primary Key for our Property
            String id = databaseTenants.push().getKey();

            //creating an Property Object
            Tenant tenant = new Tenant(id, name, gender,contact,email,paymentDate);

            //Saving the Property
            databaseTenants.child("tenants").child(id).setValue(tenant);

            //setting edittext to blank again
            editTextTenantName.setText("");
            editTextContact.setText("");
            editTextEmail.setText("");
            dateView.setText("");

            //displaying a success toast
            Toast.makeText(this, "Tenant added", Toast.LENGTH_LONG).show();
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

                ArrayList<Tenant> newList = new ArrayList<>();

                for (Tenant tenant : tenants)
                {
                    /* Choose which string to compare */
                    String search_by_property_name = tenant.getName().toLowerCase();

                    if ( search_by_property_name.contains( searchedText ) )
                    {
                        newList.add(tenant);
                    }
                }

                tenantAdapter.setFilter(newList);
                return true;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void contactPicker(){
        Intent i = new Intent(Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
        startActivityForResult(i, IMPORT_CONTACT_REQUEST);
    }

    private void contactImport(Intent data){
        Cursor cursor = null ;

        try{
            String phoneNum = null;
            String phoneName = null;

            Uri uri = data.getData();

            cursor = getContentResolver().query(
                    uri,
                    null,null,null,null
            );
            cursor.moveToFirst();
            int phoneNumIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            int phoneNameIndex = cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);

            phoneNum = cursor.getString( phoneNumIndex);
            phoneName = cursor.getString(phoneNameIndex);

            editTextTenantName.setText(phoneName);
            editTextContact.setText(phoneNum);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == IMPORT_CONTACT_REQUEST && resultCode == Activity.RESULT_OK && data.getData() != null){
            contactImport(data);
        }
    }

    public boolean fieldValidator()
    {
        String email = editTextEmail.getText().toString().trim();
        if(editTextTenantName.length() == 0 || editTextContact.length() == 0 || editTextEmail.length() == 0)
        {
            Toast.makeText(TenantHomePage.this,"Please fill in all the fields required",Toast.LENGTH_LONG).show();
            return false;
        }

        if(!email.contains("@")){
            Toast.makeText(TenantHomePage.this,"Please enter a valid email",Toast.LENGTH_LONG).show();
            return false;
        }
        return true;
    }

    // date picker resources: https://www.tutorialspoint.com/android/android_datepicker_control.htm
    @SuppressWarnings("deprecation")
    public void setDate(View view) {
        showDialog(999);
        Toast.makeText(getApplicationContext(), "Date set",
                Toast.LENGTH_SHORT)
                .show();

    }

    @Override
    protected Dialog onCreateDialog(int id) {
        // TODO Auto-generated method stub
        if (id == 999) {
            return new DatePickerDialog(this,
                    myDateListener, year, month, day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener myDateListener = new
            DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(android.widget.DatePicker arg0,
                                      int arg1, int arg2, int arg3) {
                    // TODO Auto-generated method stub
                    // arg1 = year
                    // arg2 = month
                    // arg3 = day
                    showDate(arg1, arg2+1, arg3);
                    dayOfMonth = arg3;
                    setAlarmTimer();
                }
            };

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }

    private void setAlarmTimer()
    {
        /* Using calendar instance to get date and time */
        /* Return current system real time value */
        /* Value can be check using getTimeInMillis() */
        /* Millisecond to date converter: https://currentmillis.com/ */
        Calendar calendar = Calendar.getInstance();
        calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );

        Intent intent = new Intent( getApplicationContext(), NotificationReceiver.class);

        pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(),
                100,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT );

        alarmManager = (AlarmManager) getSystemService( ALARM_SERVICE );

        /* Set repeat notification based on preset condition */
        /* Datasheet: https://developer.android.com/reference/android/app/AlarmManager.html */
        alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), //Capture the time set from calendar as repeat timer
                AlarmManager.INTERVAL_DAY,
                pendingIntent );

        /*=====================================================================================*/

        if ( alarmManager != null )
        {
            Toast.makeText( TenantHomePage.this,
                    "Notification scheduled", Toast.LENGTH_LONG).show();
        }
    }


}
