package test;
import server.NotificationServer;
import shared.SubscriberInterface;
import java.rmi.RemoteException;
import java.util.concurrent.atomic.AtomicInteger;

public class DeadSubscriberTest {
    static class Fake implements SubscriberInterface {
        final boolean dead; final AtomicInteger calls = new AtomicInteger();
        Fake(boolean dead) { this.dead = dead; }
        public void receiveNotification(String t, String m) throws RemoteException {
            calls.incrementAndGet(); if (dead) throw new RemoteException("connection refused");
        }
        public String getSubscriberId() { return dead ? "dead" : "alive"; }
    }
    public static void main(String[] a) throws Exception {
        NotificationServer s = new NotificationServer();
        Fake alive = new Fake(false), dead = new Fake(true);
        s.subscribe("Sports", alive); s.subscribe("Sports", dead);
        s.publishNotification("Sports", "goal 1");
        s.publishNotification("Sports", "goal 2");
        boolean ok = alive.calls.get() == 2 && dead.calls.get() == 1;
        System.out.println("alive=" + alive.calls.get() + " dead=" + dead.calls.get() + (ok ? "  PASS" : "  FAIL"));
        System.exit(ok ? 0 : 1);
    }
}
