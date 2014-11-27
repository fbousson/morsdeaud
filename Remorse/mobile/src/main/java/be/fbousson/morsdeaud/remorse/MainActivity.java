package be.fbousson.morsdeaud.remorse;

import android.content.Context;
import android.os.Vibrator;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.crumbleworks.mcdonnough.morsecoder.Encoder;

import be.fbousson.morsdeaud.common.Morser;


public class MainActivity extends WearConnectedActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private Morser _morser;
    private EditText _morseText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _morser = new Morser(new Encoder( getResources().openRawResource(R.raw.morsecode)), new Morser.MorseStrategy(), (Vibrator) getSystemService(Context.VIBRATOR_SERVICE));


        _morseText = (EditText) findViewById(R.id.debug_message_edit);
        Button morseOnMobileButton = (Button) findViewById(R.id.debug_morse_on_mobile_button);

        morseOnMobileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _morser.vibrateTextAsMorse(getTextToMorse());
            }
        });

        Button cancelButton = (Button) findViewById(R.id.debug_morse_cancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _morser.cancel();
            }
        });

        Button morseOnWearButton = (Button) findViewById(R.id.debug_morse_on_wear);
//        morseOnMobileButton.setEnabled(mGoogleApiClient.isConnected());
        morseOnWearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               sendMessage(getTextToMorse());
            }
        });

        Button startWearAppButton = (Button) findViewById(R.id.debug_start_wear_app);
        startWearAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Starting wear app");
                onStartWearableActivityClick();
            }
        });


    }

    private String getTextToMorse() {
        return _morseText.getText().toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
