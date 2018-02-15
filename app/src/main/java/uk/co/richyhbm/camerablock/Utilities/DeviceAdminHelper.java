package uk.co.richyhbm.camerablock.Utilities;

import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import uk.co.richyhbm.camerablock.R;
import uk.co.richyhbm.camerablock.Receivers.CameraAdminReceiver;

public class DeviceAdminHelper {
    public static final int ADD_DEVICE_ADMIN_ACTIVITY_REQUEST = 654;

    private Context context;
    private DevicePolicyManager devicePolicyManager;
    private ComponentName adminName;

    public DeviceAdminHelper(Context context) {
        this.context = context;
        this.devicePolicyManager = (DevicePolicyManager)context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        this.adminName = new ComponentName(context, CameraAdminReceiver.class);
    }

    public boolean isAdmin() {
        return devicePolicyManager != null && devicePolicyManager.isAdminActive(adminName);
    }


    public void openAddDeviceAdminActivity(Activity activity) {
        Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        ComponentName deviceAdminComponentName = new ComponentName(context, CameraAdminReceiver.class);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, deviceAdminComponentName);
        activity.startActivityForResult(intent, ADD_DEVICE_ADMIN_ACTIVITY_REQUEST);
    }

    public void removeAdmin() {
        if(isAdmin())
            devicePolicyManager.removeActiveAdmin(adminName);
    }

    public void toggleCameraDisabled() {
        if(isAdmin())
            devicePolicyManager.setCameraDisabled(adminName, !devicePolicyManager.getCameraDisabled(adminName));
    }

    public void disableCamera() {
        if(isAdmin())
            devicePolicyManager.setCameraDisabled(adminName, true);
    }

    public boolean isCameraDisabled() {
        return isAdmin() && devicePolicyManager.getCameraDisabled(adminName);
    }
}
