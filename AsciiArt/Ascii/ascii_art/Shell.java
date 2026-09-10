package ascii_art;

import ascii_art.img_to_char.BrightnessImgCharMatcher;
import ascii_output.AsciiOutput;
import ascii_output.HtmlAsciiOutput;
import image.Image;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Class which represent an application that asks the user to change
 * whatever he wants in the rendered asciiArt (resolution,
 * type and number of chars it is made of, whether it is rendered to
 * a file or to the console).
 * @author elias.
 */
public class Shell {

    private static final String INITIAL_CHARS_RANGE = "0-9";
    private static final int INITIAL_CHARS_IN_ROW = 64;
    private static final int MIN_PIXELS_PER_CHAR = 2;
    private static final String FONT_NAME = "Courier New";
    private static final String OUTPUT_FILENAME = "out.html";
    private static final String CMD_EXIT = "exit";
    private final Set<Character> charSet = new HashSet<>();
    private final int minCharsInRow;
    private final int maxCharsInRow;
    private int charsInRow;
    private BrightnessImgCharMatcher charMatcher;
    private AsciiOutput output;
    private boolean isConsole = false;

    /**
     * Constructor.
     * @param image The image which will be converted to ASCII Art.
     */
    public Shell(Image image) {
        addChars(INITIAL_CHARS_RANGE);
        minCharsInRow = Math.max(1, image.getWidth()/image.getHeight());
        maxCharsInRow = image.getWidth() / MIN_PIXELS_PER_CHAR;
        charsInRow = Math.max(Math.min(INITIAL_CHARS_IN_ROW, maxCharsInRow),
                minCharsInRow);
        charMatcher = new BrightnessImgCharMatcher(image, FONT_NAME);
        output = new HtmlAsciiOutput(OUTPUT_FILENAME, FONT_NAME);
    }

    /**
     * Main method in this class. It runs the application, and asks the
     * user for input and changes the asciiArt rendered accordingly.
     */
    public void run() {
        Scanner scanner = new Scanner(System.in);
        System.out.print(">>> ");
        String cmd = scanner.next().trim();
        while(true) {
            var param = scanner.nextLine().trim();
            if (cmd.equals(CMD_EXIT) && param.isBlank()){
                return;
            }
            command(cmd, param);
            System.out.print(">>> ");
            cmd = scanner.next();
        }
    }

    /*
     * Private method that takes the input of the user
     * and performs the action he needs.
     */
    private void command(String cmd, String param) {
        switch (cmd) {
            case "chars":
                if (checkSomeErr(param)) {
                    break;
                }
                showChars();
                break;
            case "add":
                addChars(param);
                break;
            case "remove":
                removeChars(param);
                break;
            case "res":
                resChange(param);
                break;
            case "console":
                if (checkSomeErr(param)) {
                    break;
                }
                isConsole = true;
                break;
            case "render":
                if (checkSomeErr(param)) {
                    break;
                }
                render();
                break;
            default:
                System.out.println("Your input is illegal");
        }
    }

    /*
     * Private method that checks that nothing is written
     * after the commands chars, console and render.
     */
    private boolean checkSomeErr(String param) {
        if (!param.isBlank()) {
            System.out.println("Your input is illegal");
            return true;
        }
        return false;
    }

    /*
     * Private method that passes through the charSet containing
     * the characters we can use in construct the image and prints them.
     * It prints them according to ascii value of each character in ascending order.
     */
    private void showChars() {
        charSet.stream().sorted().forEach(c-> System.out.print(c + " "));
        System.out.println();
    }

    /*
     * Private method that gets a string, calls parseCharRange
     * on it and add the chars we get from this method to charSet.
     */
    private void addChars(String s) {
        if (s.isBlank()){
            System.out.println("What do you want to add?");
            return;
        }
        char[] range = parseCharRange(s);
        if(range != null) {
            Stream.iterate(range[0], c -> c <= range[1],
                    c -> (char)((int)c+1)).forEach(charSet::add);
        }
    }

    /*
     * Private method that gets a string, calls parseCharRange
     * on it and remove the chars we get from this method from charSet.
     */
    private void removeChars(String s) {
        if (s.isBlank()){
            System.out.println("What do you want to remove?");
            return;
        }
        char[] range = parseCharRange(s);
        if(range != null) {
            Stream.iterate(range[0], c -> c <= range[1],
                    c -> (char)((int)c+1)).forEach(charSet::remove);
        }
    }

    /*
     * Private method that gets a String, multiplies/divides
     * charsInRow by 2 when the String is up/down in case we
     * didn't exceed the maxCharsInRow/minCharsInRow.
     */
    private void resChange(String s) {
        if (s.isBlank()){
            System.out.println("You want to res up or down?");
            return;
        }
        switch (s) {
            case "up":
                if (charsInRow * 2 <= maxCharsInRow) {
                    charsInRow *= 2;
                    System.out.println("Width set to " + charsInRow);
                }
                else {
                    System.out.println("You're using the maximum resolution");
                }
                break;
            case "down":
                if (charsInRow / 2 >= minCharsInRow) {
                    charsInRow /= 2;
                    System.out.println("Width set to " + charsInRow);
                }
                else {
                    System.out.println("You're using the minimum resolution");
                }
                break;
            default:
                System.out.println("Your input is illegal");
        }
    }

    /*
     * Private method that renders the asciiArt to the
     * console if isConsole == true, else to a file.
     */
    private void render() {
        if (charSet.size() <= 0){
            return;
        }
        Character[] characters = new Character[charSet.size()];
        charSet.toArray(characters);
        char[][] ascii = charMatcher.chooseChars(charsInRow, characters);
        if (isConsole) {
            for (char[] row : ascii) {
                for (char letter : row) {
                    System.out.print(letter);
                }
                System.out.println();
            }
            return;
        }
        output.output(ascii);
    }

    /*
     * Private method that gets a string, checks its content and
     * according to this check, it returns the range of chars we need.
     */
    private static char[] parseCharRange(String param) {
        if (param.length() == 1) {
            return new char[] {param.charAt(0), param.charAt(0)};
        }
        if (param.length() == 3 && param.charAt(1) == '-') {
            if (param.charAt(0) > param.charAt(2)){
                return new char[] {param.charAt(2), param.charAt(0)};
            }
            return new char[] {param.charAt(0), param.charAt(2)};
        }
        switch (param) {
            case "space":
                return new char[] {' ', ' '};
            case "all":
                return new char[] {' ', '~'};
            default:
                System.out.println("Your input is illegal");
                return null;
        }
    }
}
