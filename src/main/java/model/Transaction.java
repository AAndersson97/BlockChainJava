package model;

import utility.StringUtil;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;

import static utility.StringUtil.getStringFromKeys;

public class Transaction {

    private String transactionId;
    private final PublicKey sender;
    private final PublicKey recipient;
    private final BigDecimal amount;
    private byte[] signature;

    private final TransactionInput[] inputs;
    private TransactionOutput[] outputs;

    private static int noOfGeneratedTransactions = 0;

    public Transaction(PublicKey sender, PublicKey recipient, BigDecimal amount, TransactionInput[] inputs) {
        this.sender = sender;
        this.recipient = recipient;
        this.amount = amount;
        this.inputs = inputs;
    }

    private String calculateHash() {
        noOfGeneratedTransactions++; // avoiding 2 identical transaction from having the same hash
        return StringUtil.applySha256(
                StringUtil.getStringFromKey(sender) +
                        StringUtil.getStringFromKey(recipient) +
                        amount.toString() + noOfGeneratedTransactions);
    }

    public boolean processTransaction(Chain chain) {
        if (!verifySignature()) {
            return false;
        }
        for (TransactionInput input : inputs) {
            input.setUTXO(chain.UTXOs.get(input.transactionOutputId));
        }
        BigDecimal inputValue = getInputsValue();
        if (inputValue.compareTo(chain.minimumTransaction) < 0) {
            return false;
        }
        BigDecimal amountLeft = inputValue.subtract(amount);
        transactionId = calculateHash();
        outputs = new TransactionOutput[]{new TransactionOutput(this.recipient, amount, transactionId),
                new TransactionOutput(this.sender, amountLeft, transactionId)};

        for (TransactionOutput output : outputs) {
            chain.UTXOs.put(output.id, output);
        }

        for (TransactionInput input : inputs) {
            if (input.getUTXO() != null) {
                chain.UTXOs.remove(input.getUTXO().id);
            }
        }
        return true;
    }

    public BigDecimal getInputsValue() {
        BigDecimal total = new BigDecimal(0);
        for (TransactionInput input : inputs) {
            if (input.getUTXO() != null) {
                total = total.add(input.getUTXO().amount);
            }
        }
        return total;
    }

    public BigDecimal getOutputsValue() {
        BigDecimal total = new BigDecimal(0);
        for (TransactionOutput output : outputs) {
            total = total.add(output.amount);
        }
        return total;
    }

    public void generateSignature(PrivateKey privateKey) {
        signature = StringUtil.applyECDSASig(privateKey, String.format("%s%s", getStringFromKeys(sender, recipient),
                amount.toString()));
    }

    public boolean verifySignature() {
        String data = String.format("%s%s", getStringFromKeys(sender, recipient), amount.toString());
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}
