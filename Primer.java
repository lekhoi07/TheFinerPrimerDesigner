package indy;

public class Primer extends Sequence {
    private int[] position;

    public Primer(String sequence) {
        super(sequence);
    }

    public double goodnessScore() {
        return this.gcContentScore() + this.dimerizationScore(this) + this.hairpinScore() + this.gcClampScore() + this.meltingTempScore();
    }

    private double gcContentScore() {
        if (Math.abs(this.getGC_Content() - 0.5) <= 0.1) {
            return 0;
        } else {
            return Math.abs(this.getGC_Content() - 0.5);
        }
    }

    public double dimerizationScore(Primer primer2) {
        int maxAlignmentScore = 0;
        for (int i = -1 * primer2.getLength(); i < this.getLength() - 1; i++) {
            int alignmentScore = this.alignmentScore(this, primer2, i);
            if (alignmentScore > maxAlignmentScore) {
                maxAlignmentScore = alignmentScore;
            }
        }
        return maxAlignmentScore;
    }

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

    private double hairpinScore() {
        String seq = this.getSequence();

        double maxHairpinScore = 0;
        for (int loopLength = 6; loopLength < this.getLength() - 2; loopLength++) {
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

    private double gcClampScore() {
        Sequence clamp = new Sequence(this.getSequence().substring(this.getLength() - 5));
        if (clamp.getGC_Content() == 0.4 || clamp.getGC_Content() == 0.6) {
            return 0;
        } else {
            return 15;
        }
    }

    private double meltingTempScore() {
        double a = this.getMeltingTemperature() - 52;
        double b = 58 - this.getMeltingTemperature();
        if (a >= 0 && b >= 0) {
            return 0;
        } else {
            return Math.min(Math.abs(a), Math.abs(b));
        }
    }

    public double compatibilityScore(Primer primer2) {
        return this.dimerizationScore(primer2) + this.meltingTempCompatibilityScore(primer2);
    }

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
