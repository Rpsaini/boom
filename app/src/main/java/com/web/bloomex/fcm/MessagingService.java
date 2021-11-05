package com.web.bloomex.fcm;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import androidx.core.app.NotificationCompat;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.web.bloomex.R;
import com.web.bloomex.SplashScreen;

public class MessagingService extends FirebaseMessagingService {

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);


        System.out.println("Device token ==="+token);
        //SavePreferences savePreferences = new SavePreferences();
        //savePreferences.savePreferencesData(this, token, AppSettings.device_token);

    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        System.out.println("on message received==="+remoteMessage.getData());
           //showNotification(jsonObject.getString("title"), jsonObject.getString("message"));

        }

    private void showNotification(String title, String message) {
        Intent intent = new Intent(this, SplashScreen.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri defaultSoundUri = Uri.parse("android.resource://" + getPackageName() + "/");
        NotificationManager nm = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String CHANNEL_ID = "bloomex";// The id of the channel.
            CharSequence name = getResources().getString(R.string.app_name);// The user-visible name of the channel.
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_NOTIFICATION).build();
            mChannel.setSound(defaultSoundUri, attributes); // This is IMPORTANT

            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
            Notification notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(this.getResources().getString(R.string.app_name)).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setChannelId(CHANNEL_ID)
                    .setSound(defaultSoundUri)
                    .setSmallIcon(R.mipmap.ic_launcher)

                    .setContentText(message).build();

            nm.createNotificationChannel(mChannel);
            nm.notify(401, notification);
        } else {
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                    this);
            Notification notification = mBuilder.setSmallIcon(R.mipmap.ic_launcher).setTicker(getResources().getString(R.string.app_name)).setWhen(0)
                    .setAutoCancel(true)
                    .setContentTitle(title)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                    .setContentIntent(pendingIntent)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setSound(defaultSoundUri)
                    .setContentText(message).build();

            nm.notify(401, notification);
        }
    }

}