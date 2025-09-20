package ascii_art;

/**
 * Defines rounding modes for matching brightness to characters.
 */
public enum RoundingMethod {
    ABS,   // Closest brightness
    UP,    // Ceiling (smallest brightness ≥ query)
    DOWN   // Floor (largest brightness ≤ query)
}
