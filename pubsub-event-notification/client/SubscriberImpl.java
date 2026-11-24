package client;

import shared.*;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.function.BiConsumer;

public class SubscriberImpl extends UnicastRemoteObject implements SubscriberInterface {
    private final String subscriberId;
    private BiConsumer<String, String> messageHandler;
    
    public SubscriberImpl(String subscriberId) throws RemoteException {
        super();
        this.subscriberId = subscriberId;
    }
    
    public void setMessageHandler(BiConsumer<String, String> handler) {
        this.messageHandler = handler;
    }
    
    @Override
    public void receiveNotification(String topic, String message) throws RemoteException {
        if (messageHandler != null) {
            messageHandler.accept(topic, message);
        }
    }
    
    @Override
    public String getSubscriberId() throws RemoteException {
        return subscriberId;
    }
}