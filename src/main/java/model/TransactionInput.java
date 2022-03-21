package model;

public class TransactionInput {
    public final String transactionOutputId;
    private TransactionOutput UTXO; // unspent transaction output

    public TransactionInput(String transactionOutputId) {
        this.transactionOutputId = transactionOutputId;
    }

    public TransactionOutput getUTXO() {
        return UTXO;
    }

    public void setUTXO(TransactionOutput UTXO) {
        this.UTXO = UTXO;
    }
}
