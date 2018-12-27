package bgu.spl.net.srv;

import bgu.spl.net.srv.messages.LoginMessage;
import bgu.spl.net.srv.messages.RegisterMessage;


import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {
    private static class SingletonHolder {
        private static DataBase instance = new DataBase();
    }


    /*
     *  registeredClients this Hashmap links to each User - ClientID a boolean value which detriments
     *  if the user is registered or not.
     *
     *  clientToUserNameAndPassword this hashmap links each user -clientID to a Pair which holds a name and username,
     *  this will help us to determine if the user credentials are correct.
     *
     *  loggedinClients this Hashmap links to each User - ClientID a boolean value which detriments
     *  if the user is logged in  or not .
     *
     *  clientToFollowList this Hashmap links to each User - userName(String) a linkedList<String> which will
     *  include all of the users which the User is following .
     *
     *  clientToFollowers this Hashmap links to each User - userName(String) a linkedList<String> which will
     *  include all of the users which follows the user.
     *
     *  clientToPostList  this Hashmap links to each User - userName(String) a linkedList<String> which will
     *  include all the posts which the user posted*/

    private ConcurrentHashMap<Integer, Boolean> registeredClients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Pair> clientToUserNameAndPassword = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, Boolean> loggedinClients = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LinkedList<String>> clientToFollowList = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, LinkedList<String>> clientToFollowers = new ConcurrentHashMap<>();
    private ConcurrentHashMap<Integer, LinkedList<String>> clientToPostList = new ConcurrentHashMap<>();



    public static DataBase getInstance() {
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

    public boolean registerClient(Integer clientId, RegisterMessage message) {
        if(!registeredClients.containsKey(clientId)) {
            clientToUserNameAndPassword.put(clientId, new Pair(message.getUserName(), message.getPassword()));
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

    public boolean login(Integer clientId, LoginMessage msg) {
        if (checkUserNameAndPassword(clientId, msg.getUserName(), msg.getPassword())) {
            if(!isLoggedIn(clientId)) {
                loggedinClients.replace(clientId, true);
                return true;
            }
        }
        return false;
    }

    public void addFollower (Integer clientId, Integer clientToFollow) {
        if (!clientToFollowList.get(clientId).contains(clientToFollow)) {
            clientToFollowList.get(clientId).addLast(clientToFollow);
        }
        if (!clientToFollowers.get(clientToFollow).contains(clientId)) {
            clientToFollowers.get(clientToFollow).addLast(clientId);
        }
    }

    private boolean checkUserNameAndPassword(Integer clientId, String userName, String password) {
        return (clientToUserNameAndPassword.get(clientId).getFirst().equals(userName)) &
                (clientToUserNameAndPassword.get(clientId).getSecond().equals(password));
    }

    public void logOut(int clientID){
        loggedinClients.replace(clientID, false);
    }

}
