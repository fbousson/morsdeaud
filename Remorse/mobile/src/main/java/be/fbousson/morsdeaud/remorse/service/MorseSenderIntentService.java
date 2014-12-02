package be.fbousson.morsdeaud.remorse.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.internal.m;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

import java.util.concurrent.TimeUnit;

import be.fbousson.morsdeaud.common.constants.MessagingConstants;

/**
 * Created by fbousson on 01/12/14.
 */
public class MorseSenderIntentService extends IntentService implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = MorseSenderIntentService.class.getSimpleName();

    public static final String EXTRA_MORSE_PLAIN_TEXT = "MorseSenderIntentService_morsePlainText";
    public static final String EXTRA_MORSE_SENDER = "MorseSenderIntentService_morseSender";



    protected GoogleApiClient mGoogleApiClient;

    public static final int CONNECT_TIMEOUT_MS = 100;



    public MorseSenderIntentService() {
        super("MorseSenderIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        String text = intent.getExtras().getString(EXTRA_MORSE_PLAIN_TEXT);
        Log.d(TAG, "Received text " + text);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        ConnectionResult result =  mGoogleApiClient.blockingConnect(CONNECT_TIMEOUT_MS,
                TimeUnit.MILLISECONDS);

        if (!result.isSuccess()) {
            Log.e(TAG, "MorseSenderIntentService failed to connect to GoogleApiClient.");
            return;
        }
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();
        for (Node node : nodes.getNodes()) {
            Wearable.MessageApi.sendMessage(mGoogleApiClient, node.getId(), MessagingConstants.BACKGROUND_MESSAGE_PATH, text.getBytes());
        }

    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
