package be.fbousson.morsdeaud.remorse.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;
import android.util.Log;


/**
 * Created by fbousson on 01/12/14.
 */
public class SMSReceiver extends BroadcastReceiver {
    private final String TAG = getClass().getSimpleName();
    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    // Retrieve SMS
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(action.equals(ACTION_SMS_RECEIVED)){

            String address = "";
            String messages = "";
//            int contactId = -1;

            SmsMessage[] msgs = getMessagesFromIntent(intent);
            if (msgs != null) {
                for (int i = 0; i < msgs.length; i++) {
                    //Can we get multiple originating addresses in a single intent?
                    address = msgs[i].getOriginatingAddress();
                   // contactId = ContactsUtils.getContactId(mContext, address, "address");
                    messages += msgs[i].getMessageBody().toString();
                    messages += "\n";
                }
            }

//            if(contactId != -1){
//                showNotification(contactId, str);
//            }
            Log.d(TAG,"Received SMS " + messages);
            Intent broadcastIntent = new Intent();
            broadcastIntent.putExtra(MorseReceiver.EXTRA_MORSE_PLAIN_TEXT, messages);
            broadcastIntent.putExtra(MorseReceiver.EXTRA_SENDER, address);
            broadcastIntent.setAction(MorseReceiver.ACTION_MORSE_RECEIVER);


            context.sendBroadcast(broadcastIntent);
        }

    }

    public static SmsMessage[] getMessagesFromIntent(Intent intent) {
        Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        byte[][] pdus = new byte[pduObjs.length][];
        int pduCount = pdus.length;
        SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

}
