//package com.example.emilie.property_management_5;
//
//import android.app.AlarmManager;
//import android.app.DatePickerDialog;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import java.util.Calendar;
//
///**
// * Created by Emily on 3/30/2018.
// */
//
//public class TimePicker extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
//
//    public static final String DATEPICKER_TAG = "datepicker";
//    private AlarmManager alarmManager;
//    private PendingIntent pendingIntent;
//
//    /* Place holder for the day picked from calendar */
//    private int dayOfMonth;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_tenant_home_page);
//
//        final Calendar calendar = Calendar.getInstance();
//
//        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance( this,
//                calendar.get( Calendar.YEAR ),
//                calendar.get( Calendar.MONTH ),
//                calendar.get( Calendar.DAY_OF_MONTH ) );
//
//        findViewById(R.id.buttonPickDate).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //datePickerDialog.set;
//                datePickerDialog.show();
//            }
//        });
//
//        if ( savedInstanceState != null )
//        {
//            DatePickerDialog dpd = (DatePickerDialog)
//                    getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
//
//            if ( dpd != null )
//            {
//                dpd.setOnDateSetListener( this );
//            }
//        }
//
//    private void setAlarmTimer()
//    {
//        /* Using calendar instance to get date and time */
//        /* Return current system real time value */
//        /* Value can be check using getTimeInMillis() */
//        /* Millisecond to date converter: https://currentmillis.com/ */
//        Calendar calendar = Calendar.getInstance();
//        calendar.set( Calendar.DAY_OF_MONTH, dayOfMonth );
//
//        long test = calendar.getTimeInMillis();
//        TextView toTV = findViewById( R.id.textViewPaymentDate_tenant );
//        toTV.setText( String.valueOf( test ) );
//
//        Intent intent = new Intent( getApplicationContext(), ViewTenant.class);
//
//        pendingIntent = PendingIntent.getBroadcast(
//                getApplicationContext(),
//                100,
//                intent,
//                PendingIntent.FLAG_UPDATE_CURRENT );
//
//        alarmManager = ( AlarmManager ) getSystemService( ALARM_SERVICE );
//
//        /* Set repeat notification based on preset condition */
//        /* Datasheet: https://developer.android.com/reference/android/app/AlarmManager.html */
//        alarmManager.setRepeating(
//                AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(), //Capture the time set from calendar as repeat timer
//                AlarmManager.INTERVAL_DAY,
//                pendingIntent );
//
//        /* Testing purposes: To test the interval of notification been fired after fixed time */
//        /* In android API22+ minimum interval time set to 1 minute */
////        alarmManager.setRepeating(
////                AlarmManager.ELAPSED_REALTIME,
////                SystemClock.elapsedRealtime(), //trigger timer
////                60 * 1000, //interval timer
////                pendingIntent );
//        /*=====================================================================================*/
//
//        if ( alarmManager != null )
//        {
//            Toast.makeText( TimePicker.this,
//                    "Notification scheduled", Toast.LENGTH_LONG).show();
//        }
//
//    }
//}
