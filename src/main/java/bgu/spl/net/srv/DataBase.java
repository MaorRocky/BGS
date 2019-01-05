package bgu.spl.net.srv;

import bgu.spl.net.srv.messages.*;

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

    private ConcurrentHashMap<String, Boolean> registeredClients;
    private ConcurrentHashMap<String, String> clientUserNameToPassword;
    private ConcurrentHashMap<String, Boolean> loggedinClients;
    private ConcurrentHashMap<String, LinkedList<String>> clientToFollowList;
    private ConcurrentHashMap<String, LinkedList<String>> clientToFollowers;
    private ConcurrentHashMap<String, LinkedList<PostMessage>> clientToPostList;
    private ConcurrentHashMap<String, LinkedList<PrivateMessage>> clientToPrivateMessageList;
    private ConcurrentHashMap<String, Integer> clientUserNameToCurrentHandlerId;
    private ConcurrentHashMap<String, LinkedList<Message>> clientUserNameToUnReceivedMessages;
    private LinkedList<String> userNamesList;

    public DataBase() {
        registeredClients = new ConcurrentHashMap<>();
        loggedinClients = new ConcurrentHashMap<>();
        clientToFollowList = new ConcurrentHashMap<>();
        clientToFollowers = new ConcurrentHashMap<>();
        clientToPostList = new ConcurrentHashMap<>();
        clientToPrivateMessageList = new ConcurrentHashMap<>();
        userNamesList = new LinkedList<>();
        clientUserNameToPassword = new ConcurrentHashMap<>();
        clientUserNameToCurrentHandlerId = new ConcurrentHashMap<>();
        clientUserNameToUnReceivedMessages = new ConcurrentHashMap<>();
    }


    public boolean isRegistered(String clientName) {
        if (registeredClients.containsKey(clientName)) {
            return registeredClients.get(clientName);
        } else {
            return false;
        }

    }

    public boolean isLoggedIn(String clientName) {
        if (loggedinClients.containsKey(clientName)) {
            return loggedinClients.get(clientName);
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

    public LinkedList<PostMessage> getClientsPostList(String clientName) {
        if (clientToPostList.containsKey(clientName)) {
            return clientToPostList.get(clientName);
        } else {
            return null;
        }
    }

    public boolean registerClient(int connectionId, RegisterMessage message) {
        if (!registeredClients.containsKey(message.getUserName())) {
            registeredClients.put(message.getUserName(), true);
            loggedinClients.put(message.getUserName(), false);
            clientToFollowList.put(message.getUserName(), new LinkedList<>());
            clientToFollowers.put(message.getUserName(), new LinkedList<>());
            clientToPostList.put(message.getUserName(), new LinkedList<>());
            clientToPrivateMessageList.put(message.getUserName(), new LinkedList<>());
            userNamesList.add(message.getUserName());
            clientUserNameToPassword.put(message.getUserName(), message.getPassword());
            clientUserNameToCurrentHandlerId.put(message.getUserName(), connectionId);
            clientUserNameToUnReceivedMessages.put(message.getUserName(), new LinkedList<>());


            return true;
        } else {
            return false;
        }
    }

    public void addUnReceivedMessage(String userName, Message message) {
        if (clientUserNameToUnReceivedMessages.containsKey(userName)) {
            synchronized (clientUserNameToUnReceivedMessages.get(userName)) {
                clientUserNameToUnReceivedMessages.get(userName).add(message);
            }
        }
    }

    public LinkedList<Message> getUnReceivedMessages(String userName) {
        if (clientUserNameToUnReceivedMessages.containsKey(userName)) {
            synchronized (clientUserNameToUnReceivedMessages.get(userName)) {
                LinkedList<Message> toReturn = clientUserNameToUnReceivedMessages.get(userName);
                clientUserNameToUnReceivedMessages.replace(userName, new LinkedList<>());
                return toReturn;
            }
        }
        return null;
    }

    public boolean login(LoginMessage msg) {
        if (registeredClients.containsKey(msg.getUserName())
                && clientUserNameToPassword.get(msg.getUserName()).equals(msg.getPassword())) {
            if (!isLoggedIn(msg.getUserName())) {
                loggedinClients.replace(msg.getUserName(), true);
                return true;
            }
        }
        return false;
    }

    /*we add clientToFollow to the clientName followList
     * we add clientName to clientToFollow followersList */

    public String addFollower(String clientName, String clientToFollow) {
        String addedFollower = "";
        if (clientToFollowList.containsKey(clientName)) {
            synchronized (clientToFollowList.get(clientName)) {
                if (!clientToFollowList.get(clientName).contains(clientToFollow)) {
                    clientToFollowList.get(clientName).addLast(clientToFollow);
                    addedFollower = clientToFollow;
                }
            }
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

   // private boolean checkUserNameAndPassword(Integer clientId, String userName, String password) {
    //    return (clientToUserNameAndPassword.get(clientId).getFirst().equals(userName)) &
    //            (clientToUserNameAndPassword.get(clientId).getSecond().equals(password));
   // }

    public void logOut(String clientName) {
        loggedinClients.replace(clientName, false);
    }


    public int getCurrentHandlerIdFromUserName(String userName) {
        if (clientUserNameToCurrentHandlerId.containsKey(userName))
            return clientUserNameToCurrentHandlerId.get(userName);
        else return -1;
    }

    public void addPost(String clientName, PostMessage post) {
        if (clientToPostList.containsKey(clientName)) {
            clientToPostList.get(clientName).add(post);
        }
    }

    public void addPrivateMessage(String clientName, PrivateMessage privateMessage) {
        if (clientToPrivateMessageList.containsKey(clientName)) {
            clientToPrivateMessageList.get(clientName).add(privateMessage);
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
