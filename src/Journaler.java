
import java.io.*;

public class Journaler {

    static BufferedWriter writer;

    Journaler(InventoryManager inv){
        Journaler.recover(inv);
        Journaler.initWriter();

    }

    public static void initWriter() {
        try {
            writer = new BufferedWriter(new FileWriter("journal.txt", true));
        } catch (IOException e) {
            System.err.println("CRITICAL: Could not open journal for writing!");
            e.printStackTrace();
            System.exit(1);
        }
    }
    public static void recover(InventoryManager inv){
        File journal = new File("journal.txt");

        if (!journal.exists()) {
            System.out.println("No journal found. Initializing empty inventory.");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(journal))){
            String line;
            while((line = reader.readLine()) != null){

                if (line.trim().isEmpty()) continue;
                try {
                    String[] actionLine = line.split(",");
                    String action = actionLine[0];
                    String productName = actionLine[1];
                    int quantity = Integer.parseInt(actionLine[2]);
                    inv.updateInventory(action, productName, quantity);

                } catch (NumberFormatException e) {
                    System.err.println("Possible journal corruption");
                }

            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void log(String action, String product, int quantity) {
        try {
            writer.write(action.toLowerCase().strip() + "," + product.toLowerCase() + "," + quantity);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
