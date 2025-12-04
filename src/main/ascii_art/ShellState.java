package ascii_art;

import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.util.HashSet;
import java.util.Set;

public class ShellState {
    private static final int DEFAULT_CHARS = 10;
    private static final int DEFAULT_RESOLUTION = 2;

    private final Image image;
    private final int maxCharsinRow;
    private final int minCharsinRow;

    private SubImgCharMatcher subImgCharMatcher;
    private int charsInRow;
    private int resolution = DEFAULT_RESOLUTION;
    private boolean reverseMode;
//    private OutputMode outputMode;
    private Set<Character> charSet;

    public ShellState(Image image){
        this.image = image;
        this.maxCharsinRow = calculateMaxCharsInRow();
        this.minCharsinRow = calculateMinCharsInRow();
        charSet = new HashSet<>();
        for(int i = 0; i < DEFAULT_CHARS; i++){
            char c = (char)i;
            charSet.add(c);
        }
    }
    private int calculateMaxCharsInRow() {
        return image.getWidth();
    }
    private int calculateMinCharsInRow() {
        return Math.max(1, image.getWidth()/ image.getHeight());
    }
}
