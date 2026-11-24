package client;

import shared.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.rmi.RemoteException;

public class SubscriberGUI extends JFrame {
    private final String subscriberId;
    private final PublisherInterface service;
    private final SubscriberImpl subscriber;
    private final JTextArea messageArea;
    
    public SubscriberGUI(String subscriberId, PublisherInterface service) throws RemoteException {
        super("Subscriber: " + subscriberId);
        this.subscriberId = subscriberId;
        this.service = service;
        this.subscriber = new SubscriberImpl(subscriberId);
        
        setLayout(new BorderLayout(10, 10));
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Header
        JLabel header = new JLabel(subscriberId + "'s Notifications", SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 16));
        add(header, BorderLayout.NORTH);
        
        // Subscription Panel
        JPanel subPanel = new JPanel(new GridLayout(1, Constants.TOPICS.length, 5, 5));
        for (String topic : Constants.TOPICS) {
            JCheckBox checkBox = new JCheckBox(topic);
            checkBox.addItemListener(e -> toggleSubscription(topic, e.getStateChange() == ItemEvent.SELECTED));
            subPanel.add(checkBox);
        }
        add(subPanel, BorderLayout.CENTER);
        
        // Message Area
        messageArea = new JTextArea();
        messageArea.setEditable(false);
        messageArea.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        add(new JScrollPane(messageArea), BorderLayout.SOUTH);
        
        subscriber.setMessageHandler(this::displayMessage);
    }
    
    private void toggleSubscription(String topic, boolean subscribe) {
        try {
            if (subscribe) {
                service.subscribe(topic, subscriber);
            } else {
                service.unsubscribe(topic, subscriber);
            }
        } catch (RemoteException e) {
            JOptionPane.showMessageDialog(this, "Subscription error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void displayMessage(String topic, String message) {
        SwingUtilities.invokeLater(() -> {
            messageArea.append("[" + topic + "] " + message + "\n");
        });
    }
}