package techkids.vn.chinesechessonline.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Icon;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import techkids.vn.chinesechessonline.R;
import techkids.vn.chinesechessonline.activities.GameActivity;
import techkids.vn.chinesechessonline.activities.HomeActivity;
import techkids.vn.chinesechessonline.utils.Util;

public class MyFirebaseService extends FirebaseMessagingService {
    private String chanel_id;
    private String chanel_name;
    private final String TAG = MyFirebaseService.class.toString();


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        chanel_id = getString(R.string.chinese_chess_channel_id);
        chanel_name = getString(R.string.chinese_chess_channel_name);
        Log.d(TAG, String.format("onMessageReceived: RemoteMessage() size -> %s", remoteMessage.getData().size()));

        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification body: " + remoteMessage.getNotification().getBody());
        }

        // check if message contains a data payload
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "From: " + remoteMessage.getFrom());
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        Log.d(TAG, "onMessageReceived: from" + remoteMessage.getFrom());
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "onMessageReceived: size = " + remoteMessage.getData().size());
        }

        String fromPushId = remoteMessage.getData().get("fromPushId");
        String fromId = remoteMessage.getData().get("fromId");
        String fromName = remoteMessage.getData().get("fromName");
        String type = remoteMessage.getData().get("type");

        Log.d(TAG, String.format("onMessageReceived: %s, %s, %s, %s", fromPushId, fromId, fromName, type));

        switch (type) {
            case "invite":
                Log.d(TAG, "onMessageReceived: log nhe 1 cai");
                handleInviteIntent(fromPushId, fromId, fromName);
                break;
            case "accept":
                // TODO: 3/2/2019 start game

                Toast.makeText(this, "Start game", Toast.LENGTH_SHORT).show();
                break;
            case "reject":
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, chanel_id)
                        .setSmallIcon(R.drawable.ic_stat_ic_notification)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setContentTitle(String.format("%s rejected your invite!", fromName));

                //what something?
                Intent intent = new Intent(this, GameActivity.class)
                        .putExtra("type", "wifi");
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
                stackBuilder.addParentStack(GameActivity.class);
                stackBuilder.addNextIntent(intent);

                PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

                builder.setContentIntent(pendingIntent);
                NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (notificationManager != null) {
                    notificationManager.notify(1, builder.build());
                }
                break;
        }
    }

    //xử lý sự kiện khi người dùng click vào accept hoặc cancel
    //bằng cách addaction () pending intent
    private void handleInviteIntent(String fromPushID, String fromID, String fromName) {
        Notification build = null;
        Intent rejectIntent = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("reject")
                .putExtra("withId", fromID)
                .putExtra("to", fromPushID);
        PendingIntent pendingIntentReject = PendingIntent.getBroadcast(this, 0, rejectIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        String gameId = fromID + "-" + Util.getCurrentUserID();

        Intent acceptIntent = new Intent(getApplicationContext(), MyReceiver.class)
                .setAction("accept")
                .putExtra("withId", fromID)
                .putExtra("to", fromPushID);
        PendingIntent pendingIntentAccept = PendingIntent.getBroadcast(this, 2, acceptIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        Intent resultIntent = new Intent(this, GameActivity.class)
                .putExtra("type", "wifi")
                .putExtra("withId", fromID)
                .putExtra("to", fromPushID);

        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(HomeActivity.class);
        stackBuilder.addNextIntent(resultIntent);

        PendingIntent pendingIntentResult = stackBuilder.getPendingIntent(
                0,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //custom notification --- kể từ Android nougat Icon của action sẽ ko được hiển thị -> https://android-developers.googleblog.com/2016/06/notifications-in-android-n.html
        NotificationCompat.Action actionAccept = new NotificationCompat.Action.Builder(R.drawable.accept, "Accept", pendingIntentAccept).build();
        NotificationCompat.Action actionCancel = new NotificationCompat.Action.Builder(R.drawable.cancel, "Cancel", pendingIntentReject).build();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            build = new NotificationCompat.Builder(this, chanel_id)
                    .setSmallIcon(R.drawable.ic_stat_ic_notification)
                    .setColor(getResources().getColor(R.color.colorAccent))
                    .setPriority(NotificationManager.IMPORTANCE_HIGH)
                    .setContentTitle(String.format("%s invites you to play!", fromName))
                    .addAction(actionAccept)
                    .setVibrate(new long[5000])
                    .setChannelId(chanel_id)
                    .setContentIntent(pendingIntentResult)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.hat_king))
                    .addAction(actionCancel)
                    .build();
        }
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (notificationManager == null) {
            return;
        }

        createNotificationChannel();
        notificationManager.notify(1, build);
    }

    private void showListNoti(NotificationManager s) {
        for (NotificationChannel channel : s.getNotificationChannels()) {
            Log.d(TAG, "showListNoti: channel id -> " + channel.getId());
        }
    }

    private void createNotificationChannel() {
        NotificationManager notificationManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel chineseChessChannel = new NotificationChannel(
                    chanel_id,
                    chanel_name,
                    NotificationManager.IMPORTANCE_HIGH);

            chineseChessChannel.enableLights(true);
            chineseChessChannel.enableVibration(true);
            chineseChessChannel.setLightColor(R.color.colorAccent);
            chineseChessChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

            notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(chineseChessChannel);
                Log.d(TAG, "createNotificationChannel: created notification chanel");
            }
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
        Log.d(TAG, "onNewToken: new key" + s);

        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null || currentUser.isAnonymous()) {
            return;
        }
        Log.d(TAG, "onTokenRefresh: ");
        Util.savePushToken(s, currentUser.getUid());
    }
}
