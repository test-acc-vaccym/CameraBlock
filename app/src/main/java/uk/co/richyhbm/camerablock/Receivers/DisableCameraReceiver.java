package uk.co.richyhbm.camerablock.Receivers;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import uk.co.richyhbm.camerablock.R;
import uk.co.richyhbm.camerablock.Utilities.DeviceAdminHelper;
import uk.co.richyhbm.camerablock.Utilities.NotificationHelper;


public class DisableCameraReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        DeviceAdminHelper dah = new DeviceAdminHelper(context);

        if(dah.isAdmin()) {
            dah.disableCamera();

            //Notify user of camera state
            NotificationHelper.showToast(context, dah.isCameraDisabled() ? R.string.camera_disabled_toast : R.string.camera_enabled_toast);

            NotificationHelper.hidePersistentNotification(context);
        } else {
            NotificationHelper.showNotification(context, R.string.device_admin_not_granted);
        }
    }

    public static PendingIntent openReceiver(Context context) {
        Intent disableActivity = new Intent(context, DisableCameraReceiver.class);
        return PendingIntent.getBroadcast(context, 0, disableActivity, 0);
    }
}
