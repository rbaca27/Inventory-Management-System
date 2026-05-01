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
                System.out.println("Not enough units or product does not exist.");
                return false;
            }
            int newAmount = currentAmount - quantityRequested;
            if (inventoryAmount.compareAndSet(currentAmount, newAmount)){
                Journaler.log("sell", this.productName, quantityRequested);
                return true;
            }
        }
    }

    public void updateStock(int amount, String action) {
        this.inventoryAmount.addAndGet(amount);
    }

    public void updateStock(int amount, boolean shouldLog, String action) {
        this.inventoryAmount.addAndGet(amount);
        if (shouldLog) {
            Journaler.log(action, this.productName, amount);
        }
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
