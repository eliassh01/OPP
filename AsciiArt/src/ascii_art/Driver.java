package ascii_art;
import image.Image;
import java.util.logging.Logger;

/**
 * This is the main Class in our program.
 * It has a main method. It takes the arguments,
 * checks their legality and initialises Object Shell.
 * @author elias.
 */
public class Driver {

    public static void main(String[] args) throws Exception {
        if (args.length != 1) {
            System.err.println("USAGE: java asciiArt ");
            return;
        }
        Image img = Image.fromFile(args[0]);
        if (img == null) {
            Logger.getGlobal().severe("Failed to open image file " +
                    args[0]);
            return;
        }
        new Shell(img).run();
    }
}