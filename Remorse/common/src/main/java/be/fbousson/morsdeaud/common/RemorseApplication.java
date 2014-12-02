package be.fbousson.morsdeaud.common;

import android.app.Application;
import android.content.Context;
import android.os.Vibrator;
import android.util.Log;

import org.crumbleworks.mcdonnough.morsecoder.Encoder;

/**
 * Created by fbousson on 02/12/14.
 */
public class RemorseApplication extends Application {

    protected static final String TAG = RemorseApplication.class.getSimpleName();


    private static RemorseApplication _instance;

    private Morser _morser;

    @Override
    public void onCreate() {
        super.onCreate();
        _instance = this;
        _morser = new Morser(new Encoder( getResources().openRawResource(R.raw.morsecode)), new Morser.MorseStrategy(), (Vibrator) getSystemService(Context.VIBRATOR_SERVICE));
    }

    public static RemorseApplication getInstance(){
        return _instance;
    }

    public Morser getMorser() {
        return _morser;
    }

}
