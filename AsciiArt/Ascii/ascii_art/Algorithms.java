package ascii_art;

import java.util.HashSet;
import java.util.Set;

/**
 * Class that contains solutions for two problems.
 * @author elias.
 */
public class Algorithms {

    /**
     * Method that gets an array of length n+1 and contains numbers from 1 to n.
     * It returns the number that is present more than one time in the array.
     * @param numList The array of numbers.
     * @return Number that is present more than one time in the array.
     */
    public static int findDuplicate(int[] numList) {
        int slowPointer = numList[0];
        int fastPointer = numList[0];
        while (true)
        {
            slowPointer = numList[slowPointer];
            fastPointer = numList[numList[fastPointer]];
            if (slowPointer == fastPointer)
                break;
        }
        slowPointer = numList[0];
        while (slowPointer != fastPointer)
        {
            slowPointer = numList[slowPointer];
            fastPointer = numList[fastPointer];
        }
        return slowPointer;
    }

    /**
     * Method that takes array of Strings and finds the number of
     * unique Morse codes are there in this array.
     * @param words array of Strings.
     * @return How many unique Morse codes are there in this list.
     */
    public static int uniqueMorseRepresentations(String[] words) {
        String[] morseTable = new String[] {".-","-...","-.-.","-..",".","..-.","--.",
                "....","..",".---","-.-",".-..","--","-.","---",".--.","--.-",".-.",
                "...","-","..-","...-",".--","-..-","-.--","--.."};
        Set<String> morseWords = new HashSet<>();
        for (String word : words) {
            StringBuilder morse = new StringBuilder();
            for (int i = 0; i < word.length(); i++) {
                morse.append(morseTable[word.charAt(i) - 97]);
            }
            morseWords.add(morse.toString());
        }
        return morseWords.size();
    }

}
