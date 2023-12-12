package indy;

/**
 * This class represents a DNA sequence. It is a wrapper class for a String that represents the sequence of nucleotides.
 * This class is useful because the program can query multiple different properties of the sequence.
 */
public class Sequence {
    private String sequence;

    /**
     * This constructor initializes the sequence with a String that represents the sequence.
     * @param sequence
     */
    public Sequence(String sequence) {
        this.sequence = sequence;
    }

    /**
     * This method returns the complementary sequence of the sequence.
     * @return
     */
    public Sequence getComplement() {
        StringBuilder complementSequence = new StringBuilder();
        for (Character nucleotide : this.sequence.toCharArray()) {
            switch (nucleotide) {
                case 'a':
                    complementSequence.append('t');
                    break;
                case 't':
                    complementSequence.append('a');
                    break;
                case 'c':
                    complementSequence.append('g');
                    break;
                case 'g':
                    complementSequence.append('c');
                    break;
                case '5':
                    complementSequence.append('3');
                    break;
                case '3':
                    complementSequence.append('5');
                    break;
            }
        }

        return new Sequence(complementSequence.toString());
    }

    public int getLength() {
        return this.sequence.length();
    }

    public String getSequence() {
        return this.sequence;
    }

    public Sequence getReverse() {
        StringBuilder reverseSequence = new StringBuilder(new Sequence(this.sequence).getSequence()).reverse();
        return new Sequence(reverseSequence.toString());
    }

    /**
     * This method returns the percentage of nucleotides in the sequence that are either guanine or cytosine.
     * @return
     */
    public double getGC_Content() {
        double gcCount = 0;
        for (char c : this.sequence.toCharArray()) {
            if (c == 'g' || c == 'c') {
                gcCount += 1;
            }
        }

        return gcCount / this.sequence.length();
    }

    /**
     * This method uses the formula 4 * (C + G) + 2 * (A + T) to calculate the melting temperature of the sequence.
     * @return
     */
    public double getMeltingTemperature() {
        int aCount = 0;
        int tCount = 0;
        int cCount = 0;
        int gCount = 0;

        for (char c : this.sequence.toCharArray()) {
            switch (c) {
                case 'a':
                    aCount += 1;
                    break;
                case 't':
                    tCount += 1;
                    break;
                case 'g':
                    gCount += 1;
                    break;
                case 'c':
                    cCount += 1;
                    break;
            }
        }

        return 4 * (gCount + cCount) + 2 * (aCount + tCount);
    }
}
