package com.example.miche.uilabs;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;



/**
 * Created by miche on 20/11/2017.
 */

public class NotificationReceiver extends BroadcastReceiver {
    //Todo Additional 2 - create a notification receiver, extending broadcast receiver.
    // it will push a notification, plus alarm sound when there are only five minutes to deadline
    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notification = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent newIntent = new Intent(context, ToDoManagerActivity.class);
        newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);

        // From Intent get the request ID to uniquely identify the pendingIntent
        Bundle b = intent.getExtras();
        int code = b.getInt("requestID");

        PendingIntent pendingIntent = PendingIntent.getActivity(context, code, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        // TODO - Create notification with title, text, icons and alarm sound
        String title = intent.getExtras().getString("title");

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "my_channel_id").
                setSmallIcon(R.mipmap.ic_launcher_round).
                setContentIntent(pendingIntent).
                setContentText(title + " : Deadline is coming. 5 min to go!").
                setContentTitle("UILabs App").
                setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)).
                setAutoCancel(true);

        notification.notify(code, builder.build());

    }
}
