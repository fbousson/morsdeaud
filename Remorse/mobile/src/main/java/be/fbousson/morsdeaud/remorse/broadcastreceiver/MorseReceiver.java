package be.fbousson.morsdeaud.remorse.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import be.fbousson.morsdeaud.remorse.service.MorseSenderIntentService;

/**
 * Created by fbousson on 01/12/14.
 */
public class MorseReceiver extends BroadcastReceiver {

    public static final String EXTRA_MORSE_PLAIN_TEXT = "MorsePlainText";

    public static final String ACTION_MORSE_RECEIVER = "be.fbousson.morsdeaud.remorse.broadcastreceiver.MorseReceiver";


    @Override
    public void onReceive(Context context, Intent intent) {


        String morsePlainText = intent.getExtras().getString(EXTRA_MORSE_PLAIN_TEXT);
        Intent morseServiceIntent = new Intent(context, MorseSenderIntentService.class);
        morseServiceIntent.putExtra(MorseSenderIntentService.EXTRA_MORSE_PLAIN_TEXT, morsePlainText);
        context.startService(morseServiceIntent);
    }
}
