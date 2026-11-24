import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final String HOST = "localhost";
        final int PORT = 6000;

        try (
            Socket socket = new Socket(HOST, PORT);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            Scanner sc = new Scanner(System.in)
        ) {
            System.out.println(in.readLine()); // greeting

            System.out.print("Enter server number (1 or 2): ");
            String serverChoice = sc.nextLine();
            out.println(serverChoice);

            System.out.println(in.readLine()); // prompt for product
            System.out.print("Enter Product ID (e.g., P1): ");
            String productId = sc.nextLine();
            out.println(productId);

            String resp = in.readLine();
            System.out.println("Response: " + resp);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
