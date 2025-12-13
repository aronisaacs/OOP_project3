package ascii_art;

import image.Image;
import image_char_matching.SubImgCharMatcher;

/**
 * The ShellState class represents the state of the shell for ASCII art generation.
 * It holds the image to be processed, character matching settings, resolution,
 * output mode, reverse mode, and cached brightness grid for optimization.
 *
 * @author ron.stein
 */
public class ShellState {
	//Default settings
	private static final int DEFAULT_RESOLUTION = 2;
	private static final char[] DEFAULT_CHARSET = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
	private static final OutputMode DEFAULT_OUTPUT_MODE = OutputMode.CONSOLE;

	private final Image image;
	private final int maxCharsInRow;
	private final int minCharsInRow;
	private final SubImgCharMatcher subImgCharMatcher = new SubImgCharMatcher(DEFAULT_CHARSET);
	private int resolution = DEFAULT_RESOLUTION;
	private boolean reverseMode = false; //true if reverse mode is on, false otherwise and by default
	private OutputMode outputMode = DEFAULT_OUTPUT_MODE;
	private double[][] cachedBrightnessGrid = null;

	/**
	 * Constructs a ShellState with the given image and default settings.
	 *
	 * @param image the image to be processed
	 */
	public ShellState(Image image) {
		this.image = image;
		maxCharsInRow = calculateMaxCharsInRow();
		minCharsInRow = calculateMinCharsInRow();
	}

	private int calculateMaxCharsInRow() {
		return image.getWidth();
	}

	private int calculateMinCharsInRow() {
		return Math.max(1, image.getWidth() / image.getHeight());
	}

	/**
	 * Gets the max chars in row.
	 *
	 * @return the max chars in row
	 */
	public int getMaxCharsInRow() {
		return maxCharsInRow;
	}

	/**
	 * Gets the min chars in row.
	 *
	 * @return the min chars in row
	 */
	public int getMinCharsInRow() {
		return minCharsInRow;
	}

	/**
	 * Gets the SubImgCharMatcher.
	 *
	 * @return the SubImgCharMatcher
	 */
	public SubImgCharMatcher getSubImgCharMatcher() {
		return subImgCharMatcher;
	}

	/**
	 * Gets the resolution
	 *
	 * @return the resolution
	 */
	public int getResolution() {
		return resolution;
	}

	/**
	 * Sets the resolution
	 *
	 * @param resolution the resolution to set
	 */
	public void setResolution(int resolution) {

		this.resolution = resolution;
		this.cachedBrightnessGrid = null; // invalidate cache
	}

	/**
	 * gets the output mode
	 *
	 * @return the output mode (enum OutputMode) console or HTML
	 */
	public OutputMode getOutputMode() {
		return outputMode;
	}

	/**
	 * sets the output mode
	 *
	 * @param outputMode the output mode to set (enum OutputMode) console or HTML
	 */
	public void setOutputMode(OutputMode outputMode) {
		this.outputMode = outputMode;
	}


	/**
	 * sets the reverse mode
	 *
	 * @param reverseMode true to set reverse mode on, false to set it off
	 */
	public void setReverseMode(boolean reverseMode) {
		this.reverseMode = reverseMode;
	}

	/**
	 * gets the reverse mode
	 *
	 * @return the reverse mode
	 */
	public boolean getReverseMode() {
		return reverseMode;
	}

	/**
	 * gets the image
	 *
	 * @return the image
	 */
	public Image getImg() {
		return image;
	}

	/**
	 * gets the cached brightness grid
	 *
	 * @return the cached brightness grid
	 */
	public double[][] getCachedBrightnessGrid() {
		return cachedBrightnessGrid;
	}

	/**
	 * sets the cached brightness grid
	 *
	 * @param cachedBrightnessGrid the cached brightness grid to set
	 */
	public void setCachedBrightnessGrid(double[][] cachedBrightnessGrid) {
		this.cachedBrightnessGrid = cachedBrightnessGrid;
	}


}
