import java.io.*;
import java.net.*;

public class CorMiddle {
    private static final int PORT = 6000;
    private static final String HOST = "localhost";
    private static final int SERVER1_PORT = 1000;
    private static final int SERVER2_PORT = 2002;

    public static void main(String[] args) {
        System.out.println("Coordinator is running on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // 1) Send greeting so clients that expect a first line don't hang
            out.println("CONNECTED_TO_COORDINATOR");

            // 2) Read server choice (expects "1" or "2")
            String serverChoice = in.readLine();
            if (serverChoice == null) {
                out.println("ERROR: No server choice received.");
                return;
            }

            // 3) Prompt and read product ID (e.g., P1)
            out.println("ENTER_PRODUCT_ID");
            String productId = in.readLine();
            if (productId == null || productId.isEmpty()) {
                out.println("ERROR: No product ID received.");
                return;
            }

            int targetPort;
            if ("1".equals(serverChoice)) {
                targetPort = SERVER1_PORT;
            } else if ("2".equals(serverChoice)) {
                targetPort = SERVER2_PORT;
            } else {
                out.println("ERROR: Invalid server choice. Use 1 or 2.");
                return;
            }

            // 4) Forward request to the chosen server
            String response = queryBackendServer(HOST, targetPort, productId);

            // 5) Send back to client
            out.println(response == null ? "ERROR: No response from backend server." : response);

        } catch (IOException e) {
            // If anything goes wrong, try to tell the client
            try (PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
                out.println("ERROR: " + e.getMessage());
            } catch (Exception ignore) {}
        } finally {
            try { clientSocket.close(); } catch (IOException ignore) {}
        }
    }

    private static String queryBackendServer(String host, int port, String productId) {
        try (
            Socket serverSocket = new Socket(host, port);
            PrintWriter out = new PrintWriter(serverSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()))
        ) {
            out.println(productId);
            String line = in.readLine();
            return (line == null || line.isEmpty()) ? "Product not found" : line;
        } catch (IOException e) {
            return "ERROR contacting server on port " + port + ": " + e.getMessage();
        }
    }
}