package model;

import utility.StringUtil;

import java.util.Objects;

public class Block {

    public final String hash;
    public final String previousHash;
    public final String data;
    public final long timeStamp;

    public Block(String data, String previousHash) {
        this.hash = calculateHash();
        this.previousHash = previousHash;
        this.data = data;
        this.timeStamp = System.currentTimeMillis();
    }

    public String calculateHash() {
        return StringUtil.applySha256(String.format("%s%d%s", previousHash, timeStamp, data));
    }
}