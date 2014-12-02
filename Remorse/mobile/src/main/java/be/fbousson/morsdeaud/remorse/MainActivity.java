package be.fbousson.morsdeaud.remorse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import be.fbousson.morsdeaud.common.Morser;
import be.fbousson.morsdeaud.remorse.preferences.UserPreferences;


public class MainActivity extends WearConnectedActivity {


    private static final String TAG = MainActivity.class.getSimpleName();

    private Morser _morser;
    private EditText _morseText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _morser = RemorseMobileApplication.getInstance().getMorser();

        String sampleText = getString(R.string.debug_sample_message);

        final TextView morseTextView = (TextView) findViewById(R.id.debug_message_morse);
        morseTextView.setText(_morser.encodeToMorse(sampleText));

        _morseText = (EditText) findViewById(R.id.debug_message_edit);
        _morseText.setText(sampleText);

        _morseText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                morseTextView.setText(_morser.encodeToMorse(s.toString()));
            }
        });

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

        CheckBox mobileCheckbox = (CheckBox) findViewById(R.id.enable_mobile_morse_checkbox);

        mobileCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferences.setMobileMorseMessagingEnabled(isChecked);
            }
        });

        mobileCheckbox.setChecked(UserPreferences.isMobileMorseMessagingEnabled());


        CheckBox wearCheckbox = (CheckBox) findViewById(R.id.enable_wear_morse_checkbox);

        wearCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                UserPreferences.setWearMorseMessagingEnabled(isChecked);
            }
        });

        wearCheckbox.setChecked(UserPreferences.isWearMorseMessagingEnabled());


    }

    private String getTextToMorse() {
        return _morseText.getText().toString();
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
