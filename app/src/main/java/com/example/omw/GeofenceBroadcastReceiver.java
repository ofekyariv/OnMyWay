package com.example.omw;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import java.util.List;

import static com.example.omw.MapsActivity.latLng;


public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //  Toast.makeText(context, "Geofence triggered", Toast.LENGTH_SHORT).show();

        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }
        //המקום שבו קורא הטריגר לגיאופנס (לא המרכז)
        //Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                notificationHelper.sendSMS("user entered zone: "+latLng.latitude+" : "+latLng.longitude);
                notificationHelper.sendHighPriorityNotification("ENTER", "", MapsActivity.class);

                break;
            //זו הודעה על שהייה כרגע לא רלוונטית אולי בהמשך אפשרות לשליחת הודעה לאחר שהייה במשך זמן מסוים
            // case Geofence.GEOFENCE_TRANSITION_DWELL:
            //  Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
            // notificationHelper.sendSMS("user dwelling zone");
            // notificationHelper.sendHighPriorityNotification("DWELL","", MapsActivity.class);
            //break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                notificationHelper.sendSMS("user exited zone");
                notificationHelper.sendHighPriorityNotification("EXIT", "", MapsActivity.class);
                break;
        }
    }
}