package utility;

import model.Transaction;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.ArrayList;
import java.util.Base64;

public class StringUtil {
    public static byte[] applyECDSASig(PrivateKey privateKey, String input) {
        Signature dsa;
        try {
            dsa = Signature.getInstance("ECDSA", "BC");
            dsa.initSign(privateKey);
            byte[] strByte = input.getBytes();
            dsa.update(strByte);
            return dsa.sign();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public static String applySha256(String input){
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            //Applies sha256 to our input,
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(); // This will contain hash as hexidecimal
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        }
        catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean verifyECDSASig(PublicKey publicKey, String data, byte[] signature) {
        try {
            Signature verify = Signature.getInstance("ECDSA", "BC");
            verify.initVerify(publicKey);
            verify.update(data.getBytes());
            return verify.verify(signature);
        } catch(Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String getMerkleRoot(ArrayList<Transaction> transactions) {
        int count = transactions.size();
        String[] previousTreeLayer = new String[transactions.size()];
        for(int i = 0; i < transactions.size(); i++) {
            previousTreeLayer[i] = (transactions.get(i).getTransactionId());
        }
        String[] treeLayer = previousTreeLayer;
        while(count > 1) {
            treeLayer = new String[previousTreeLayer.length];
            for(int i=1; i < previousTreeLayer.length; i++) {
                treeLayer[i] = (applySha256(previousTreeLayer[i-1] + previousTreeLayer[i]));
            }
            count = treeLayer.length;
            previousTreeLayer = treeLayer;
        }
        return (treeLayer.length == 1) ? treeLayer[0] : "";
    }

    public static String getDifficultyString(int difficulty) {
        return new String(new char[difficulty]).replace('\0', '0');
    }

    public static String getStringFromKey(Key key) {
        return Base64.getEncoder().encodeToString(key.getEncoded());
    }

    public static String getStringFromKeys(Key key1, Key key2) {
        return String.format("%s%s", getStringFromKey(key1), getStringFromKey(key2));
    }
}
