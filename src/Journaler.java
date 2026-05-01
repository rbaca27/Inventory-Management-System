
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Journaler {

    static BufferedWriter writer;
    static {
        try {
            writer = new BufferedWriter(new FileWriter("journal.txt", true));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void recover(InventoryManager inv){
        File file = new File("journal.txt");
        if (file.exists() && file.canRead()){

        }
    }
    public static void log(String action, String product, int quantity) {
        try {
            writer.write(action.toLowerCase() + "," + product.toLowerCase() + "," + quantity);
            writer.newLine();
            writer.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

