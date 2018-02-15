package uk.co.richyhbm.camerablock.Utilities;


import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import uk.co.richyhbm.camerablock.R;

public class Settings {
    private Context context;
    private SharedPreferences sharedPreferences;

    public Settings(Context context) {
        this.context = context;
        this.sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    private boolean getBoolean(int keyId, boolean defaultValue) {
        return sharedPreferences.getBoolean(context.getString(keyId), defaultValue);
    }

    private void setBoolean(int keyId, boolean value) {
        sharedPreferences.edit().putBoolean(context.getString(keyId), value).apply();
    }

    public void setShowPermanentNotification(boolean value) {
         setBoolean(R.string.settings_key_show_notification_when_camera_enabled, value);
    }

    public boolean getShowPermanentNotification() {
        return getBoolean(R.string.settings_key_show_notification_when_camera_enabled, false);
    }
}
