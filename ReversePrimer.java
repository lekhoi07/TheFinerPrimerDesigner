package indy;

/**
 * This class represents a reverse primer. Since it has much of the same functionalities as a forward primer, it
 * inherits from Primer. However, reverse primers are different from forward ones, so some methods need to be
 * overriden.
 */
public class ReversePrimer extends Primer {
    private String sequence;

    /**
     * This constructor calls upon the constructor of Primer and initializes the sequence that this class represents.
     * @param sequence
     */
    public ReversePrimer(String sequence) {
        super(sequence);
        this.sequence = sequence;
    }

    /**
     * This override is necessary because reverse primers have an orientation reversed compared to forward primers.
     * @return
     */
    @Override
    public String getSequence() {
        return this.reverse(this.sequence);
    }

    /**
     * This override is necessary becayse reverse primers have opposite orientation compared to forward primers.
     * @return
     */
    @Override
    public ReversePrimer getComplement() {
        return new ReversePrimer(this.reverse(super.getComplement().getSequence()));
    }

    /**
     * This helper method takes in a String and returns the reverse of it.
     * @param sequence
     * @return
     */
    private String reverse(String sequence) {
        StringBuilder reverseSequence = new StringBuilder(new Sequence(sequence).getComplement().getSequence()).reverse();
        return reverseSequence.toString();
    }
}
