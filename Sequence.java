package indy;

public class Sequence {
    public String sequence;

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
            }
        }

        return new Sequence(complementSequence.toString());
    }

    public int getLength() {
        return this.sequence.length();
    }
}
