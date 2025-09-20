import ascii_art.AsciiArtAlgorithm;
import ascii_art.RoundingMethod;
import ascii_output.AsciiOutput;
import image.Image;
import image_char_matching.SubImgCharMatcher;

import java.io.IOException;

public class DemoMain {
    public static void main(String[] args) {
        try {
            // 1. Load image from file
            AsciiArtAlgorithm algo = getAsciiArtAlgorithm();
            algo.setRoundingMethod(RoundingMethod.ABS);

            // 4. Run the algorithm
            char[][] ascii = algo.run();

            AsciiOutput output = new ascii_output.HtmlAsciiOutput("output.html", "Courier New");
            output.out(ascii);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static AsciiArtAlgorithm getAsciiArtAlgorithm() throws IOException {
        String filename = "cat.jpeg"; // <-- put your test file here
        Image img = new Image(filename);

        // 2. Initialize charset matcher with a basic set
        char[] fullCharset = new char[95];
        for (int i = 32; i <= 126; i++) {
            fullCharset[i - 32] = (char) i;
        }

        SubImgCharMatcher matcher = new SubImgCharMatcher(fullCharset);

        // 3. Create the algorithm with resolution (must be power of 2 ≤ image width)
        int resolution = 512; // adjust depending on your image size
        AsciiArtAlgorithm algo = new AsciiArtAlgorithm(img, matcher, resolution);
        return algo;
    }
}
