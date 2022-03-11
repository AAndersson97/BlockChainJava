import com.google.gson.GsonBuilder;
import model.Block;

public class Application {
    public static void main(String[] args) {
        Chain chain = new Chain();
        Block head = new Block("First block", "0");
        Block secondBlock = new Block("Second block", head.hash);
        Block thirdBlock = new Block("Third block", secondBlock.hash);
        chain.blockChain.add(head);
        chain.blockChain.add(secondBlock);
        chain.blockChain.add(thirdBlock);
        System.out.println(new GsonBuilder().setPrettyPrinting().create().toJson(chain.blockChain));
        System.out.printf("Block chain is valid: %b", chain.isChainValid());
    }
}
