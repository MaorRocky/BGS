package bgu.spl.net.srv.bidi;

import bgu.spl.net.api.bidi.Connections;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionsImpl<T> implements Connections<T> {
    private boolean isReactor;
    private ConcurrentHashMap<Integer, ConnectionHandler<T>> clients;

    public ConnectionsImpl(boolean isReactor) {
        clients = new ConcurrentHashMap<>();
        this.isReactor = isReactor;
    }

    public boolean send(int connectionId, T msg) {
        if (clients.containsKey(connectionId)) {
            clients.get(connectionId).send(msg);
            return true;
        }
        else {
            return false;
        }
    }

    public void broadcast (T msg) {
        for (Integer id: clients.keySet()) {
            clients.get(id).send(msg);
        }
    }

    public void disconnect(int connectionId) {
        if (clients.containsKey(connectionId)) {
            clients.remove(connectionId);
        }
    }

    public boolean addClient(int clientId) {
        if (!clients.containsKey(clientId)) {
            clients.put
        }
    }
}
