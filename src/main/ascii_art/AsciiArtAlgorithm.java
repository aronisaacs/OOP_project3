package ascii_art;

import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class coordinates the process of converting an image
 * into ASCII art using a given charset and resolution.
 * It separates image preprocessing (computing brightness grid) from character
 * mapping, so that brightness is only recalculated when resolution changes.
 * Charset changes are handled externally via the SubImgCharMatcher object.
 * Additionally, the class can be fed a cached brightness grid to avoid recomputation. and can push
 * updates via a callback.
 * @author aronisaacs
 */
public class AsciiArtAlgorithm {

    private final Image image;                 // the padded image (fixed after construction)
    private final SubImgCharMatcher matcher;   // character matcher (mutable externally)
    private int resolution;                    // number of characters per row
    private final boolean reverseBrightness; //false by default, true if brightness must be reversed

    // Cached brightness grid (recomputed only when resolution changes)
    private double[][] brightnessGrid;
    private final java.util.function.Consumer<double[][]> cacheCallback;

    /**
     * Constructs an AsciiArtAlgorithm with the given image, matcher, and resolution.
     * The image is padded to power-of-two dimensions during construction.
     *
     * @param image original image
     * @param matcher character matcher
     * @param resolution number of characters per row (must be a power of two)
     * @throws IllegalArgumentException if resolution is invalid
     */
    public AsciiArtAlgorithm(Image image,
                             SubImgCharMatcher matcher,
                             int resolution,
                             boolean reverseBrightness,
                             double[][] initialBrightnessGrid,
                             java.util.function.Consumer<double[][]> cacheCallback) {
        this.image = ImageProcessor.padToPowerOfTwo(image);
        this.matcher = matcher;
        setResolution(resolution); // validates and sets
        this.brightnessGrid = initialBrightnessGrid;
        this.reverseBrightness = reverseBrightness;
        this.cacheCallback = cacheCallback;
    }

    /**
     * Sets the resolution. Must be a positive power of two validated in the resCommand in Shell.
     * @param resolution number of characters per row
     */
    private void setResolution(int resolution) {
        this.resolution = resolution;
    }


    /**
     * Runs the algorithm using the current resolution, charset, and brightness grid.
     * @return a 2D char array representing the ASCII art
     * @throws IllegalStateException if charset is too small
     */
    public char[][] run() {
        if (matcher.getCharset().size() < 2) {
            throw new IllegalStateException("Did not execute. Charset is too small.");
        }

        // Recompute brightness grid if needed
        if (brightnessGrid == null) {
            brightnessGrid = computeBrightnessGrid();
            //add the push callback here later
            if (cacheCallback != null) {
                cacheCallback.accept(brightnessGrid);
            }
        }

        // Determine brightness source (normal or reversed)
        double[][] source = brightnessGrid;

        if (reverseBrightness) {
            double[][] reversed = new double[source.length][source[0].length];
            for (int r = 0; r < source.length; r++) {
                for (int c = 0; c < source[r].length; c++) {
                    reversed[r][c] = 1.0 - source[r][c];
                }
            }
            source = reversed;
        }

        // Allocate output ASCII matrix
        char[][] ascii = new char[source.length][source[0].length];

        // Map brightness to characters — clean, no branching inside loop
        for (int row = 0; row < source.length; row++) {
            for (int col = 0; col < source[row].length; col++) {
                ascii[row][col] = matcher.getCharByImageBrightness(source[row][col]);
            }
        }

        return ascii;
    }

    // Helper: recomputes brightness grid based on current resolution
    private double[][] computeBrightnessGrid() {
        int tilesPerRow = resolution;
        int tileDimension = image.getWidth() / tilesPerRow;
        // square tiles

        int rows = image.getHeight() / tileDimension;
        double[][] grid = new double[rows][tilesPerRow];

        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < tilesPerRow; col++) {
                int startX = col * tileDimension;
                int startY = row * tileDimension;
                grid[row][col] = ImageProcessor.computeBrightness(
                        image, startY, startX, tileDimension,
                        ImageProcessor.DEFAULT_BRIGHTNESS);
            }
        }
        return grid;
    }
}
