package bgu.spl.net.srv;

import bgu.spl.net.srv.bidi.ConnectionHandler;
import bgu.spl.net.srv.messages.Message;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class InformationHolder {
    private static class SingletonHolder {
        private static InformationHolder instance = new InformationHolder();
    }
    private ConcurrentHashMap<Integer, Boolean> registeredClients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Boolean> loggedinClients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, LinkedList<Integer>> clientToFollowList = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, LinkedList<Integer>> clientToFollowers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, LinkedList<String>> clientToPostList = new ConcurrentHashMap<>();
    ConnectionsImpl<Message> connections = new ConnectionsImpl<>();



    public static InformationHolder getInstance() {
        return SingletonHolder.instance;
    }

    public boolean isRegistered(Integer clientId) {
        if (registeredClients.containsKey(clientId)) {
            return registeredClients.get(clientId);
        }
        else {
            return false;
        }
    }

    public boolean isLoggedIn(Integer clientId) {
        if (loggedinClients.containsKey(clientId)) {
            return loggedinClients.get(clientId);
        }
        else {
            return false;
        }
    }

    public LinkedList<Integer> getClientFollowList(Integer clientId) {
        if (clientToFollowList.containsKey(clientId)) {
            return clientToFollowList.get(clientId);
        }
        else {
            return null;
        }
    }

    public LinkedList<String> getClientsPostList(Integer clientId) {
        if (clientToPostList.containsKey(clientId)) {
            return clientToPostList.get(clientId);
        }
        else {
            return null;
        }
    }

    public boolean registerClient(Integer clientId, ConnectionHandler handler) {
        if(connections.addClient(clientId, handler)) {
            registeredClients.put(clientId, true);
            loggedinClients.put(clientId, false);
            clientToFollowList.put(clientId, new LinkedList<>());
            clientToFollowers.put(clientId, new LinkedList<>());
            clientToPostList.put(clientId, new LinkedList<>());
            return true;
        }
        else {
            return false;
        }
    }

    public boolean login(Integer clientId) {
        if(!isLoggedIn(clientId)) {
            loggedinClients.replace(clientId, true);
            return true;
        }
        else {
            return false;
        }
    }

    public void addFollower (Integer clientId, Integer clientToFollow) {
        if (!clientToFollowList.get(clientId).contains(clientToFollow)) {
            clientToFollowList.get(clientId).addLast(clientToFollow);
        }
        if (!clientToFollowers.get(clientToFollow).contains(clientId)) {
            clientToFollowers.get(clientToFollow).addLast(clientId);
        }
    }

    public ConnectionsImpl getConnections() {
        return connections;
    }



}
