package be.fbousson.morsdeaud.remorse.preferences;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by fbousson on 02/12/14.
 */
public class UserPreferences {

    private static final String PREFS_NAME = "UserPreferencesFile";


    private static SharedPreferences userSharedPreferences;
    private static SharedPreferences.Editor editor;

    private static final String ENABLE_WEAR_MORSE_MESSAGING = "enableWearMorseMessaging";
    private static final String ENABLE_MOBILE_MORSE_MESSAGING = "enableMobileMorseMessaging";


    public static void init(final Context app)
    {
        userSharedPreferences = app.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = userSharedPreferences.edit();
    }


    public static boolean isWearMorseMessagingEnabled()
    {
        return userSharedPreferences.getBoolean(ENABLE_WEAR_MORSE_MESSAGING, true);
    }

    public static void setWearMorseMessagingEnabled(boolean enableMorseMessaging)
    {
        editor.putBoolean(ENABLE_WEAR_MORSE_MESSAGING, enableMorseMessaging);
        editor.apply();
    }

    public static boolean isMobileMorseMessagingEnabled()
    {
        return userSharedPreferences.getBoolean(ENABLE_MOBILE_MORSE_MESSAGING, true);
    }

    public static void setMobileMorseMessagingEnabled(boolean enableMorseMessaging)
    {
        editor.putBoolean(ENABLE_MOBILE_MORSE_MESSAGING, enableMorseMessaging);
        editor.apply();
    }


}
