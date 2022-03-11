import com.google.gson.GsonBuilder;
import model.Block;

import java.util.ArrayList;

public class Chain {

    public ArrayList<Block> blockChain = new ArrayList<>();

    public boolean isChainValid() {
        Block currentBlock;
        Block previousBlock;
        for(int i = 1; i < blockChain.size(); i++) {
            currentBlock = blockChain.get(i);
            previousBlock = blockChain.get(i-1);
            if (!currentBlock.hash.equals(currentBlock.calculateHash()) ||
                    !previousBlock.hash.equals(currentBlock.previousHash)) {
                return false;
            }
        }
        return true;
    }
}
