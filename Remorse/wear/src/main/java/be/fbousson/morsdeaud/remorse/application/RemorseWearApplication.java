package be.fbousson.morsdeaud.remorse.application;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;

import org.crumbleworks.mcdonnough.morsecoder.Encoder;

import be.fbousson.morsdeaud.common.Morser;
import be.fbousson.morsdeaud.remorse.R;

/**
 * Created by fbousson on 01/12/14.
 */
public class RemorseWearApplication extends Application {

    private static RemorseWearApplication _instance;


    private Morser _morser;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        _morser = new Morser(new Encoder( getResources().openRawResource(R.raw.morsecode)), new Morser.MorseStrategy(), (Vibrator) getSystemService(Context.VIBRATOR_SERVICE));
    }

    public static RemorseWearApplication getInstance(){
        return _instance;
    }

    public Morser getMorser() {
        return _morser;
    }
}
