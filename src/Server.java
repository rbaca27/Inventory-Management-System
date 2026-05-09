import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    public static void main(String[] args) throws IOException {

        InventoryManager inventoryManager = new InventoryManager();
        new Journaler(inventoryManager);

        ServerSocket serverSocket = new ServerSocket(8080);
        ExecutorService threadPool = Executors.newFixedThreadPool(50);

        while (true) {
            Socket clientSocket = serverSocket.accept();
            threadPool.execute(new ClientHandler(clientSocket, inventoryManager));
        }
    }
}

class ClientHandler implements Runnable {
    private final Socket socket;
    private final InventoryManager inv;

    public ClientHandler(Socket socket, InventoryManager inv) {
        this.socket = socket;
        this.inv = inv;
    }

    @Override
    public void run() {
        try (socket; Scanner input = new Scanner(socket.getInputStream())) {
            if (input.hasNextLine()) {

                String request = input.nextLine();
                System.out.println("Received request from " + socket.getInetAddress()+ ": " + request);

                String[] actionLine = request.split(",");
                String action = actionLine[0];
                String productName = actionLine[1];
                int quantity = Integer.parseInt(actionLine[2]);

                if (action.equals("add")) {
                    inv.addProduct(productName, quantity);
                    inv.printProductList();
                } else if (action.equals("sell")) {
                    inv.sellProduct(productName, quantity);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
