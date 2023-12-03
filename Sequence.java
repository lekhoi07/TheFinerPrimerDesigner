package indy;

public class Sequence {
    private String sequence;

    public Sequence(String sequence) {
        this.sequence = sequence;
    }

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

    public double getGC_Content() {
        double gcCount = 0;
        for (char c : this.sequence.toCharArray()) {
            if (c == 'g' || c == 'c') {
                gcCount += 1;
            }
        }

        return gcCount / this.sequence.length();
    }
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
