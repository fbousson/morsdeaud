package be.fbousson.morsdeaud.remorse;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;

import org.crumbleworks.mcdonnough.morsecoder.Encoder;

import be.fbousson.morsdeaud.common.Morser;
import be.fbousson.morsdeaud.remorse.application.RemorseWearApplication;

/**
 * Created by fbousson on 27/11/14.
 */
public class MorseIntentService extends IntentService {

    public static final String EXTRA_MESSAGE = "MorseIntentService_message" ;

    private Morser _morser;


    public MorseIntentService() {
        super(MorseIntentService.class.getSimpleName());
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        _morser = RemorseWearApplication.getInstance().getMorser();
        String message = intent.getExtras().getString(EXTRA_MESSAGE);
        if(message != null){
            _morser.vibrateTextAsMorse(message);
        }
    }
}
