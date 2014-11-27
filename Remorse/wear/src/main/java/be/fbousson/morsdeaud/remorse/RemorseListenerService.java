package be.fbousson.morsdeaud.remorse;

import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

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
}
