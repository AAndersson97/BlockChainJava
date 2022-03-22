package model;

import utility.StringUtil;

import java.util.ArrayList;

public class Block {

    public String hash;
    private String merkleRoot;
    public final String previousHash;
    public final ArrayList<Transaction> transactions;
    public final long timeStamp;
    private int nonce;

    public Block(String previousHash) {
        this.previousHash = previousHash;
        this.timeStamp = System.currentTimeMillis();
        this.hash = calculateHash();
        this.transactions = new ArrayList<>();
    }

    public String calculateHash() {
        return StringUtil.applySha256(String.format("%s%d%s%s", previousHash, timeStamp, nonce, merkleRoot));
    }

    public void mineBlock(int difficulty) {
        merkleRoot = StringUtil.getMerkleRoot(transactions);
        String target = StringUtil.getDifficultyString(difficulty);
        while(!hash.substring( 0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
    }

    public boolean addTransaction(Chain chain, Transaction transaction) {
        // Process transaction and check if valid, unless block is genesis block then ignore.
        if(transaction == null) return false;
        if(!previousHash.equals("0") && !transaction.processTransaction(chain)) {
            return false;
        }
        return transactions.add(transaction);
    }
}