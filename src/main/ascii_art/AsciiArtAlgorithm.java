package ascii_art;

import image.Image;
import image.ImageProcessor;
import image_char_matching.SubImgCharMatcher;

/**
 * The AsciiArtAlgorithm class coordinates the process of converting an image
 * into ASCII art using a given charset and resolution.
 *
 * It separates image preprocessing (computing brightness grid) from character
 * mapping, so that brightness is only recalculated when resolution changes.
 *
 * Charset changes are handled externally via the SubImgCharMatcher object.
 */
public class AsciiArtAlgorithm {

    private final Image image;                 // the padded image (fixed after construction)
    private final SubImgCharMatcher matcher;   // character matcher (mutable externally)
    private int resolution;                    // number of characters per row
    private RoundingMethod rounding;           // rounding mode

    // Cached brightness grid (recomputed only when resolution changes)
    private double[][] brightnessGrid;

    /**
     * Constructs an AsciiArtAlgorithm with the given image, matcher, and resolution.
     * The image is padded to power-of-two dimensions during construction.
     *
     * @param image original image
     * @param matcher character matcher
     * @param resolution number of characters per row (must be a power of two)
     * @throws IllegalArgumentException if resolution is invalid
     */
    public AsciiArtAlgorithm(Image image, SubImgCharMatcher matcher, int resolution) {
        this.image = ImageProcessor.padToPowerOfTwo(image);
        this.matcher = matcher;
        this.rounding = RoundingMethod.ABS; // sensible default
        setResolution(resolution); // validates and sets
        this.brightnessGrid = null;
    }

    /**
     * Sets the resolution. Must be a power of two between 1 and the image width.
     * Brightness grid will be recomputed on next run().
     *
     * @param resolution number of characters per row
     * @throws IllegalArgumentException if resolution is invalid
     */
    public void setResolution(int resolution) {
        if (resolution <= 0) {
            throw new IllegalArgumentException("Resolution must be positive");
        }
        if ((resolution & (resolution - 1)) != 0) { // power-of-two check
            throw new IllegalArgumentException("Resolution must be a power of two");
        }
        if (resolution > image.getWidth()) {
            throw new IllegalArgumentException("Resolution cannot exceed image width");
        }
        this.resolution = resolution;
        this.brightnessGrid = null; // invalidate cache
    }

    /**
     * Returns the current resolution.
     */
    public int getResolution() {
        return resolution;
    }

    /**
     * Sets the rounding method for mapping brightness to characters.
     *
     * @param method rounding method (must not be null)
     * @throws IllegalArgumentException if method is null
     */
    public void setRoundingMethod(RoundingMethod method) {
        if (method == null) {
            throw new IllegalArgumentException("Rounding method must not be null");
        }
        this.rounding = method;
    }

    /**
     * Returns the current rounding method.
     */
    public RoundingMethod getRoundingMethod() {
        return rounding;
    }

    /**
     * Runs the algorithm using the current resolution, charset, and rounding method.
     *
     * @return a 2D char array representing the ASCII art
     * @throws IllegalStateException if charset is too small
     */
    public char[][] run() {
        if (matcher.getCharset().size() < 2) {
            throw new IllegalStateException("Charset is too small");
        }

        // Compute brightness if cache invalid
        if (brightnessGrid == null) {
            brightnessGrid = computeBrightnessGrid();
        }

        // Map brightness to characters
        char[][] ascii = new char[brightnessGrid.length][brightnessGrid[0].length];
        for (int row = 0; row < brightnessGrid.length; row++) {
            for (int col = 0; col < brightnessGrid[row].length; col++) {
                ascii[row][col] = matcher.getCharByBrightness(
                        brightnessGrid[row][col], rounding);
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
