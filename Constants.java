package indy;

/**
 * This class contains useful constants for the program to use.
 */
public class Constants {
    public static final int APP_SIZE = 700;

    // Constants for primer designing.
    public static final int MIN_PRIMER_LENGTH = 18;
    public static final int MAX_PRIMER_LENGTH = 24;
    public static final int MIN_HAIRPIN_LOOP_LENGTH = 6;
    public static final int PENALTY_OF_MISSING_GC_CLAMP = 15;
    public static final int MIN_MELT_TEMP = 52;
    public static final int MAX_MELT_TEMP = 58;

    // Constants for DNA template sequence displaying.
    public static final int MAX_NUCLEOTIDES_PER_ROW = 42;
    public static final int SPACING_BETWEEN_ROWS = 110;

    // Constants for primer pair results displaying.
    public static final int SPACING_BETWEEN_RESULTS = 170;
}
