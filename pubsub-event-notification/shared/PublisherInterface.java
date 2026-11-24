package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface PublisherInterface extends Remote {
    void subscribe(String topic, SubscriberInterface subscriber) throws RemoteException;
    void unsubscribe(String topic, SubscriberInterface subscriber) throws RemoteException;
    void publishNotification(String topic, String message) throws RemoteException;
}