package be.fbousson.morsdeaud.common;

import android.os.Vibrator;
import android.util.Log;

import org.crumbleworks.mcdonnough.morsecoder.Constants;
import org.crumbleworks.mcdonnough.morsecoder.Encoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by fbousson on 25/11/14.
 */
public class Morser {


    private static final String DOT = ".";
    private static final String DASH = "-";
    private static final String SLASH = "/";




    /**
     * Default strategy. Can .be overriden
     */
    public static class MorseStrategy{
        private long dot = 200;      // Length of a Morse Code "dot" in milliseconds
        private long dash = 500;     // Length of a Morse Code "dash" in milliseconds
        private long short_gap = 201;    // Length of Gap Between dots/dashes
        private long medium_gap = 501;   // Length of Gap Between Letters
        private long long_gap = 1001;    // Length of Gap Between Words

        public void setDot(long dot) {
            this.dot = dot;
        }

        public void setDash(long dash) {
            this.dash = dash;
        }

        public void setShort_gap(long short_gap) {
            this.short_gap = short_gap;
        }

        public void setMedium_gap(long medium_gap) {
            this.medium_gap = medium_gap;
        }

        public void setLong_gap(long long_gap) {
            this.long_gap = long_gap;
        }
    }


    private static final String TAG = Morser.class.getSimpleName();

    private Encoder _morseEncoder;
    private MorseStrategy _morseStrategy;
    private Vibrator _vibrator;

    // This example will cause the phone to vibrate "SOS" in Morse Code
// In Morse Code, "s" = "dot-dot-dot", "o" = "dash-dash-dash"
// There are pauses to separate dots/dashes, letters, and words
// The following numbers represent millisecond lengths

//    long[] pattern = {
//            0,  // Start immediately
//            dot, short_gap, dot, short_gap, dot,    // s 200 201 200 201 200
//            medium_gap, 501
//            dash, short_gap, dash, short_gap, dash, // o
//            medium_gap,
//            dot, short_gap, dot, short_gap, dot,    // s
//            long_gap
//    };



    public Morser(Encoder morseEncoder, MorseStrategy morseStrategy, Vibrator vibrator) {
        _morseEncoder = morseEncoder;
        _morseStrategy = morseStrategy;
        _vibrator = vibrator;

    }

    public void vibrateTextAsMorse(String text){
        String encodedString = encodeToMorse(text);
        encodedString = encodedString.replace("//", "");
        Log.d(TAG, "Encoded String: " + encodedString);
        long[] pattern = buildPattern(encodedString, _morseStrategy);

        _vibrator.vibrate(pattern, -1);
    }

    public String encodeToMorse(String text) {
        return _morseEncoder.encode(text);
    }

    private long[] buildPattern(String encodedString, MorseStrategy morseStrategy) {
        List<Long> pattern = new ArrayList<Long>();
        String[] words = encodedString.split(" ");
        pattern.add(0l); // Start immediately
        Log.d(TAG, "Words " + Arrays.toString(words));

        for(String word : words){
            String[] letters = word.split("");
            Log.d(TAG, "Letters " + Arrays.toString(letters));
            String previous = "";
            for(String letter : letters) {

                if(previous.equals(DOT) || previous.equals(DASH)){
                    if(!letter.equals(SLASH)){
                        pattern.add(morseStrategy.short_gap);
                    }
                }

                if (DOT.equals(letter)) {
                    pattern.add(morseStrategy.dot);
                } else if (DASH.equals(letter)) {
                    pattern.add(morseStrategy.dash);
                } else if (SLASH.equals(letter)) {
                    pattern.add(morseStrategy.medium_gap);
                } else if (Constants.ERROR_STRING.equals(letter)) {
                    Log.d(TAG, "End of statement" + letter);
                    break;
                } else {
                    Log.e(TAG, "Ignoring unknown token" + letter);
                }

                previous = letter;
            }
            pattern.add(morseStrategy.long_gap);
        }



        Log.d(TAG, "Pattern as timings " + pattern );


        long[] patternArray = new long[pattern.size()];
        for(int i = 0;i < pattern.size();i++){
            patternArray[i] = pattern.get(i);
        }

        return patternArray;
    }

    public void cancel(){
        _vibrator.cancel();

    }





}
