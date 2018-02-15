package uk.co.richyhbm.camerablock.Services;


import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.service.quicksettings.TileService;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import uk.co.richyhbm.camerablock.R;
import uk.co.richyhbm.camerablock.Receivers.DisableCameraReceiver;
import uk.co.richyhbm.camerablock.Utilities.DeviceAdminHelper;
import uk.co.richyhbm.camerablock.Utilities.NotificationHelper;
import uk.co.richyhbm.camerablock.Utilities.Settings;

@RequiresApi(api = Build.VERSION_CODES.N)
public class QuickCameraToggleTile extends TileService {
    @Override
    public void onTileAdded() {
        super.onTileAdded();
        updateTileState();
    }

    @Override
    public void onClick() {
        super.onClick();
        DeviceAdminHelper dah = new DeviceAdminHelper(getApplicationContext());
        Settings settings = new Settings(getApplicationContext());

        if(dah.isAdmin()) {
            dah.toggleCameraDisabled();

            //Notify user of camera state
            NotificationHelper.showToast(getApplicationContext(),
                    dah.isCameraDisabled() ? R.string.camera_disabled_toast : R.string.camera_enabled_toast);

            if(settings.getShowPermanentNotification()) {
                NotificationCompat.Action disableAction = new NotificationCompat.Action(R.drawable.ic_camera, getText(R.string.disable_camera), DisableCameraReceiver.openReceiver(this));

                if(dah.isCameraDisabled())
                    NotificationHelper.hidePersistentNotification(this);
                else
                    NotificationHelper.showPersistentNotification(this, R.string.app_name, R.string.camera_enabled_toast, disableAction);
            }
        } else {
            NotificationHelper.showNotification(getApplicationContext(), R.string.device_admin_not_granted);
        }

        updateTileState();
    }

    void updateTileState() {
        Tile tile = getQsTile();
        DeviceAdminHelper dah = new DeviceAdminHelper(getApplicationContext());

        if(!dah.isAdmin()) {
            tile.setState(Tile.STATE_UNAVAILABLE);
            tile.setLabel(getApplicationContext().getText(R.string.disable_camera));
        } else if (dah.isCameraDisabled()) {
            tile.setState(Tile.STATE_ACTIVE);
            tile.setLabel(getApplicationContext().getText(R.string.enable_camera));
        } else {
            tile.setState(Tile.STATE_INACTIVE);
            tile.setLabel(getApplicationContext().getText(R.string.disable_camera));
        }

        tile.updateTile();
    }
}
