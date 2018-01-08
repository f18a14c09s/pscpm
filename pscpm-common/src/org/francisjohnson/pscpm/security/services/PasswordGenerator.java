package org.francisjohnson.pscpm.security.services;

import java.security.SecureRandom;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


/**
 * Standalone password generator.
 * Note that min length and max length are not properties because they are
 * agruments to at least one overloaded version of generatePassword().
 */
public class PasswordGenerator {
    private static final String DEFAULT_LOWERCASE_ALPHABET =
        "abcdefghijklmnopqrstuvwxyz";
    private static final String DEFAULT_UPPERCASE_ALPHABET =
        "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DEFAULT_DIGIT_ALPHABET = "0123456789";
    private static final String DEFAULT_SPECIAL_CHARACTER_ALPHABET =
        "`-=[]\\;'./~!@#$%^&*()_+{}|:\"<>?";
    private static final int ABSOLUTE_MIN_LENGTH_GENERATED = 4;

    /**
     * Note that this constant is non-static due to Java language constraints.
     */
    private final int DEFAULT_MIN_LENGTH_GENERATED =
        Math.max(15, DEFAULT_MIN_LOWERCASE_GENERATED +
                 DEFAULT_MIN_UPPERCASE_GENERATED +
                 DEFAULT_MIN_DIGITS_GENERATED +
                 DEFAULT_MIN_SPECIAL_CHARACTERS_GENERATED);
    private static final int DEFAULT_MAX_LENGTH_GENERATED = 30;
    private static final int DEFAULT_MIN_LOWERCASE_GENERATED = 2;
    private static final int DEFAULT_MIN_UPPERCASE_GENERATED = 2;
    private static final int DEFAULT_MIN_DIGITS_GENERATED = 2;
    private static final int DEFAULT_MIN_SPECIAL_CHARACTERS_GENERATED = 2;
    //
    //
    //

    private SecureRandom rand = new SecureRandom();
    private String lowerCaseAlphabet = DEFAULT_LOWERCASE_ALPHABET;
    private String upperCaseAlphabet = DEFAULT_UPPERCASE_ALPHABET;
    private String digitAlphabet = DEFAULT_DIGIT_ALPHABET;
    private String specialCharacterAlphabet =
        DEFAULT_SPECIAL_CHARACTER_ALPHABET;
    private int minLowerCaseGenerated = DEFAULT_MIN_LOWERCASE_GENERATED;
    private int minUpperCaseGenerated = DEFAULT_MIN_UPPERCASE_GENERATED;
    private int minDigitsGenerated = DEFAULT_MIN_DIGITS_GENERATED;
    private int minSpecialCharactersGenerated =
        DEFAULT_MIN_SPECIAL_CHARACTERS_GENERATED;

    //
    //
    //

    /**
     * Constructor that initializes all of the properties to default values.
     */
    public PasswordGenerator() {
        super();
    }

    /**
     * Constructor that overrides the property default values.  Note that min
     * length and max length are not arguments because they are passed to an
     * overloaded version of generatePassword() instead.
     * @param lowerCaseAlphabet String.  e.g. "abcdefghijklmnopqrstuvwxyz"
     * @param upperCaseAlphabet String.  e.g. "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
     * @param digitAlphabet String.  e.g. "0123456789"
     * @param specialCharacterAlphabet String.  e.g. "`-=[]\\;'./~!@#$%^&*()_+{}|:\"<>?"
     * @param minLowerCaseGenerated int.  Minimum number of characters from the
     * lowercase alphabet.
     * @param minUpperCaseGenerated int.  Minimum number of characters from the
     * UPPERCASE alphabet.
     * @param minDigitsGenerated int.  Minimum number of characters from the
     * digit alphabet.
     * @param minSpecialCharactersGenerated int.  Minimum number of characters
     * from the special character alphabet.
     */
    public PasswordGenerator(String lowerCaseAlphabet,
                             String upperCaseAlphabet, String digitAlphabet,
                             String specialCharacterAlphabet,
                             int minLowerCaseGenerated,
                             int minUpperCaseGenerated, int minDigitsGenerated,
                             int minSpecialCharactersGenerated) {
        super();
        if (lowerCaseAlphabet != null && lowerCaseAlphabet.length() >= 1) {
            setLowerCaseAlphabet(lowerCaseAlphabet);
        }
        if (upperCaseAlphabet != null && upperCaseAlphabet.length() >= 1) {
            setUpperCaseAlphabet(upperCaseAlphabet);
        }
        if (digitAlphabet != null && digitAlphabet.length() >= 1) {
            setDigitAlphabet(digitAlphabet);
        }
        if (specialCharacterAlphabet != null &&
            specialCharacterAlphabet.length() >= 1) {
            setSpecialCharacterAlphabet(specialCharacterAlphabet);
        }
        setMinLowerCaseGenerated(Math.max(0, minLowerCaseGenerated));
        setMinUpperCaseGenerated(Math.max(0, minUpperCaseGenerated));
        setMinDigitsGenerated(Math.max(0, minDigitsGenerated));
        setMinSpecialCharactersGenerated(Math.max(0,
                                                  minSpecialCharactersGenerated));
    }

    //
    //
    //

    public static void main(String... args) {
        PasswordGenerator gen = new PasswordGenerator();
        gen.setSpecialCharacterAlphabet("-=[];./~!#$%^&*()_+{}|:<>?");
        for (int i = 0; i < 82; i++) {
            System.out.println(gen.generatePassword());
        }
    }

    //
    //
    //

    public char[] generatePassword() {
        return generatePassword(DEFAULT_MIN_LENGTH_GENERATED,
                                DEFAULT_MAX_LENGTH_GENERATED);
    }

    public char[] generatePassword(int minLength, int maxLength) {
        char[] retval = null;
        // Sort out the length constraints.
        final int absoluteMinLength =
            Math.max(minLowerCaseGenerated + minUpperCaseGenerated +
                     minDigitsGenerated + minSpecialCharactersGenerated,
                     ABSOLUTE_MIN_LENGTH_GENERATED);
        minLength = Math.max(absoluteMinLength, minLength);
        maxLength = Math.max(absoluteMinLength, maxLength);
        retval =
                new char[minLength + ((maxLength - minLength) <= 0 ? 0 : rand.nextInt(maxLength -
                                                                                      minLength))];
        // Collect all of the array's indices and randomly shuffle them.
        List<Integer> indices = new ArrayList<Integer>();
        for (int i = 0; i < retval.length; i++) {
            indices.add(i);
        }
        Collections.shuffle(indices, rand);
        // Collect all of the character classes and randomly shuffle them.
        List<CharacterClass> characterClasses =
            Arrays.asList(new CharacterClass(lowerCaseAlphabet,
                                             Math.max(0, minLowerCaseGenerated)),
                          new CharacterClass(upperCaseAlphabet,
                                             Math.max(0, minUpperCaseGenerated)),
                          new CharacterClass(digitAlphabet,
                                             Math.max(0, minDigitsGenerated)),
                          new CharacterClass(specialCharacterAlphabet,
                                             Math.max(0,
                                                      minSpecialCharactersGenerated)));
        Collections.shuffle(characterClasses, rand);
        // Randomly generate part of the password, satisfying the minimum
        // character requirements of each character class.
        for (CharacterClass charClass : characterClasses) {
            // Randomly shuffle the actual characters.
            Collections.shuffle(charClass.alphabet, rand);
            // Randomly generate the minimum number of characters required by
            // the current character class.
            for (int j = 0; j < charClass.minGenerated; j++) {
                retval[indices.remove(0).intValue()] =
                        charClass.alphabet.get(rand.nextInt(charClass.alphabet.size())).charValue();
            }
        }
        // Concatenate the alphabets from all of the character classes into one
        // alphabet, and randomly shuffle the characters.
        List<Character> fullAlphabet = new ArrayList<Character>();
        for (CharacterClass charClass : characterClasses) {
            fullAlphabet.addAll(charClass.alphabet);
        }
        Collections.shuffle(fullAlphabet, rand);
        // Randomly generate the remainder of the password in order to satisfy
        // the randomly selected password length.
        while (!indices.isEmpty()) {
            retval[indices.remove(0).intValue()] =
                    fullAlphabet.get(rand.nextInt(fullAlphabet.size())).charValue();
        }
        return retval;
    }

    private void setRand(SecureRandom rand) {
        this.rand = rand;
    }

    private SecureRandom getRand() {
        return rand;
    }

    private void setLowerCaseAlphabet(String lowerCaseAlphabet) {
        this.lowerCaseAlphabet = lowerCaseAlphabet;
    }

    private String getLowerCaseAlphabet() {
        return lowerCaseAlphabet;
    }

    private void setUpperCaseAlphabet(String upperCaseAlphabet) {
        this.upperCaseAlphabet = upperCaseAlphabet;
    }

    private String getUpperCaseAlphabet() {
        return upperCaseAlphabet;
    }

    private void setDigitAlphabet(String digitAlphabet) {
        this.digitAlphabet = digitAlphabet;
    }

    private String getDigitAlphabet() {
        return digitAlphabet;
    }

    private void setSpecialCharacterAlphabet(String specialCharacterAlphabet) {
        this.specialCharacterAlphabet = specialCharacterAlphabet;
    }

    private String getSpecialCharacterAlphabet() {
        return specialCharacterAlphabet;
    }

    private void setMinLowerCaseGenerated(int minLowerCaseGenerated) {
        this.minLowerCaseGenerated = minLowerCaseGenerated;
    }

    private int getMinLowerCaseGenerated() {
        return minLowerCaseGenerated;
    }

    private void setMinUpperCaseGenerated(int minUpperCaseGenerated) {
        this.minUpperCaseGenerated = minUpperCaseGenerated;
    }

    private int getMinUpperCaseGenerated() {
        return minUpperCaseGenerated;
    }

    private void setMinDigitsGenerated(int minDigitsGenerated) {
        this.minDigitsGenerated = minDigitsGenerated;
    }

    private int getMinDigitsGenerated() {
        return minDigitsGenerated;
    }

    private void setMinSpecialCharactersGenerated(int minSpecialCharactersGenerated) {
        this.minSpecialCharactersGenerated = minSpecialCharactersGenerated;
    }

    private int getMinSpecialCharactersGenerated() {
        return minSpecialCharactersGenerated;
    }

    private static final class CharacterClass {
        List<Character> alphabet = new ArrayList<Character>();
        int minGenerated;

        CharacterClass(String alphabet, int minGenerated) {
            for (int i = 0; i < alphabet.length(); i++) {
                this.alphabet.add(alphabet.charAt(i));
            }
            this.minGenerated = minGenerated;
        }
    }
}
