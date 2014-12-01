package be.fbousson.morsdeaud.remorse;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.data.FreezableUtils;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.List;
import java.util.concurrent.TimeUnit;

import be.fbousson.morsdeaud.common.constants.MessagingConstants;

/**
 * Created by fbousson on 27/11/14.
 */
public class RemorseListenerService extends WearableListenerService {


    private static final String TAG = RemorseListenerService.class.getSimpleName();

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
       Log.d(TAG, "Message Received: " + messageEvent);
       final String messagePath =  messageEvent.getPath();
        if(messagePath.equalsIgnoreCase( MessagingConstants.START_ACTIVITY_PATH ) ) {
            Intent intent = new Intent( this, MainActivity.class );
            intent.addFlags( Intent.FLAG_ACTIVITY_NEW_TASK );
            startActivity( intent );
        }
        else if(messagePath.equalsIgnoreCase( MessagingConstants.BACKGROUND_MESSAGE_PATH )){
            Log.d(TAG, "Starting backgroud message service");
            Intent intent = new Intent( this, MorseIntentService.class );
            String message = new String( messageEvent.getData());
            Log.d(TAG, "Received message " + message);
            intent.putExtra(MorseIntentService.EXTRA_MESSAGE,message);
            Log.d(TAG, "Starting service ");
            startService(intent);
        }
        else {
            super.onMessageReceived( messageEvent );
        }
    }

    @Override
    public void onDataChanged(DataEventBuffer dataEvents) {
        if (Log.isLoggable(TAG, Log.DEBUG)) {
            Log.d(TAG, "onDataChanged: " + dataEvents);
        }
        final List<DataEvent> events = FreezableUtils
                .freezeIterable(dataEvents);

        GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .build();

        ConnectionResult connectionResult =
                googleApiClient.blockingConnect(30, TimeUnit.SECONDS);

        if (!connectionResult.isSuccess()) {
            Log.e(TAG, "Failed to connect to GoogleApiClient.");
            return;
        }

        // Loop through the events and send a message
        // to the node that created the data item.
        for (DataEvent event : events) {
            Uri uri = event.getDataItem().getUri();

            // Get the node id from the host value of the URI
            String nodeId = uri.getHost();
            // Set the data of the message to be the bytes of the URI
            byte[] payload = uri.toString().getBytes();
            Log.d(TAG, "Data changed on uri " + uri + " payload: " + payload);

            // Send the RPC
//            Wearable.MessageApi.sendMessage(googleApiClient, nodeId,
//                    DATA_ITEM_RECEIVED_PATH, payload);
        }
    }
}
