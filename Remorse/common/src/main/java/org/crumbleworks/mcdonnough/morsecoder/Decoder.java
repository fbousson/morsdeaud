package org.crumbleworks.mcdonnough.morsecoder;

import java.io.InputStream;

public class Decoder {
    private MorseCodeCharacterGetter morseCodeCharacterGetter;
    private StringBuffer morseDecodedTextBuffer;

    public Decoder(InputStream is) {
        morseCodeCharacterGetter = new MorseCodeCharacterGetter(is);
    }

    public String decode(String morseEncodedText) {
        morseDecodedTextBuffer = new StringBuffer();
        String[] splittedMorseCode = morseEncodedText.split("//");

        for(int wordCounter = 0; wordCounter < splittedMorseCode.length; wordCounter++) {
            String word = splittedMorseCode[wordCounter];
            String[] letters = word.split("/");

            for(int letterCounter = 0; letterCounter < letters.length; letterCounter++) {
                String letter = letters[letterCounter];
                morseDecodedTextBuffer.append(morseCodeCharacterGetter
                        .getLetterForCode(letter));
            }

            if(wordCounter != (splittedMorseCode.length - 1))
                morseDecodedTextBuffer.append(" ");
        }

        return new String(morseDecodedTextBuffer);
    }
}