package server;

import shared.*;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class PublisherApp {
    public static void main(String[] args) {
        try {
            Registry registry = LocateRegistry.getRegistry("localhost", Constants.RMI_PORT);
            PublisherInterface service = (PublisherInterface) registry.lookup(Constants.RMI_ID);
            new PublisherGUI(service).setVisible(true);
        } catch (Exception e) {
            System.err.println("Publisher error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}