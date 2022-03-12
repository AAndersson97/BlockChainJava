public class Application {
    public static void main(String[] args) {
        Chain chain = new Chain();
        chain.addData("This is the second block");
        chain.addData("This is the third block");
        System.out.printf("Block chain is valid: %b", chain.isChainValid());
    }
}
