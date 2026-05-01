import java.util.concurrent.atomic.AtomicInteger;

public class Product {
    private String productName;
    private final AtomicInteger inventoryAmount;

    Product(String productName, int initialAmount){
        String normalizedStr = InventoryManager.normalizeStr(productName);
        this.productName = normalizedStr;
        this.inventoryAmount = new AtomicInteger(initialAmount);
    }

    public boolean tryToSell(int quantityRequested){
        while(true){
            int currentAmount = inventoryAmount.get();

            if (currentAmount < quantityRequested || quantityRequested  <= 0) {
                return false;
            }
            int newAmount = currentAmount - quantityRequested;
            if (inventoryAmount.compareAndSet(currentAmount, newAmount)){
                Journaler.log("sell", this.productName, quantityRequested);
                return true;
            }
        }
    }

    public void updateStock(int amount) {
        this.inventoryAmount.addAndGet(amount);
    }

    //setters and getters
    public AtomicInteger getInventoryAmount() {
        return inventoryAmount;
    }

    public String getProductName() {
        return productName;
    }
    public void setProductName(String productName) {
        this.productName = productName;
    }
}
