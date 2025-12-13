package image;

import java.awt.Color;
import java.util.function.Function;

/**
 * Utility class for image processing operations such as padding and brightness calculation.
 * Methods are static and do not modify the original image.
 * @author aronisaacs
 */
public class ImageProcessor {

    /**
     *     Default brightness function (luminance formula)
     */
    public static final Function<Color, Double> DEFAULT_BRIGHTNESS = c ->
            0.2126 * c.getRed() + 0.7152 * c.getGreen() + 0.0722 * c.getBlue();

    private ImageProcessor() {
        // prevent instantiation
    }

    /**
     * Pads the given image with white pixels until width and height are powers of two.
     * Padding is added to the right and bottom sides only.
     *
     * @param img the original image
     * @return a new Image whose dimensions are powers of two
     */
//    public static Image padToPowerOfTwo(Image img) {
//        int width = img.getWidth();
//        int height = img.getHeight();
//
//        int newWidth = nextPowerOfTwo(width);
//        int newHeight = nextPowerOfTwo(height);
//
//		int offsetX = (newWidth - width) / 2;
//		int offsetY = (newHeight - height) / 2;
//
//
//		// If already power of two, return original
//        if (newWidth == width && newHeight == height) {
//            return img;
//        }
//
//        Color[][] newPixels = new Color[newHeight][newWidth];
//
//        // Fill original pixels
//        for (int y = 0; y < height; y++) {
//            for (int x = 0; x < width; x++) {
//                newPixels[y][x] = img.getPixel(y, x);
//            }
//        }
//
//        // Fill padding with white
//        Color white = new Color(255, 255, 255);
//        for (int y = 0; y < newHeight; y++) {
//            for (int x = 0; x < newWidth; x++) {
//                if (y >= height || x >= width) {
//                    newPixels[y][x] = white;
//                }
//            }
//        }
//
//        return new Image(newPixels, newWidth, newHeight);
//    }

	    public static Image padToPowerOfTwo(Image img) {
        int width = img.getWidth();
        int height = img.getHeight();

        int newWidth = nextPowerOfTwo(width);
        int newHeight = nextPowerOfTwo(height);

		int offsetX = (newWidth - width) / 2;
		int offsetY = (newHeight - height) / 2;


		// If already power of two, return original
        if (newWidth == width && newHeight == height) {
            return img;
        }

        Color[][] newPixels = new Color[newHeight][newWidth];



        // Fill padding with white
        Color white = new Color(255, 255, 255);
        for (int y = 0; y < newHeight; y++) {
            for (int x = 0; x < newWidth; x++) {
				if (y < offsetY || y >= offsetY + height ||
						x < offsetX || x >= offsetX + width) {
					// Outside original-image region → padding
					newPixels[y][x] = white;
				} else {
					// Inside original-image region → copy shifted pixel
					newPixels[y][x] = img.getPixel(y - offsetY, x - offsetX);
				}
            }
        }

        return new Image(newPixels, newWidth, newHeight);
    }

    /**
     * Computes the average brightness of a rectangular region of the image.
     * Brightness is calculated using the given function, normalized to [0,1].
     *
     * @param img                the source image
     * @param startY             top-left row index
     * @param startX             top-left column index
     * @param tileDimension          width of the region
     * @param brightnessFunction function mapping a Color to a brightness value in [0,255]
     * @return average brightness in [0,1]
     */
    public static double computeBrightness(
            Image img,
            int startY,
            int startX,
            int tileDimension,
            Function<Color, Double> brightnessFunction) {

        double sum = 0.0;

        for (int y = startY; y < startY + tileDimension; y++) {
            for (int x = startX; x < startX + tileDimension; x++) {
                Color pixel = img.getPixel(y, x);
                sum += brightnessFunction.apply(pixel);
            }
        }

        return sum / (tileDimension * tileDimension * 255.0);
    }



    // Helper to compute next power of two ≥ n
    private static int nextPowerOfTwo(int n) {
        if (n <= 0) return 1;
        int p = 1;
        while (p < n) {
            p <<= 1;
        }
        return p;
    }
}
