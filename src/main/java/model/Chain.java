package model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class Chain {

    public final static int difficulty = 5;
    public final static BigDecimal minimumTransaction = new BigDecimal("0.005");
    public final ArrayList<Block> blockChain = new ArrayList<>();
    public final HashMap<String, TransactionOutput> UTXOs = new HashMap<>(); // list of unspent transactions

    public Chain() {
        blockChain.add(new Block("First block", "0"));
    }

    public void addData(String data) {
        Block block = new Block(data, blockChain.get(blockChain.size() - 1).previousHash);
        block.mineBlock(difficulty);
        blockChain.add(block);
    }

    public boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        String hashTarget = new String(new char[difficulty]).replace('\0', '0');

        for(int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i-1);
            if (!currentBlock.hash.equals(currentBlock.calculateHash()) ||
                    !previousBlock.hash.equals(currentBlock.previousHash) ||
                    hashIsNotSolved(currentBlock, hashTarget)) {
                return false;
            }
        }
        return true;
    }

    private boolean hashIsNotSolved(Block currentBlock, String hashTarget) {
        return !currentBlock.hash.substring( 0, difficulty).equals(hashTarget);
    }
}
