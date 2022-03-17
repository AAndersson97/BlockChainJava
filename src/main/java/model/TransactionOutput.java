package model;

import utility.StringUtil;

import java.math.BigDecimal;
import java.security.PublicKey;

public class TransactionOutput {
    private final String id;
    private final PublicKey recipient; // owner of coins
    private final BigDecimal amount;
    private final String parentTransactionId; // id of transaction this output was created in

    public TransactionOutput(PublicKey recipient, BigDecimal amount, String parentTransactionId) {
        this.id = StringUtil.applySha256(String.format("%s%s%s", StringUtil.getStringFromKey(recipient),
                amount.toString(), parentTransactionId);
        this.recipient = recipient;
        this.amount = amount;
        this.parentTransactionId = parentTransactionId;
    }

    public boolean belongsTo(PublicKey publicKey) {
        return publicKey == recipient;
    }
}
