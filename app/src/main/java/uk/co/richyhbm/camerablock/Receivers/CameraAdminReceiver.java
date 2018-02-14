package uk.co.richyhbm.camerablock.Receivers;


import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class CameraAdminReceiver extends android.app.admin.DeviceAdminReceiver {
    @Override
    public void onDisabled(Context context, Intent intent) {
        DevicePolicyManager devicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        ComponentName adminName = new ComponentName(context, CameraAdminReceiver.class);

        if(devicePolicyManager != null && devicePolicyManager.isAdminActive(adminName)) {
            //If removing as an admin, ensure camera is enabled (maybe in future we can set it to the state it was in when admin got enabled)
            devicePolicyManager.setCameraDisabled(adminName, false);
        }
    }
}
