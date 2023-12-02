package indy;

public class ReversePrimer extends Primer {
    private String sequence;

    public ReversePrimer(String sequence) {
        super(sequence);
        this.reverse(sequence);
    }

    private void reverse(String sequence) {
        StringBuilder reverseSequence = new StringBuilder(new Sequence(sequence).getComplement().getSequence()).reverse();
        this.sequence = reverseSequence.toString();
    }

    @Override
    public String getSequence() {
        return this.sequence;
    }
}
