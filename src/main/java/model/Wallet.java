package model;

import java.math.BigDecimal;
import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    private final PrivateKey privateKey;
    private final PublicKey publicKey;

    public Wallet() {
        KeyPair keyPair = generateKeyPair();
        privateKey = keyPair.getPrivate();
        publicKey = keyPair.getPublic();
    }

    public BigDecimal getBalance(Chain chain) {
        BigDecimal total = new BigDecimal(0);
        for (var item : chain.UTXOs.entrySet()) {
            TransactionOutput UTXO = item.getValue();
            if (UTXO.belongsTo(publicKey)) {
                chain.UTXOs.put(UTXO.id, UTXO);
                total = total.add(UTXO.amount);
            }
        }
        return total;
    }

    public Transaction sendFunds(Chain chain, PublicKey recipient, BigDecimal amount) {
        BigDecimal total = new BigDecimal(0);
        if(getBalance(chain).compareTo(amount) < 0) {
            return null;
        }
        TransactionInput[] inputs = new TransactionInput[chain.UTXOs.size()];

        int index = 0;
        for (var item : chain.UTXOs.entrySet()) {
            total = total.add(item.getValue().amount);
            inputs[index++] = new TransactionInput(item.getValue().id);
            if (total.compareTo(amount) > 0) {
                break;
            }
        }

        Transaction newTransaction = new Transaction(publicKey, recipient, amount, inputs);
        newTransaction.generateSignature(privateKey);

        for (TransactionInput input : inputs) {
            chain.UTXOs.remove(input.transactionOutputId);
        }
        return newTransaction;
    }

    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyGenerator = KeyPairGenerator.getInstance("ECDSA","BC");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            ECGenParameterSpec ecSpec = new ECGenParameterSpec("prime192v1");
            // Initialize the key generator and generate a KeyPair
            keyGenerator.initialize(ecSpec, random);
            return keyGenerator.generateKeyPair();
        } catch (Exception ignored) {
        }
        throw new RuntimeException("Failed to generate keys");
    }

    public PrivateKey getPrivateKey() {
        return privateKey;
    }

    public PublicKey getPublicKey() {
        return publicKey;
    }
}
