package server;

import shared.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.rmi.RemoteException;

public class PublisherGUI extends JFrame {
    private final PublisherInterface service;
    private final JTextArea logArea;
    private final JComboBox<String> topicCombo;
    private final JTextField messageField;

    public PublisherGUI(PublisherInterface service) {
        super("Publisher Dashboard");
        this.service = service;
        
        setLayout(new BorderLayout(10, 10));
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Header
        JLabel header = new JLabel("Notification Publisher", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 18));
        add(header, BorderLayout.NORTH);
        
        // Input Panel
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topicCombo = new JComboBox<>(Constants.TOPICS);
        messageField = new JTextField(25);
        JButton publishBtn = createStyledButton("Publish", new Color(70, 130, 180));
        
        inputPanel.add(new JLabel("Topic:"));
        inputPanel.add(topicCombo);
        inputPanel.add(new JLabel("Message:"));
        inputPanel.add(messageField);
        inputPanel.add(publishBtn);
        add(inputPanel, BorderLayout.CENTER);
        
        // Log Area
        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setFont(new Font("Consolas", Font.PLAIN, 12));
        add(new JScrollPane(logArea), BorderLayout.SOUTH);
        
        // Event Handlers
        publishBtn.addActionListener(this::publishNotification);
        messageField.addActionListener(this::publishNotification);
    }

    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setBackground(bgColor);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setFont(new Font("Segoe UI", Font.BOLD, 12));
        return button;
    }

    private void publishNotification(ActionEvent e) {
        try {
            String topic = (String) topicCombo.getSelectedItem();
            String message = messageField.getText().trim();
            
            if (!message.isEmpty()) {
                service.publishNotification(topic, message);
                logArea.append("[PUBLISHED] " + topic + ": " + message + "\n");
                messageField.setText("");
            }
        } catch (RemoteException ex) {
            JOptionPane.showMessageDialog(this, "Publish failed: " + ex.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}