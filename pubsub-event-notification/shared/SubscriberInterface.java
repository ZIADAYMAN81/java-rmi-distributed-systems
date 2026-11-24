package shared;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface SubscriberInterface extends Remote {
    void receiveNotification(String topic, String message) throws RemoteException;
    String getSubscriberId() throws RemoteException;
}