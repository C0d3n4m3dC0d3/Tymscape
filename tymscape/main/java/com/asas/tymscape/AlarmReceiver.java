package com.asas.tymscape;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "SAMPLE_CHANNEL";

    public void onReceive(Context context, Intent intent) {
        //Get id & message from intent.
        int notificationId = intent.getIntExtra("notificationId", 0);
        String message = intent.getStringExtra(("message"));
        //Call MainActivity when notification is tapped.
        Intent mainIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, mainIntent, 0);

        //NotificationManager
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //For API 26 and above
            CharSequence channel_name = "My Notification";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, channel_name,
                    importance);
            notificationManager.createNotificationChannel(channel);
        }

        MediaPlayer mediaPlayer = MediaPlayer.create(context,
                Settings.System.DEFAULT_RINGTONE_URI);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.l2)
                .setContentTitle("YOU HAVE AN EVENT!")
                .setContentText(message)
               .setContentIntent(contentIntent)
               .setPriority(NotificationCompat.PRIORITY_DEFAULT)
               .setAutoCancel(true);
        //Notify
        notificationManager.notify(notificationId, builder.build());
        mediaPlayer.start();
    }
}