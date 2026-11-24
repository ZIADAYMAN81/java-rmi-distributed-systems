import java.io.*;
import java.net.*;
import java.util.*;

public class Server1 {
    private static final int SERVER_PORT = 1000;
    private static final List<String> products = new ArrayList<>();

    static {
        products.add("P1 - T-Shirt");
        products.add("P2 - Jeans");
        products.add("P3 - Jacket");
    }

    public static void main(String[] args) {
        System.out.println("Server 1 is running on port " + SERVER_PORT);
        try (ServerSocket serverSocket = new ServerSocket(SERVER_PORT)) {
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(() -> handleRequest(socket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleRequest(Socket socket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String productId = in.readLine();
            System.out.println("Server1 received: " + productId);

            String response = "Product not found";
            if (productId != null) {
                for (String p : products) {
                    if (p.startsWith(productId)) { response = p; break; }
                }
            }
            out.println(response);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try { socket.close(); } catch (IOException ignore) {}
        }
    }
}
