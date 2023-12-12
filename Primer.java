package indy;

/**
 * This is the class that represents a DNA primer, specifically a forward primer. It is a DNA Sequence, being made of DNA.
 * Therefore, it inherits from Sequence. It also is a wrapper class for a String that represents the sequence of the primer.
 */
public class Primer extends Sequence {
    private int[] position;

    /**
     * This constructor calls upon the constructor of Sequence to set the sequence that the primer represents.
     * @param sequence
     */
    public Primer(String sequence) {
        super(sequence);
    }

    /**
     * This method calculates the goodness score of the primer that takes into account the GC-content, most stable
     * homodimer, most stable hairpin structure, whether it has a GC clamp, and its melting temperature. A lower
     * score is better.
     * @return
     */
    public double goodnessScore() {
        return this.gcContentScore() + this.dimerizationScore(this) + this.hairpinScore() + this.gcClampScore() + this.meltingTempScore();
    }

    /**
     * This helper method calculates how good the GC content of the primer is. If it is in the ideal range, then 0 is
     * returned. Otherwise, the deviation from 50% is returned.
     * @return
     */
    private double gcContentScore() {
        if (Math.abs(this.getGC_Content() - 0.5) <= 0.1) {
            return 0;
        } else {
            return Math.abs(this.getGC_Content() - 0.5);
        }
    }

    /**
     * This helper method takes every possible alignment of a potential homodimer and returns the maximum number
     * of nucleotide pairings that are complementary. In other words, it calculates the stability of the most
     * stable homodimer.
     * @param primer2
     * @return
     */
    public double dimerizationScore(Primer primer2) {
        int maxAlignmentScore = 0;
        for (int offset = -1 * primer2.getLength(); offset < this.getLength() - 1; offset++) {
            int alignmentScore = this.alignmentScore(this, primer2, offset);
            if (alignmentScore > maxAlignmentScore) {
                maxAlignmentScore = alignmentScore;
            }
        }
        return maxAlignmentScore;
    }

    /**
     * This helper method takes a configuration of two sequences, matches them up nucleotide-to-nucleotide,
     * then returns how many of those match-ups are complementary. The parameter "offset" is the index of the
     * nucleotide in seq2 that the start of seq1 is matched to.
     * @param seq1
     * @param seq2
     * @param offset
     * @return
     */
    private int alignmentScore(Sequence seq1, Sequence seq2, int offset) {
        int score = 0;
        for (int j = 0; j < seq1.getLength(); j++) {
            int comparisonPosition = offset + j;
            if (comparisonPosition >= 0 && comparisonPosition < seq2.getLength()) {
                if (seq1.getSequence().toCharArray()[j] == seq2.getComplement().getReverse().getSequence().toCharArray()[comparisonPosition]) {
                    score += 1;
                }
            }
        }
        return score;
    }

    /**
     * This method generates all the possible ways the primer could loop in on itself, and returns the number of
     * complementary base pairs from the most stable configuration.
     * @return
     */
    private double hairpinScore() {
        String seq = this.getSequence();

        double maxHairpinScore = 0;
        for (int loopLength = Constants.MIN_HAIRPIN_LOOP_LENGTH; loopLength < this.getLength() - 2; loopLength++) {
            for (int i = 1; i < seq.length() - loopLength; i++) {
                String subseq1 = seq.substring(0, i);
                String subseq2 = seq.substring(i + loopLength);
                int k = Math.min(subseq1.length(), subseq2.length());
                subseq1 = subseq1.substring(subseq1.length() - k);
                subseq2 = subseq2.substring(subseq2.length() - k);
                double currentScore = this.alignmentScore(new Sequence(subseq1), new Sequence(subseq2), 0);
                if (currentScore > maxHairpinScore) {
                    maxHairpinScore = currentScore;
                }
            }
        }

        return maxHairpinScore;
    }

    /**
     * This helper method returns zero if there is a GC clamp in the primer. This means that out of the last 5
     * nucleotides of the primer, 2 or 3 of them are guanine or cytosine. Otherwise, it returns a penalty score.
     * @return
     */
    private double gcClampScore() {
        Sequence clamp = new Sequence(this.getSequence().substring(this.getLength() - 5));
        if (clamp.getGC_Content() == 0.4 || clamp.getGC_Content() == 0.6) {
            return 0;
        } else {
            return Constants.PENALTY_OF_MISSING_GC_CLAMP;
        }
    }

    /**
     * This method returns 0 if the primer's melting temperature is within the ideal range, and the deviation from the
     * ideal range otherwise as a penalty.
     * @return
     */
    private double meltingTempScore() {
        double a = this.getMeltingTemperature() - Constants.MIN_MELT_TEMP;
        double b = Constants.MAX_MELT_TEMP - this.getMeltingTemperature();
        if (a >= 0 && b >= 0) {
            return 0;
        } else {
            return Math.min(Math.abs(a), Math.abs(b));
        }
    }

    /**
     * This method calculates the compatibility of a primer with other primer called primer2, taking into account
     * the likelihood of heterodimerization and whether their melting temperatures are compatible.
     * @param primer2
     * @return
     */
    public double compatibilityScore(Primer primer2) {
        return this.dimerizationScore(primer2) + this.meltingTempCompatibilityScore(primer2);
    }

    /**
     * This helper method returns the difference between a primer's melting temperature with that of another primer's.
     * This is because melting temperatures around the same value are ideal for PCR primers.
     * @param primer2
     * @return
     */
    private double meltingTempCompatibilityScore(Primer primer2) {
        return Math.abs(this.getMeltingTemperature() - primer2.getMeltingTemperature());
    }

    public void setPosition(int[] position) {
        this.position = position;
    }

    public int[] getPosition() {
        return this.position;
    }
}
