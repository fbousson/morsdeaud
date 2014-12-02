package be.fbousson.morsdeaud.remorse;

import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataApi.DataItemResult;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageApi.SendMessageResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;

import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.fbousson.morsdeaud.common.constants.MessagingConstants;
import be.fbousson.morsdeaud.remorse.service.MorseSenderIntentService;

/**
 * Created by fbousson on 25/11/14.
 */
public class WearConnectedActivity extends ActionBarActivity implements DataApi.DataListener,
        MessageApi.MessageListener, NodeApi.NodeListener, ConnectionCallbacks,
        OnConnectionFailedListener  {

    private static final String TAG = WearConnectedActivity.class.getSimpleName();


    /** Request code for launching the Intent to resolve Google Play services errors. */
    private static final int REQUEST_RESOLVE_ERROR = 1000;

    protected GoogleApiClient mGoogleApiClient;
    private boolean mResolvingError = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!mResolvingError) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onStop() {
        if (!mResolvingError) {
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

    @Override //ConnectionCallbacks
    public void onConnected(Bundle connectionHint) {
        Log.d(TAG, "Google API Client was connected");
        mResolvingError = false;

        Wearable.DataApi.addListener(mGoogleApiClient, this);
        Wearable.MessageApi.addListener(mGoogleApiClient, this);
        Wearable.NodeApi.addListener(mGoogleApiClient, this);
    }

    @Override //ConnectionCallbacks
    public void onConnectionSuspended(int cause) {
        Log.d(TAG, "Connection to Google API client was suspended");
    }

    @Override //OnConnectionFailedListener
    public void onConnectionFailed(ConnectionResult result) {
        if (mResolvingError) {
            // Already attempting to resolve an error.
            return;
        } else if (result.hasResolution()) {
            try {
                mResolvingError = true;
                result.startResolutionForResult(this, REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                mGoogleApiClient.connect();
            }
        } else {
            Log.e(TAG, "Connection to Google API client has failed");
            mResolvingError = false;
            Wearable.DataApi.removeListener(mGoogleApiClient, this);
            Wearable.MessageApi.removeListener(mGoogleApiClient, this);
            Wearable.NodeApi.removeListener(mGoogleApiClient, this);
        }
    }

    @Override //DataListener
    public void onDataChanged(DataEventBuffer dataEvents) {
        Log.d(TAG, "onDataChanged: " + dataEvents);
        final List<DataEvent> events = FreezableUtils.freezeIterable(dataEvents);
        dataEvents.close();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                for (DataEvent event : events) {
                    if (event.getType() == DataEvent.TYPE_CHANGED) {
//                        mDataItemListAdapter.add(
//                                new Event("DataItem Changed", event.getDataItem().toString()));
//                    } else if (event.getType() == DataEvent.TYPE_DELETED) {
//                        mDataItemListAdapter.add(
//                                new Event("DataItem Deleted", event.getDataItem().toString()));
                    }
                }
            }
        });
    }

    @Override //MessageListener
    public void onMessageReceived(final MessageEvent messageEvent) {
        Log.d(TAG, "onMessageReceived() A message from watch was received:" + messageEvent
                .getRequestId() + " " + messageEvent.getPath());
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mDataItemListAdapter.add(new Event("Message from watch", messageEvent.toString()));
//            }
//        });

    }

    @Override //NodeListener
    public void onPeerConnected(final Node peer) {
        Log.d(TAG, "onPeerConnected: " + peer);
        Toast.makeText(WearConnectedActivity.this, "Peer connected: " + peer, Toast.LENGTH_SHORT).show();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mDataItemListAdapter.add(new Event("Connected", peer.toString()));
//            }
//        });

    }

    @Override //NodeListener
    public void onPeerDisconnected(final Node peer) {
        Log.d(TAG, "onPeerDisconnected: " + peer);
        Toast.makeText(WearConnectedActivity.this, "Peer disconnected: " + peer, Toast.LENGTH_SHORT).show();
//        mHandler.post(new Runnable() {
//            @Override
//            public void run() {
//                mDataItemListAdapter.add(new Event("Disconnected", peer.toString()));
//            }
//        });
    }

    private Collection<String> getNodes() {
        Set<String> results = new HashSet<String>();
        NodeApi.GetConnectedNodesResult nodes =
                Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for (Node node : nodes.getNodes()) {
            results.add(node.getId());
        }

        return results;
    }

    private void sendStartActivityMessage(String node) {
        Wearable.MessageApi.sendMessage(
                mGoogleApiClient, node, MessagingConstants.START_ACTIVITY_PATH, new byte[0]).setResultCallback(
                new ResultCallback<SendMessageResult>() {
                    @Override
                    public void onResult(SendMessageResult sendMessageResult) {
                        if (!sendMessageResult.getStatus().isSuccess()) {
                            Log.e(TAG, "Failed to send message with status code: "
                                    + sendMessageResult.getStatus().getStatusCode());
                        }
                    }
                }
        );
    }

    private class StartWearableActivityTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... args) {
            Collection<String> nodes = getNodes();
            for (String node : nodes) {
                sendStartActivityMessage(node);
            }
            return null;
        }
    }


    /** Sends an RPC to start a fullscreen Activity on the wearable. */
    protected void onStartWearableActivityClick() {
        Log.d(TAG, "Generating RPC");
        // Trigger an AsyncTask that will query for a list of connected nodes and send a
        // "start-activity" message to each connected node.
        new StartWearableActivityTask().execute();
    }

    protected  void sendMessage(final String message){
        Intent intent = new Intent(this, MorseSenderIntentService.class);
        intent.putExtra(MorseSenderIntentService.EXTRA_MORSE_PLAIN_TEXT, message);
        startService(intent);
    }


    protected  void sendMessageold(final String message){

        new Thread( new Runnable() {
            @Override
            public void run() {
                NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes( mGoogleApiClient ).await();
                for(Node node : nodes.getNodes()) {
//                    MessageApi.SendMessageResult result = Wearable.MessageApi.sendMessage(
//                            mApiClient, node.getId(), path, text.getBytes() ).await();

                    MessageApi.SendMessageResult result =     Wearable.MessageApi.sendMessage(
                            mGoogleApiClient, node.getId(), MessagingConstants.BACKGROUND_MESSAGE_PATH, message.getBytes()).await();

//                            .setResultCallback(
//                            new ResultCallback<SendMessageResult>() {
//                                @Override
//                                public void onResult(SendMessageResult sendMessageResult) {
//                                    if (!sendMessageResult.getStatus().isSuccess()) {
//                                        Log.e(TAG, "Failed to send message with status code: "
//                                                + sendMessageResult.getStatus().getStatusCode());
//                                    }
//                                }
//                            }
//                    );

                }


            }
        }).start();


    }

    /**
     * Sends the asset that was created form the photo we took by adding it to the Data Item store.
     */
    protected void sendMessageData(String message) {
        PutDataMapRequest dataMap = PutDataMapRequest.create(MessagingConstants.BACKGROUND_MESSAGE_PATH);
        dataMap.getDataMap().putString(MessagingConstants.MESSAGE_KEY, message);
        dataMap.getDataMap().putLong("time", new Date().getTime());
        PutDataRequest request = dataMap.asPutDataRequest();
        Wearable.DataApi.putDataItem(mGoogleApiClient, request)
                .setResultCallback(new ResultCallback<DataItemResult>() {
                    @Override
                    public void onResult(DataItemResult dataItemResult) {
                        Log.d(TAG, "Sending message was successful: " + dataItemResult.getStatus()
                                .isSuccess());
                    }
                });

    }
}
