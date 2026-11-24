package client;

import shared.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javax.swing.JOptionPane;

public class SubscriberApp {
    public static void main(String[] args) {
        try {
            String subscriberId = JOptionPane.showInputDialog("Enter your subscriber ID:");
            if (subscriberId == null || subscriberId.trim().isEmpty()) return;
            
            Registry registry = LocateRegistry.getRegistry("localhost", Constants.RMI_PORT);
            PublisherInterface service = (PublisherInterface) registry.lookup(Constants.RMI_ID);
            new SubscriberGUI(subscriberId, service).setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Connection error: " + e.getMessage(), 
                "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}