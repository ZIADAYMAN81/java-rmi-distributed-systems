package server;

import shared.*;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class NotificationServer extends UnicastRemoteObject implements PublisherInterface {
    private final Map<String, Set<SubscriberInterface>> topicSubscribers = new ConcurrentHashMap<>();

    public NotificationServer() throws RemoteException {
        super();
        Arrays.stream(Constants.TOPICS).forEach(topic -> 
            topicSubscribers.put(topic, ConcurrentHashMap.newKeySet()));
    }

    @Override
    public synchronized void subscribe(String topic, SubscriberInterface subscriber) throws RemoteException {
        topicSubscribers.computeIfAbsent(topic, k -> ConcurrentHashMap.newKeySet())
                       .add(subscriber);
        System.out.println(subscriber.getSubscriberId() + " subscribed to " + topic);
    }

    @Override
    public synchronized void unsubscribe(String topic, SubscriberInterface subscriber) throws RemoteException {
        if (topicSubscribers.containsKey(topic)) {
            topicSubscribers.get(topic).remove(subscriber);
            System.out.println(subscriber.getSubscriberId() + " unsubscribed from " + topic);
        }
    }

    @Override
    public void publishNotification(String topic, String message) throws RemoteException {
        Set<SubscriberInterface> subscribers = topicSubscribers.getOrDefault(topic, Collections.emptySet());
        subscribers.forEach(sub -> {
            try {
                sub.receiveNotification(topic, message);
            } catch (RemoteException e) {
                try {
                    System.err.println("Failed to notify " + sub.getSubscriberId());
                } catch (RemoteException e1) {
                    // TODO Auto-generated catch block
                    e1.printStackTrace();
                }
            }
        });
        System.out.println("Published to " + subscribers.size() + " subscribers of " + topic);
    }

    public static void main(String[] args) {
        try {
            NotificationServer server = new NotificationServer();
            Registry registry = LocateRegistry.createRegistry(Constants.RMI_PORT);
            registry.rebind(Constants.RMI_ID, server);
            System.out.println("Server running on port " + Constants.RMI_PORT);
        } catch (Exception e) {
            System.err.println("Server error: " + e.getMessage());
            e.printStackTrace();
        }
    }
}