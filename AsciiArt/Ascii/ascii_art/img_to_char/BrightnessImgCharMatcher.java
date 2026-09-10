package ascii_art.img_to_char;

import image.Image;
import java.awt.Color;
import java.util.HashMap;

/**
 * Class that converts image files to an ASCII Art,
 * which is a picture made of letters of different brightnesses.
 * @author elias.
 */
public class BrightnessImgCharMatcher {

    private final Image image;
    private final String font;
    private final HashMap<Image, Double> cache = new HashMap<>();

    /**
     * Constructor.
     * @param image The image which will be converted to ASCII Art.
     * @param font The font of the ASCII Art.
     */
    public BrightnessImgCharMatcher(Image image, String font) {
        this.image = image;
        this.font = font;
    }

    /**
     * Method that converts the Image we have to an ASCII Art.
     * This art is made up of letters (chars) of different brightnesses we have in charSet.
     * @param numCharsInRow Number of letters (characters) in each row in the ASCII picture.
     * @param charSet The chars we build the ASCII from.
     * @return Two-dimensional array of chars placed in the correct indexes.
     *         This array represents the ASCII picture.
     */
    public char[][] chooseChars(int numCharsInRow, Character[] charSet) {
        if (charSet.length == 0){
            return null;
        }
        double[] arrBrightness = calculateCharArrBrightness(charSet);
        linearStretching(arrBrightness);
        return convertImageToAscii(numCharsInRow, arrBrightness, charSet);
    }

    /*
     * Private Method that takes a Character and calculates its brightness
     * by counting the number of trues in its boolean representation.
     * It returns the brightness.
     */
    private double calculateCharBrightness(Character letter) {
        boolean[][] charBooleanRepresentation = CharRenderer.getImg(letter, 16, font);
        double pixelsNeeded = 0;
        for (boolean[] row : charBooleanRepresentation) {
            for (boolean value : row) {
                if (value) {
                    pixelsNeeded++;
                }
            }
        }
        return pixelsNeeded / (16*16);
    }

    /*
     * Private method that takes an array of Characters and for each Character
     * it calculates its brightness using calculateCharBrightness method.
     * It returns an array of brightnesses.
     */
    private double[] calculateCharArrBrightness(Character[] charSet) {
        double[] arrBrightness = new double[charSet.length];
        for (int i = 0; i < charSet.length; i++) {
            arrBrightness[i] = calculateCharBrightness(charSet[i]);
        }
        return arrBrightness;
    }

    /*
     * Private method that takes the array of brightnesses
     * and do a linear stretching to the values in it using an equation.
     * ir calls takeMax and takeMin methods.
     * It returns nothing.
     */
    private void linearStretching(double[] charBrightnessLevels) {
        double maxBrightness = takeMax(charBrightnessLevels);
        double minBrightness = takeMin(charBrightnessLevels);
        for (int i = 0; i < charBrightnessLevels.length; i++) {
            double newCharBrightness = (charBrightnessLevels[i] - minBrightness) /
                                      (maxBrightness - minBrightness);
            charBrightnessLevels[i] = newCharBrightness;
        }
    }

    /*
     * Private method that cuts the Image we have to subImages, for every
     * subImage we get the closest char brightness to it using getClosestChar
     * method. Then it put this letter in the correct index in the asciiArt.
     * It returns asciiArt two-dimensional array.
     */
    private char[][] convertImageToAscii(int numCharInRow, double[] stretchedBrightness,
                                         Character[] charSet) {
        int pixelsOfSubImg = image.getWidth() / numCharInRow;
        int rowUntilNow = 0;
        int columnsUntilNow = 0;
        char[][] asciiArt = new char[(image.getHeight()/pixelsOfSubImg)]
                [(image.getWidth()/pixelsOfSubImg)];
        for (Image subImage : image.squareSubImagesOfSize(pixelsOfSubImg)) {
            Character closestLetterBrightness = getClosestChar(stretchedBrightness,
                    charSet, subImage);
            asciiArt[rowUntilNow][columnsUntilNow] = closestLetterBrightness;
            columnsUntilNow++;
            if (columnsUntilNow >= image.getWidth()/pixelsOfSubImg) {
                rowUntilNow++;
                columnsUntilNow = 0;
            }
        }
        return asciiArt;
    }

    /*
     * Private method that takes a subImage, calculates its brightness
     * using averageImageBrightness method, then it finds by a loop the
     * letter (Character) that has the closest brightness to the subImage brightness.
     * It returns this closest letter.
     */
    private Character getClosestChar(double[] stretchedBrightness, Character[] charSet,
                                     Image subImage) {
        double averageImageBrightness = averageImageBrightness(subImage);
        double smallestDifference = Math.abs(averageImageBrightness - stretchedBrightness[0]);
        Character closestLetterBrightness = charSet[0];
        for (int i = 0; i < stretchedBrightness.length; i++) {
            double difference = Math.abs(averageImageBrightness - stretchedBrightness[i]);
            if (smallestDifference > difference) {
                smallestDifference = difference;
                closestLetterBrightness = charSet[i];
            }
        }
        return closestLetterBrightness;
    }

    /*
     * Private method that takes an image, passes over the pixels in it
     * and calculates the brightnessSum.
     * Then, it do: brightnessSum/number of pixels.
     * It returns averageImageBrightness.
     */
    private double averageImageBrightness(Image image) {
        if (cache.containsKey(image)){
            return cache.get(image);
        }
        double brightnessSum = 0;
        int numOfPixels = 0;
        for (Color pixels : image.pixels()) {
            brightnessSum += ((pixels.getRed() * 0.2126) + (pixels.getGreen()
                    * 0.7152) + (pixels.getBlue() * 0.0722)) / 255;
            numOfPixels++;
        }
        cache.put(image, brightnessSum / numOfPixels);
        return (brightnessSum / numOfPixels);
    }

    /*
     * Private method that takes an array of doubles
     * and returns the maximum number in it.
     */
    private double takeMax(double[] arr) {
        double max = arr[0];
        for (double number : arr) {
            if (number > max) {
                max = number;
            }
        }
        return max;
    }

    /*
     * Private method that takes an array of doubles
     * and returns the minimum number in it.
     */
    private double takeMin(double[] arr) {
        double min = arr[0];
        for (double number : arr) {
            if (number < min) {
                min = number;
            }
        }
        return min;
    }
}
