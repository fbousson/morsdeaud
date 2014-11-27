package be.fbousson.morsdeaud.remorse;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;

import org.crumbleworks.mcdonnough.morsecoder.Encoder;

import be.fbousson.morsdeaud.common.Morser;
import be.fbousson.morsdeaud.common.constants.MessagingConstants;

public class MainActivity extends Activity  implements MessageApi.MessageListener,  GoogleApiClient.ConnectionCallbacks {

    private static final String TAG = MainActivity.class.getSimpleName();

    private Morser _morser;

    private TextView mTextView;
    private Button morseButton;


    private GoogleApiClient mApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _morser = new Morser(new Encoder( getResources().openRawResource(R.raw.morsecode)), new Morser.MorseStrategy(), (Vibrator) getSystemService(Context.VIBRATOR_SERVICE));

        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                morseButton = (Button) stub.findViewById(R.id.debug_morse_button);
                morseButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "MorseButton clicked");
                        _morser.vibrateTextAsMorse("sms");
                    }
                });

            }
        });

        initGoogleApiClient();

    }

    private void initGoogleApiClient() {
        mApiClient = new GoogleApiClient.Builder( this )
                .addApi( Wearable.API )
                .addConnectionCallbacks( this )
                .build();

        if( mApiClient != null && !( mApiClient.isConnected() || mApiClient.isConnecting() ) )
            mApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( mApiClient != null && !( mApiClient.isConnected() || mApiClient.isConnecting() ) )
            mApiClient.connect();
    }

    @Override
    public void onConnected( Bundle bundle ) {
        Wearable.MessageApi.addListener( mApiClient, this );

    }

    @Override
    protected void onStop() {
        if ( mApiClient != null ) {
            Wearable.MessageApi.removeListener( mApiClient, this );
            if ( mApiClient.isConnected() ) {
                mApiClient.disconnect();
            }
        }
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if( mApiClient != null )
            mApiClient.unregisterConnectionCallbacks( this );
        super.onDestroy();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onMessageReceived( final MessageEvent messageEvent ) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                if( messageEvent.getPath().equalsIgnoreCase(MessagingConstants.MESSAGE_PATH) ) {
                    String message = new String( messageEvent.getData());
                    Log.d(TAG, "received message " + message);
                    mTextView.setText(message);
                    _morser.vibrateTextAsMorse(message);


                }
            }
        });
    }
}
