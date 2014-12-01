package be.fbousson.morsdeaud.remorse;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import be.fbousson.morsdeaud.common.Morser;
import be.fbousson.morsdeaud.remorse.application.RemorseWearApplication;

/**
 * Created by fbousson on 01/12/14.
 */
public class SeeAndFeelMorseActivity extends Activity {


    private static final String TAG = SeeAndFeelMorseActivity.class.getSimpleName();

    public static final String EXTRA_SEE_AND_FEEL_TEXT = "extra_seeAndFeelMorseText";

    private Morser _morser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        _morser = RemorseWearApplication.getInstance().getMorser();
        final String plainText = getIntent().getExtras().getString(EXTRA_SEE_AND_FEEL_TEXT);
        Log.d(TAG, "Morsing text " + plainText);
        setContentView(R.layout.activity_see_and_feel_morse);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                TextView morseTextView = (TextView) stub.findViewById(R.id.see_and_feel_morse_result_text);
                morseTextView.setText(_morser.encodeToMorse(plainText));
                _morser.vibrateTextAsMorse(plainText);
            }
        });





    }
}
