package org.mdss.magicapps.alertap;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.provider.Settings;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Date;
import java.util.Random;

/**
 * Created by Dipan on 27-10-2018.
 */

public class FcmMessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.e("NEW_TOKEN",s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage){
        String title = remoteMessage.getNotification().getTitle();
        String message = remoteMessage.getNotification().getBody();

        //Multiple Notifications
        Random random = new Random();
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        Intent intent = new Intent(this,HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,m,intent,PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, "001");
        notificationBuilder.setContentTitle(title);
        notificationBuilder.setContentText(message);
        notificationBuilder.setSmallIcon(R.drawable.logo);
        notificationBuilder.setVibrate(new long[] {0, 1000, 200,1000 });
        notificationBuilder.setLights(Color.MAGENTA,500,1500);
        notificationBuilder.setAutoCancel(true);
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        notificationBuilder.setVibrate(new long[] {0, 1000, 200,1000 });

        //Notification notif = notificationBuilder.build();
        //notif.flags |= Notification.FLAG_SHOW_LIGHTS;

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(m,notificationBuilder.build());
        //notificationBuilder.setSound(Settings.System.DEFAULT_NOTIFICATION_URI);
        //notificationBuilder.setLights(Color.MAGENTA,500,1500);


    }
}
