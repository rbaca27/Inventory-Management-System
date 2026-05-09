import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryManager {
    private final HashMap<String, Product> inventory = new HashMap<>();

    //Only call this method alone when recovering inventory. Otherwise, use addProduct or sellProduct.
    void updateInventory(String action, String productName, int quantity) {
        String normalized = normalizeStr(productName);

        if (action.equals("add")) {
            if (inventory.containsKey(normalized)) {
                inventory.get(normalized).updateStock(quantity);
            } else {
                inventory.put(normalized, new Product(normalized, quantity));
            }
        }
        else if (action.equals("sell")) {
            if (inventory.containsKey(normalized)) {
                inventory.get(normalized).updateStock(-quantity);
            }
        }
    }

    public void addProduct(String name, int qty) {
        Journaler.log("add", name, qty);
        updateInventory("add", name, qty);
    }

    public void sellProduct(String name, int qty) {
        Product p = inventory.get(normalizeStr(name));
        if (p != null && p.tryToSell(qty)) {
            System.out.println("Sold!");
        }else  {
            System.out.println("No product found");
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
    public void printProductList(){
        System.out.println(inventory.keySet());
    }
}
