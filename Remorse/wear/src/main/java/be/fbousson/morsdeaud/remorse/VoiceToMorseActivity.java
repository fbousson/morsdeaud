package be.fbousson.morsdeaud.remorse;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import be.fbousson.morsdeaud.common.Morser;
import be.fbousson.morsdeaud.remorse.application.RemorseWearApplication;

/**
 * Created by fbousson on 01/12/14.
 */
public class VoiceToMorseActivity  extends Activity {


    private static final String TAG = VoiceToMorseActivity.class.getSimpleName();


    private static final int SPEECH_REQUEST_CODE = 0;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_to_morse);


        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {

                Button  speakMorseButton = (Button) stub.findViewById(R.id.voice_to_morse_speak_button);
                speakMorseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "speakMorseButton clicked");
                        displaySpeechRecognizer();
                    }
                });

            }
        });


    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
// Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }

    // This callback is invoked when the Speech Recognizer returns.
// This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (requestCode == SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(
                    RecognizerIntent.EXTRA_RESULTS);
            String spokenText = results.get(0);
            Log.d(TAG, "Spoken text " + spokenText);
            Intent intent = new Intent(VoiceToMorseActivity.this, SeeAndFeelMorseActivity.class);
            intent.putExtra(SeeAndFeelMorseActivity.EXTRA_SEE_AND_FEEL_TEXT, spokenText);
            startActivity(intent);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
