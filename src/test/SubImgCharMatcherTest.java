import image_char_matching.SubImgCharMatcher;
import ascii_art.RoundingMethod;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class SubImgCharMatcherTest {

    @Test
    public void testAddCharAndGetCharset() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] { 'a' });
        Set<Character> charset = matcher.getCharset();
        assertEquals(1, charset.size());
        assertTrue(charset.contains('a'));
    }

    @Test
    public void testDuplicateAddCharIsIgnored() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] { 'a' });
        matcher.addChar('a'); // should not throw or change anything
        assertEquals(1, matcher.getCharset().size());
    }

    @Test
    public void testRemoveChar() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] { 'a', 'b' });
        matcher.removeChar('a');
        assertFalse(matcher.getCharset().contains('a'));
        assertEquals(1, matcher.getCharset().size());
    }

    @Test
    public void testAddCharInvalidAsciiThrows() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] {});
        assertThrows(IllegalArgumentException.class, () -> matcher.addChar((char) 10));
    }

    @Test
    public void testRemoveCharNotInSetThrows() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] { 'x' });
        assertThrows(IllegalArgumentException.class, () -> matcher.removeChar('z'));
    }

    @Test
    public void testEmptyCharsetThrowsOnBrightnessLookup() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] {});
        assertThrows(IllegalStateException.class, () -> matcher.getCharByBrightness(0.5, RoundingMethod.ABS));
    }

    @Test
    public void testSingleBrightnessStillReturnsThatChar() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] { 'o' });
        assertEquals('o', matcher.getCharByBrightness(0.0, RoundingMethod.ABS));
        assertEquals('o', matcher.getCharByBrightness(1.0, RoundingMethod.UP));
        assertEquals('o', matcher.getCharByBrightness(0.5, RoundingMethod.DOWN));
    }

    @Test
    public void testRoundingMethodBehaviorDoesNotCrash() {
        SubImgCharMatcher matcher = new SubImgCharMatcher(new char[] { '.', ':', '#' });
        char abs = matcher.getCharByBrightness(0.5, RoundingMethod.ABS);
        char up = matcher.getCharByBrightness(0.5, RoundingMethod.UP);
        char down = matcher.getCharByBrightness(0.5, RoundingMethod.DOWN);

        assertTrue(matcher.getCharset().contains(abs));
        assertTrue(matcher.getCharset().contains(up));
        assertTrue(matcher.getCharset().contains(down));
    }


}
