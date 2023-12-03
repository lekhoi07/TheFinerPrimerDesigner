package indy;

public class Primer extends Sequence {
    public Primer(String sequence) {
        super(sequence);
    }

    public double goodnessScore() {
        return this.gcContentScore() + this.dimerizationScore(this) + this.hairpinScore() + this.gcClampScore();
    }

    private double gcContentScore() {
        if (Math.abs(this.getGC_Content() - 0.5) <= 0.1) {
            return 0;
        } else {
            return Math.abs(this.getGC_Content() - 0.5);
        }
    }

    public double dimerizationScore(Primer primer2) {
        String seq1 = this.getSequence();
        String seq2 = primer2.getComplement().getReverse().getSequence();

        int maxAlignmentScore = 0;
        for (int i = 0; i < seq1.length() + seq2.length() - 1; i++) {
            int alignmentScore = 0;
            for (int j = 0; j < seq1.length(); j++) {
                int comparisonPosition = seq2.length() - 1 - i + j;
                if (comparisonPosition >= 0 && comparisonPosition < seq2.length()) {
                    if (seq1.toCharArray()[j] == seq2.toCharArray()[comparisonPosition]) {
                        alignmentScore += 1;
                    }
                }
            }
            if (alignmentScore > maxAlignmentScore) {
                maxAlignmentScore = alignmentScore;
            }
        }

        return maxAlignmentScore;
    }

    private double alignmentScore(String seq1, String seq2) {
        return 0;
    }

    // TODO, not correct yet:
    private double hairpinScore() {
        String seq = this.getSequence();

        double maxHairpinScore = 0;
        for (int loopLength = 6; loopLength < this.getLength() - 2; loopLength++) {
            for (int i = 0; i < seq.length() - loopLength - 1; i++) {
                Primer subseq1 = new Primer(seq.substring(0, i));
                Primer subseq2 = new Primer(seq.substring(i + loopLength + 1));
                double currentScore = subseq1.dimerizationScore(subseq2);
                if (currentScore > maxHairpinScore) {
                    maxHairpinScore = currentScore;
                }
            }
        }

        return maxHairpinScore;
    }

    // TODO
    private double gcClampScore() {
        return Math.random();
    }

    public double compatibilityScore(Primer primer2) {
        return this.dimerizationScore(primer2) + this.meltingTempCompatibilityScore(primer2);
    }

    // TODO
    private double meltingTempCompatibilityScore(Primer primer2) {
        return Math.random();
    }
}
