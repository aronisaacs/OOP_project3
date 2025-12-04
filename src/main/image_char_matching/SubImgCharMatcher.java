package image_char_matching;

import java.util.*;
import java.util.function.Function;

/**
 * SubImgCharMatcher maps sub-image brightness values to the best-matching ASCII characters
 * from a user-defined character set, based on a specified rounding method.
 * <p>
 * It supports adding and removing characters from the set, and automatically updates
 * brightness normalization functions accordingly.
 */
public class SubImgCharMatcher {

    // Maps raw brightness (number of white pixels) to characters with that brightness
    private final TreeMap<Integer, TreeSet<Character>> rawBrightnessMap = new TreeMap<>();

    // Maps characters to their raw brightness value for fast removal
    private final Map<Character, Integer> charToRawBrightness = new HashMap<>();

    // Function to convert normalized [0,1] brightness into raw brightness [0,256]
    private Function<Double, Integer> normalizedToRaw;

    /**
     * Constructs a matcher with the given initial character set.
     * All characters must be printable ASCII characters (32 to 126).
     *
     * @param charset array of characters to initialize the matcher with
     * @throws IllegalArgumentException if any character is not printable ASCII
     * @throws NullPointerException if initialCharset is null
     */
    public SubImgCharMatcher(char[] charset) {
        Objects.requireNonNull(charset, "Initial charset must not be null");
        for (char c : charset) {
            addChar(c);
        }
        updateNormalizationFunction();
    }

    /**
     * Adds a character to the matcher if not already present.
     * Automatically updates the brightness normalization function.
     *
     * @param c character to add
     * @throws IllegalArgumentException if character is not in printable ASCII range
     * @throws NullPointerException if c is null (autoboxed Character used incorrectly)
     */
    public void addChar(char c) {
        // No null check needed for primitive 'char', but kept for consistency if autoboxed
        if (c < 32 || c > 126) {
            throw new IllegalArgumentException("Character must be printable ASCII (32–126): '" + c + "'");
        }
        if (charToRawBrightness.containsKey(c)) return;

        // Count number of white pixels (false values)
        boolean[][] matrix = CharConverter.convertToBoolArray(c);
        int whitePixels = 0;
        for (boolean[] row : matrix) {
            for (boolean pixel : row) {
                if (pixel) whitePixels++;
            }
        }

        // Add to maps
        charToRawBrightness.put(c, whitePixels);
        rawBrightnessMap.putIfAbsent(whitePixels, new TreeSet<>());
        rawBrightnessMap.get(whitePixels).add(c);

        updateNormalizationFunction();
    }

    /**
     * Removes a character from the matcher.
     * Updates normalization if min/max brightness changes.
     *
     * @param c character to remove
     * @throws IllegalArgumentException if the character is not in the charset
     * @throws NullPointerException if c is null (autoboxed Character used incorrectly)
     */
    public void removeChar(char c) {
        if (!charToRawBrightness.containsKey(c)) {
            throw new IllegalArgumentException("Character not in charset: '" + c + "'");
        }

        int brightness = charToRawBrightness.get(c);
        TreeSet<Character> bucket = rawBrightnessMap.get(brightness);

        // Remove character from brightness bucket
        if (bucket != null) {
            bucket.remove(c);
            if (bucket.isEmpty()) {
                rawBrightnessMap.remove(brightness);
            }
        }

        charToRawBrightness.remove(c);
        updateNormalizationFunction();
    }

    /**
     * Returns the character whose brightness best matches the given normalized brightness value,
     * according to the specified rounding method.
     *
     * @param brightness brightness in [0,1]
     * @return closest matching character
     * @throws IllegalStateException if charset is empty
     * @throws IllegalArgumentException if rounding method is unrecognized or null
     * @throws NullPointerException if method is null
     */
    public char getCharByImageBrightness(double brightness) {
        if (charToRawBrightness.isEmpty()) {
            throw new IllegalStateException("Charset is empty");
        }

        int rawQuery = normalizedToRaw.apply(brightness);

        Integer floor = rawBrightnessMap.floorKey(rawQuery);
        Integer ceil = rawBrightnessMap.ceilingKey(rawQuery);
        if (floor == null) return rawBrightnessMap.get(ceil).first();
        if (ceil == null) return rawBrightnessMap.get(floor).first();
        int dFloor = Math.abs(floor - rawQuery);
        int dCeil = Math.abs(ceil - rawQuery);
        return (dFloor <= dCeil) ? rawBrightnessMap.get(floor).first() : rawBrightnessMap.get(ceil).first();
    }

    /**
     * Returns a copy of the current charset.
     *
     * @return set of characters currently in the matcher
     */
    //todo this is added API!! we need to comment about it.
    public Set<Character> getCharset() {
        return new HashSet<>(charToRawBrightness.keySet());
    }

    /**
     * Recomputes the normalization function from [0,1] to raw brightness values.
     * Called automatically after every add/remove.
     */
    private void updateNormalizationFunction() {
        if (rawBrightnessMap.isEmpty()) {
            normalizedToRaw = b -> {
                throw new IllegalStateException("Cannot normalize: charset is empty");
            };
        } else {
            int min = rawBrightnessMap.firstKey();
            int max = rawBrightnessMap.lastKey();
            if (min == max) {
                normalizedToRaw = b -> min;
            } else {
                normalizedToRaw = b -> (int) Math.round(min + b * (max - min));
            }
        }
    }
}
