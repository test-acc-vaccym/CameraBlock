package uk.co.richyhbm.camerablock.Utilities;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.StringRes;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import uk.co.richyhbm.camerablock.R;

import static android.content.Context.NOTIFICATION_SERVICE;

public class NotificationHelper {
    private static final String CHANNEL_ID = "Camera_Block_channel";
    private static NotificationCompat.Builder persistentBuilder = null;
    private static final int PERSISTENT_NOTIFICATION_ID = 2654;

    public static void showNotification(Context context, @StringRes int titleId, @StringRes int messageId) {
        showNotification(context, titleId, context.getText(messageId).toString());
    }

    public static void showNotification(Context context, @StringRes int messageId) {
        showNotification(context, R.string.app_name, messageId);
    }

    public static void showNotification(Context context, @StringRes int titleId, String message) {
        NotificationCompat.Builder builder = buildNotification(context, titleId, message);
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        int notificationId = 1;
        notificationManager.notify(notificationId, builder.build());
    }

    public static void showPersistentNotification(Context context, @StringRes int titleId, @StringRes int messageId, NotificationCompat.Action action) {
        showPersistentNotification(context, titleId, context.getText(messageId).toString(), action);
    }

    public static void showPersistentNotification(Context context, @StringRes int titleId, String message, NotificationCompat.Action action) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if(persistentBuilder != null) {
            notificationManager.cancel(PERSISTENT_NOTIFICATION_ID);
            persistentBuilder = null;
        }

        persistentBuilder = buildNotification(context, titleId, message);
        persistentBuilder.setOngoing(true);

        if(action != null)
            persistentBuilder.addAction(action);

        notificationManager.notify(PERSISTENT_NOTIFICATION_ID, persistentBuilder.build());
    }

    public static void hidePersistentNotification(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.cancel(PERSISTENT_NOTIFICATION_ID);
        persistentBuilder = null;
    }

    private static NotificationCompat.Builder buildNotification(Context context, @StringRes int titleId, String message) {
        NotificationChannel channel = null;

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, null)
                .setContentTitle(context.getText(titleId))
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_camera);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel = new NotificationChannel(CHANNEL_ID, context.getString(R.string.app_name), NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(channel);
        } else {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }

        builder.setChannelId(CHANNEL_ID);

        return builder;
    }

    public static void showToast(Context context, @StringRes int messageId) {
        Toast.makeText(context,context.getText(messageId), Toast.LENGTH_LONG).show();
    }
}
