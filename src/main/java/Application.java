import model.Chain;
import model.Transaction;
import model.Wallet;
import utility.StringUtil;

import java.math.BigDecimal;

public class Application {
    private final static Wallet wallet1 = new Wallet();
    private final static Wallet wallet2 = new Wallet();

    public static void main(String[] args) {
        Chain chain = new Chain();
        chain.addData("This is the second block");
        chain.addData("This is the third block");
        System.out.println("Private and public keys:");
        System.out.println(StringUtil.getStringFromKey(wallet1.getPrivateKey()));
        System.out.println(StringUtil.getStringFromKey(wallet1.getPublicKey()));

        Transaction transaction = new Transaction(wallet1.getPublicKey(), wallet2.getPublicKey(), new BigDecimal(5), null);
        transaction.generateSignature(wallet1.getPrivateKey());
        System.out.printf("Signature is verified: %s%n", transaction.verifySignature());
        System.out.printf("Block chain is valid: %b", chain.isChainValid());
    }
}
