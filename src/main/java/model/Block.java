package model;

import utility.StringUtil;

import java.util.Objects;

public class Block {

    public String hash;
    public final String previousHash;
    public final String data;
    public final long timeStamp;
    private int nonce;

    public Block(String data, String previousHash) {
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
    }

    public String calculateHash() {
        return StringUtil.applySha256(String.format("%s%d%s", previousHash, timeStamp, data));
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0'); // Create a string with difficulty * "0"
        while(!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }
}