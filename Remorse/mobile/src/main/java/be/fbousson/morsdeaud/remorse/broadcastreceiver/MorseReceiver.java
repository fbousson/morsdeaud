package be.fbousson.morsdeaud.remorse.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import be.fbousson.morsdeaud.common.RemorseApplication;
import be.fbousson.morsdeaud.remorse.preferences.UserPreferences;
import be.fbousson.morsdeaud.remorse.service.MorseSenderIntentService;

/**
 * Created by fbousson on 01/12/14.
 */
public class MorseReceiver extends BroadcastReceiver {


    private static final String TAG = MorseReceiver.class.getSimpleName();

    public static final String EXTRA_MORSE_PLAIN_TEXT = "MorseReceiver_plaintext";
    public static final String EXTRA_SENDER = "MorseReceiver_plaintext_sender";

    public static final String ACTION_MORSE_RECEIVER = "be.fbousson.morsdeaud.remorse.broadcastreceiver.MorseReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();
        Log.d(TAG, "Entering morse broadcast receiver with action " + action);

        if( intent.getAction().equals(ACTION_MORSE_RECEIVER)){
            String morsePlainText = intent.getExtras().getString(EXTRA_MORSE_PLAIN_TEXT);
            Log.d(TAG, "Received plain text "  + morsePlainText);
            String sender = intent.getExtras().getString(EXTRA_SENDER);
            Log.d(TAG, "Sender " + sender);

            boolean isWearSMSMorseMessagingEnabled = UserPreferences.isWearMorseMessagingEnabled();
            morseOnWearDevice(context, morsePlainText, sender, isWearSMSMorseMessagingEnabled);

            boolean isMobileSMSMorseMessagingEnabled  = UserPreferences.isMobileMorseMessagingEnabled();
            morseOnMobileDevice(morsePlainText, isWearSMSMorseMessagingEnabled, isMobileSMSMorseMessagingEnabled);


        }

    }

    private void morseOnMobileDevice(String morsePlainText, boolean isWearSMSMorseMessagingEnabled, boolean isMobileSMSMorseMessagingEnabled) {
        Log.d(TAG, "Mobile SMS Morse messaging enabled " + isWearSMSMorseMessagingEnabled);
        if(isMobileSMSMorseMessagingEnabled){
            RemorseApplication.getInstance().getMorser().vibrateTextAsMorse(morsePlainText);
        }
    }

    private void morseOnWearDevice(Context context, String morsePlainText, String sender, boolean isWearSMSMorseMessagingEnabled) {
        Log.d(TAG, "Wear SMS Morse messaging enabled " + isWearSMSMorseMessagingEnabled);
        if(isWearSMSMorseMessagingEnabled){
            Intent morseServiceIntent = new Intent(context, MorseSenderIntentService.class);
            morseServiceIntent.putExtra(MorseSenderIntentService.EXTRA_MORSE_PLAIN_TEXT, morsePlainText);
            morseServiceIntent.putExtra(MorseSenderIntentService.EXTRA_MORSE_SENDER, sender);
            context.startService(morseServiceIntent);
        }
    }
}
