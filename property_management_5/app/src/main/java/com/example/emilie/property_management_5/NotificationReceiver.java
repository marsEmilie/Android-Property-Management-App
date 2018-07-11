package com.example.emilie.property_management_5;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        NotificationManager notificationManager = ( NotificationManager )
                context.getSystemService( Context.NOTIFICATION_SERVICE );

        Intent repeating_intent = new Intent( context, ViewTenant.class );
        repeating_intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );

        PendingIntent pendingIntent = PendingIntent.getActivity(
                context,
                100,
                repeating_intent,
                PendingIntent.FLAG_UPDATE_CURRENT );

        NotificationCompat.Builder builder = new NotificationCompat.Builder( context )
                .setContentIntent( pendingIntent )
                .setSmallIcon( R.drawable.ic_launcher )
                .setContentTitle( "Rental overdue notice" )
                .setContentText( "Notification text" )
                .setAutoCancel( true ); //make notification dismiss-able when user swipe

        notificationManager.notify( 100, builder.build() );
    }
}