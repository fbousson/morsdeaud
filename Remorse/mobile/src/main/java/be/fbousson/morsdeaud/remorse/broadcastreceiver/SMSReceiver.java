package be.fbousson.morsdeaud.remorse.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsMessage;


/**
 * Created by fbousson on 01/12/14.
 */
public class SMSReceiver extends BroadcastReceiver {
    private final String DEBUG_TAG = getClass().getSimpleName().toString();
    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

    // Retrieve SMS
    public void onReceive(Context context, Intent intent) {

        String action = intent.getAction();

        if(action.equals(ACTION_SMS_RECEIVED)){

            String address, str = "";
//            int contactId = -1;

            SmsMessage[] msgs = getMessagesFromIntent(intent);
            if (msgs != null) {
                for (int i = 0; i < msgs.length; i++) {
                    address = msgs[i].getOriginatingAddress();
                   // contactId = ContactsUtils.getContactId(mContext, address, "address");
                    str += msgs[i].getMessageBody().toString();
                    str += "\n";
                }
            }

//            if(contactId != -1){
//                showNotification(contactId, str);
//            }

            // ---send a broadcast intent to update the SMS received in the
            // activity---
            Intent broadcastIntent = new Intent();
            broadcastIntent.setAction(MorseReceiver.ACTION_MORSE_RECEIVER);
            broadcastIntent.putExtra(MorseReceiver.EXTRA_MORSE_PLAIN_TEXT, str);
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
