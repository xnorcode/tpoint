package com.tpoint.utils;

import android.app.NotificationManager;
import android.content.Context;
import android.location.Location;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.tpoint.R;
import com.tpoint.data.TPoint;

/**
 * Created by xnorcode on 18/02/2018.
 */

public class Tools {


    /*
    Send notification method when app is not visible
     */
    public static void sendNotification(Context context) {
        //Define Notification Manager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //Define sound URI
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context.getApplicationContext())
                .setSmallIcon(R.drawable.ic_notification)
                .setContentText(context.getString(R.string.alert))
                .setSound(soundUri); //This sets the sound to play

        //Display notification
        notificationManager.notify(0, mBuilder.build());
    }


    /*
    calculating the distance between the user and last point (in meters)
     */
    public static int calculateDistance(TPoint tpFrom, TPoint tpTo) {
        if (tpFrom != null && tpTo != null) {
            Location from = new Location("from");
            from.setLatitude(tpFrom.getLat());
            from.setLongitude(tpFrom.getLon());

            Location to = new Location("to");
            to.setLatitude(tpTo.getLat());
            to.setLongitude(tpTo.getLon());

            return Math.round(from.distanceTo(to));
        }
        return -1;
    }
}