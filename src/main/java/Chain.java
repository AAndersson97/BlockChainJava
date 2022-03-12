import com.google.gson.GsonBuilder;
import model.Block;

import java.util.ArrayList;

public class Chain {

    private final static int difficulty = 5;
    private ArrayList<Block> blockChain = new ArrayList<>();

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
