package model;

public class TransactionInput {
    private final String transactionOutputId;
    private TransactionOutput UTO; // unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }
}
