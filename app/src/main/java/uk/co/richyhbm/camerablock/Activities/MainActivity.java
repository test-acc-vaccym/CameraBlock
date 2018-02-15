package uk.co.richyhbm.camerablock.Activities;

import android.app.PendingIntent;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;

import uk.co.richyhbm.camerablock.R;
import uk.co.richyhbm.camerablock.Receivers.DisableCameraReceiver;
import uk.co.richyhbm.camerablock.Utilities.DeviceAdminHelper;
import uk.co.richyhbm.camerablock.Utilities.NotificationHelper;
import uk.co.richyhbm.camerablock.Utilities.Settings;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final DeviceAdminHelper dah = new DeviceAdminHelper(this);
        final Settings settings = new Settings(this);

        final Button enableDeviceAdminButton = findViewById(R.id.enable_device_admin);
        final Button toggleCameraButton = findViewById(R.id.toggle_camera);
        final Switch persistentNotificationSwitch = findViewById(R.id.persistent_notification);

        enableDeviceAdminButton.setOnClickListener((onClick) -> dah.openAddDeviceAdminActivity(this));

        NotificationCompat.Action disableAction = new NotificationCompat.Action(R.drawable.ic_camera, getText(R.string.disable_camera), DisableCameraReceiver.openReceiver(this));

        toggleCameraButton.setOnClickListener((onClick) -> {
            if(dah.isAdmin()) {
                dah.toggleCameraDisabled();
                NotificationHelper.showToast(getApplicationContext(), dah.isCameraDisabled() ? R.string.camera_disabled_toast : R.string.camera_enabled_toast);
                toggleCameraButton.setText(dah.isCameraDisabled() ? R.string.enable_camera : R.string.disable_camera);

                if(settings.getShowPermanentNotification()) {
                    if(dah.isCameraDisabled())
                        NotificationHelper.hidePersistentNotification(this);
                    else
                        NotificationHelper.showPersistentNotification(this, R.string.app_name, R.string.camera_enabled_toast, disableAction);
                }
            }
        });

        persistentNotificationSwitch.setChecked(settings.getShowPermanentNotification());
        persistentNotificationSwitch.setOnCheckedChangeListener((compoundButton, b) -> {
            settings.setShowPermanentNotification(b);
            if(settings.getShowPermanentNotification()) {
                if(dah.isCameraDisabled())
                    NotificationHelper.hidePersistentNotification(this);
                else
                    NotificationHelper.showPersistentNotification(this, R.string.app_name, R.string.camera_enabled_toast, disableAction);
            }
        });

        if(dah.isAdmin()) {
            persistentNotificationSwitch.setEnabled(true);
            toggleCameraButton.setEnabled(true);
            toggleCameraButton.setText(dah.isCameraDisabled() ? R.string.enable_camera : R.string.disable_camera);
        } else {
            enableDeviceAdminButton.setEnabled(true);
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        final DeviceAdminHelper dah = new DeviceAdminHelper(this);
        final Settings settings = new Settings(this);

        final Button enableDeviceAdminButton = findViewById(R.id.enable_device_admin);
        final Button toggleCameraButton = findViewById(R.id.toggle_camera);
        final Switch persistentNotificationSwitch = findViewById(R.id.persistent_notification);

        if(dah.isAdmin()) {
            persistentNotificationSwitch.setEnabled(true);
            toggleCameraButton.setEnabled(true);
            toggleCameraButton.setText(dah.isCameraDisabled() ? R.string.enable_camera : R.string.disable_camera);
        } else {
            enableDeviceAdminButton.setEnabled(true);
        }

        if(settings.getShowPermanentNotification() && !dah.isCameraDisabled()) {
            NotificationCompat.Action disableAction = new NotificationCompat.Action(R.drawable.ic_camera, getText(R.string.disable_camera), DisableCameraReceiver.openReceiver(this));
            NotificationHelper.showPersistentNotification(this, R.string.app_name, R.string.camera_enabled_toast, disableAction);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == DeviceAdminHelper.ADD_DEVICE_ADMIN_ACTIVITY_REQUEST) {
            recreate();
        }
    }
}
