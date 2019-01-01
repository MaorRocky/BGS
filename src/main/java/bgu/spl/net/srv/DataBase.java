package bgu.spl.net.srv;

import bgu.spl.net.srv.messages.LoginMessage;
import bgu.spl.net.srv.messages.PostMessage;
import bgu.spl.net.srv.messages.PrivateMessage;
import bgu.spl.net.srv.messages.RegisterMessage;


import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;

public class DataBase {


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
     *  clientToFollowList this Hashmap links to each User - userName(String) to a linkedList<String> which will
     *  include all of the users which the User is following .
     *
     *  clientToFollowers this Hashmap links to each User - userName(String) to  a linkedList<String> which will
     *  include all of the users which follows the user.
     *
     *  clientToPostList  this Hashmap links to each User - userName(String)to  a linkedList<String> which will
     *  include all the posts which the user posted
     *
     *  clientNameToClientId this Hashmap links to each User - ClientName(String) a Int value which is
     *  the specific clientID.
     *
     *  clientToPricateMessageList is a hsshmap which links to each user his privateMessageList.
     *  */

    private ConcurrentHashMap<Integer, Boolean> registeredClients;
    private ConcurrentHashMap<Integer, Pair<String, String>> clientToUserNameAndPassword;
    private ConcurrentHashMap<Integer, Boolean> loggedinClients;
    private ConcurrentHashMap<String, LinkedList<String>> clientToFollowList;
    private ConcurrentHashMap<String, LinkedList<String>> clientToFollowers;
    private ConcurrentHashMap<Integer, LinkedList<PostMessage>> clientToPostList;
    private ConcurrentHashMap<String, Integer> clientNameToClientId;
    private ConcurrentHashMap<Integer, LinkedList<PrivateMessage>> clientToPrivateMessageList;
    private LinkedList<String> userNamesList;

    public DataBase() {
        registeredClients = new ConcurrentHashMap<>();
        clientToUserNameAndPassword = new ConcurrentHashMap<>();
        loggedinClients = new ConcurrentHashMap<>();
        clientToFollowList = new ConcurrentHashMap<>();
        clientToFollowers = new ConcurrentHashMap<>();
        clientToPostList = new ConcurrentHashMap<>();
        clientNameToClientId = new ConcurrentHashMap<>();
        clientToPrivateMessageList = new ConcurrentHashMap<>();
        userNamesList = new LinkedList<>();

    }


    public boolean isRegistered(Integer clientId) {
        if (registeredClients.containsKey(clientId)) {
            return registeredClients.get(clientId);
        } else {
            return false;
        }
    }

    public boolean isLoggedIn(Integer clientId) {
        if (loggedinClients.containsKey(clientId)) {
            return loggedinClients.get(clientId);
        } else {
            return false;
        }
    }

    public LinkedList<String> getClientFollowList(String clientName) {
        if (clientToFollowList.containsKey(clientName)) {
            return clientToFollowList.get(clientName);
        } else {
            return null;
        }
    }

    public LinkedList<PostMessage> getClientsPostList(Integer clientId) {
        if (clientToPostList.containsKey(clientId)) {
            return clientToPostList.get(clientId);
        } else {
            return null;
        }
    }

    public boolean registerClient(Integer clientId, RegisterMessage message) {
        if (!registeredClients.containsKey(clientId)) {
            clientToUserNameAndPassword.put(clientId, new Pair<>(message.getUserName(), message.getPassword()));
            registeredClients.put(clientId, true);
            loggedinClients.put(clientId, false);
            clientToFollowList.put(message.getUserName(), new LinkedList<>());
            clientToFollowers.put(message.getUserName(), new LinkedList<>());
            clientToPostList.put(clientId, new LinkedList<>());
            clientNameToClientId.put(message.getUserName(), clientId);
            clientToPrivateMessageList.put(clientId, new LinkedList<>());
            userNamesList.add(message.getUserName());
            return true;
        } else {
            return false;
        }
    }

    public boolean login(Integer clientId, LoginMessage msg) {
        if (checkUserNameAndPassword(clientId, msg.getUserName(), msg.getPassword())) {
            if (!isLoggedIn(clientId)) {
                loggedinClients.replace(clientId, true);
                return true;
            }
        }
        return false;
    }

    /*we add clientToFollow to the clientName followList
     * we add clientName to clientToFollow followersList */

    public String addFollower(String clientName, String clientToFollow) {
        String addedFollower = "";
        if (!clientToFollowList.get(clientName).contains(clientToFollow)) {
            clientToFollowList.get(clientName).addLast(clientToFollow);
            addedFollower = clientToFollow;
        }
        if (clientToFollowers.containsKey(clientToFollow)) {
            if (!clientToFollowers.get(clientToFollow).contains(clientName)) {
                clientToFollowers.get(clientToFollow).addLast(clientName);
            }
        }
        /*if the user indeed started following we will return it */
        return addedFollower;
    }


    /*We will remove clientToUnfollow from clientName following list, which means clientName will stop
     * following clientToUnfollow
     * we will remove clientName from clientToUnfollow followers list because clientName stopped
     * following him*/
    public String removeFollower(String clientName, String clientToUnfollow) {
        String removedFollower = "";
        if (clientToFollowList.get(clientName).contains(clientToUnfollow)) {
            clientToFollowList.get(clientName).remove(clientToUnfollow);
            removedFollower = clientToUnfollow;
        }
        if (clientToFollowers.containsKey(clientToUnfollow)) {
            if (clientToFollowers.get(clientToUnfollow).contains(clientName)) {
                clientToFollowers.get(clientToUnfollow).remove(clientName);
            }
        }
        return removedFollower;

    }

    private boolean checkUserNameAndPassword(Integer clientId, String userName, String password) {
        return (clientToUserNameAndPassword.get(clientId).getFirst().equals(userName)) &
                (clientToUserNameAndPassword.get(clientId).getSecond().equals(password));
    }

    public void logOut(int clientID) {
        loggedinClients.replace(clientID, false);
    }


    public int getIdFromUserName(String userName) {
        if (clientNameToClientId.containsKey(userName))
            return clientNameToClientId.get(userName);
        else return -1;
    }

    public void addPost(Integer clientId, PostMessage post) {
        if (clientToPostList.containsKey(clientId)) {
            clientToPostList.get(clientId).add(post);
        }
    }

    public void addPrivateMessage(Integer clientId, PrivateMessage privateMessage) {
        if (clientToPrivateMessageList.containsKey(clientId)) {
            clientToPrivateMessageList.get(clientId).add(privateMessage);
        }
    }

    public LinkedList<String> getUserNamesList() {
        return userNamesList;
    }

    public LinkedList<String> getClientFollowersList(String clientName) {
        if (clientToFollowers.containsKey(clientName)) {
            return clientToFollowers.get(clientName);
        }
        else {
            return null;
        }
    }
}
