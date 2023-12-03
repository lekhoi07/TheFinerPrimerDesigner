package indy;

public class ReversePrimer extends Primer {
    private String sequence;

    public ReversePrimer(String sequence) {
        super(sequence);
        this.sequence = sequence;
    }

    @Override
    public String getSequence() {
        return this.reverse(this.sequence);
    }

    @Override
    public ReversePrimer getComplement() {
        return new ReversePrimer(this.reverse(super.getComplement().getSequence()));
    }

    private String reverse(String sequence) {
        StringBuilder reverseSequence = new StringBuilder(new Sequence(sequence).getComplement().getSequence()).reverse();
        return reverseSequence.toString();
    }
}
