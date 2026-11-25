# Distributed Systems in Java: RMI & Sockets

[![tests](https://github.com/ZIADAYMAN81/java-rmi-distributed-systems/actions/workflows/tests.yml/badge.svg)](https://github.com/ZIADAYMAN81/java-rmi-distributed-systems/actions/workflows/tests.yml)

> Distributed Systems coursework, The British University in Egypt. Two small distributed applications built with core Java, with no frameworks. Both compile with JDK 19.

| Project | Communication | What it shows |
|---|---|---|
| [`pubsub-event-notification`](pubsub-event-notification) | Java RMI with remote callbacks | Publish–subscribe pattern, topic subscriptions, server-to-client push |
| [`multi-server-product-search`](multi-server-product-search) | TCP sockets | A coordinator (middleware) routing client queries to one of two product servers |

---

## 1. Publish–Subscribe Event Notification (RMI)
```
PublisherGUI ──publish(topic,msg)──► NotificationServer ──notify()──► SubscriberImpl ──► SubscriberGUI
                                      (RMI registry :1099,            (exported as a remote
                                       "NotificationService")          callback object)
```
- **Topics:** Sports, Technology, Business, Entertainment.
- Subscribers register a **remote callback** (`SubscriberInterface`), so the server can push messages without polling.
- `subscribe` and `unsubscribe` are `synchronized`, and subscriber sets are concurrent, so changes during publishing are safe.
- **Fault tolerance:** if a subscriber becomes unreachable (e.g. its window was closed), the server removes it after the failed delivery, instead of retrying a dead connection on every publish.
- Swing GUIs for both the publisher and the subscribers.

**Run** (from `pubsub-event-notification/`):
```bash
javac -d out shared/*.java server/*.java client/*.java
java -cp out server.PublisherApp      # starts the registry + server + publisher GUI
java -cp out client.SubscriberApp     # start one or more subscribers
```

**Test** the unreachable-subscriber handling (headless, no GUI):
```bash
javac -d out shared/*.java server/*.java client/*.java test/*.java
java -cp out test.DeadSubscriberTest   # expected: alive=2 dead=1  PASS
```

## 2. Multi-Server Product Search (Sockets)
- `Server1` and `Server2` each hold a product catalogue and listen on their own ports.
- `CorMiddle` is a coordinator that knows both servers' ports and forwards each request.
- `ClientGUI`: the user picks a server and searches for a product by name.

**Run** (from `multi-server-product-search/src/`):
```bash
javac -d out */*.java
```
Start `Server1`, `Server2`, the coordinator, then the client GUI. See the `main` method in each class for the order.

## Concepts practised
Remote interfaces and stubs, `UnicastRemoteObject`, RMI registry, callbacks, client–server sockets, coordinator/middleware patterns, Swing event handling, thread-safe shared state.

## Requirements
JDK 8 or later. No external libraries.

## License
MIT, see [LICENSE](LICENSE).
