import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryManager {

    private final HashMap<String, Product> inventory = new HashMap<>();

    public void addProduct(String productName, int quantity){
        String normalizedStr = normalizeStr(productName);
        if (inventory.containsKey(normalizedStr)){
            inventory.get(normalizedStr).updateStock(quantity);
            System.out.printf("Updated %s inventory by %d; total: %d\n", normalizedStr, quantity,
                    inventory.get(normalizedStr).getInventoryAmount().get());
        }else {
            Product product = new Product(normalizedStr, quantity);
            Journaler.log("add", productName, quantity);
            inventory.put(normalizedStr, product);
            System.out.printf("Added %d %s to inventory.\n", quantity, normalizedStr);
        }
    }

    public void sellProduct(String productName, int quantity) {
        String normalizedStr = normalizeStr(productName);
        Product product = inventory.get(normalizedStr);

        if (product != null) {
            // Let the Product handle the math and the thread-safety
            if (product.tryToSell(quantity)) {
                System.out.println("Success!");
            } else {
                System.out.println("Sale failed: Not enough stock or tried selling 0 units.");
            }
        } else {
            System.out.println("Failed: Product not found.");
        }
    }

    public static String normalizeStr(String string){
        return string.replaceAll("[^a-zA-Z0-9/-]", " ").strip().toLowerCase();
    }

    public AtomicInteger getProductInventory(String productName) {
        String normalizedStr = normalizeStr(productName);
        Product product = inventory.get(normalizedStr);

        if (product == null) {
            System.out.println("Spelling error or missing product: " + normalizedStr);
            return null;
        }
        return product.getInventoryAmount();
    }

    static void main() {
        InventoryManager inv = new InventoryManager();
        inv.addProduct("iPhone 16", 20);
        inv.addProduct("iPhone 17",13);
        inv.sellProduct("iphone 17", 11);
    }
}
