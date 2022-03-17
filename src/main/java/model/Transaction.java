package model;

import utility.StringUtil;

import java.math.BigDecimal;
import java.security.PrivateKey;
import java.security.PublicKey;

import static utility.StringUtil.getStringFromKeys;

public class Transaction {

    private final String transactionId;
    private final PublicKey sender;
    private final PublicKey recipient;
    private final BigDecimal amount;
    private byte[] signature;

    private final TransactionInput[] inputs;
    private final TransactionOutput[] outputs;

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

    public void generateSignature(PrivateKey privateKey) {
        signature = StringUtil.applyECDSASig(privateKey, String.format("%s%s", getStringFromKeys(sender, recipient),
                amount.toString()));
    }

    public boolean verifySignature() {
        String data = String.format("%s%s", getStringFromKeys(sender, recipient), amount.toString());
        return StringUtil.verifyECDSASig(sender, data, signature);
    }
}
