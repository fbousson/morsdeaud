package be.fbousson.morsdeaud.remorse;

import android.app.Application;
import android.util.Log;

import be.fbousson.morsdeaud.common.RemorseApplication;
import be.fbousson.morsdeaud.remorse.preferences.UserPreferences;

/**
 * Created by fbousson on 02/12/14.
 */
public class RemorseMobileApplication extends RemorseApplication{

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "OnCreate");
        UserPreferences.init(this);
        Log.d(TAG, "Done with init");
    }

}
