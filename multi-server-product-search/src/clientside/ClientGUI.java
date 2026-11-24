import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.Socket;

public class ClientGUI extends JFrame {
    private JTextField serverField, productField;
    private JTextArea responseArea;
    private JButton sendButton;

    public ClientGUI() {
        setTitle("ClothBranch - Client");
        setSize(420, 320);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(8, 8));

        // Input Panel
        JPanel inputPanel = new JPanel(new GridLayout(3, 2, 8, 8));
        inputPanel.add(new JLabel("Enter Server (1 or 2):"));
        serverField = new JTextField();
        inputPanel.add(serverField);

        inputPanel.add(new JLabel("Enter Product ID (e.g., P1):"));
        productField = new JTextField();
        inputPanel.add(productField);

        sendButton = new JButton("Search Product");
        inputPanel.add(sendButton);

        add(inputPanel, BorderLayout.NORTH);

        // Response Area
        responseArea = new JTextArea();
        responseArea.setEditable(false);
        add(new JScrollPane(responseArea), BorderLayout.CENTER);

        // Button Action
        sendButton.addActionListener(e -> sendRequest());

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void sendRequest() {
        String serverChoice = serverField.getText().trim();
        String productId = productField.getText().trim();

        if (!serverChoice.equals("1") && !serverChoice.equals("2")) {
            JOptionPane.showMessageDialog(this, "Invalid server choice. Please enter 1 or 2.");
            return;
        }
        if (productId.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please enter a Product ID like P1.");
            return;
        }

        try (
            Socket socket = new Socket("localhost", 6000);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {
            // Protocol with Coordinator:
            // 1) Read greeting
            String greeting = in.readLine(); // "CONNECTED_TO_COORDINATOR"
            if (greeting == null) {
                responseArea.append("Coordinator did not respond.\n");
                return;
            }

            // 2) Send server choice
            out.println(serverChoice);

            // 3) Read product prompt then send productId
            String prompt = in.readLine(); // "ENTER_PRODUCT_ID"
            if (prompt == null) {
                responseArea.append("Coordinator closed connection unexpectedly.\n");
                return;
            }

            out.println(productId);

            // 4) Read final response (single line)
            String serverResponse = in.readLine();
            if (serverResponse == null) serverResponse = "No response received.";

            responseArea.append("Server " + serverChoice + " → " + serverResponse + "\n");

        } catch (Exception ex) {
            responseArea.append("Error: " + ex.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ClientGUI::new);
    }
}
