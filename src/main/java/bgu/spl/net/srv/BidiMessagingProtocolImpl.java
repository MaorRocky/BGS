package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.*;
import bgu.spl.net.srv.messages.*;

import java.util.LinkedList;
import java.util.List;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {
    private DataBase dataBase;
    private int connectionId;
    private Connections connections;
    private String userName;
    private String password;
    boolean terminate;

    public BidiMessagingProtocolImpl(DataBase dataBase) {
        this.dataBase = dataBase;
        terminate = false;
    }

    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }


    @Override
    public void process(Message message) {
        String messageType = message.getType();
        switch (messageType) {
            case "RegisterMessage":
                processRegisterMessage((RegisterMessage) message);
                break;
            case "LoginMessage":
                processLoginMessage((LoginMessage) message);
                break;
            case "LogoutMessage":
                processLogoutMessage();
                break;
            case "FollowMessage":
                processFollowMessage((FollowMessage) message);
                break;
            case "PostMessage":
                processPostMessage((PostMessage) message);
                break;
            case "PrivateMessage":
                processPrivateMessage((PrivateMessage) message);
                break;
            case "UserListMessage":
                processUserListMessage();
                break;
            case "StatMessage":
                processStatMessage((StatMessage) message);
                break;
        }

    }

    @Override
    public boolean shouldTerminate() {
        return terminate;
    }

    private void processRegisterMessage(RegisterMessage message) {
        if (!dataBase.isRegistered(connectionId)) {
            dataBase.registerClient(connectionId, message);
            userName = message.getUserName();
            password = message.getPassword();
            AckMessage ack = new AckMessage((short) 1, null);
            connections.send(connectionId, ack);
        } else {
            ErrorMessage error = new ErrorMessage((short) 1);
            connections.send(connectionId, error);
        }
    }

    private void processLoginMessage(LoginMessage message) {
        if (dataBase.isRegistered(connectionId) && dataBase.login(connectionId, message)) { // checks userName, password and if the client is already logged in
            AckMessage ack = new AckMessage((short) 2, null);
            connections.send(connectionId, ack);
        } else {
            ErrorMessage error = new ErrorMessage((short) 2);
            connections.send(connectionId, error);
        }
    }

    private void processLogoutMessage() {
        if (dataBase.isRegistered(connectionId) && dataBase.isLoggedIn(connectionId)) {
            dataBase.logOut(connectionId);
            AckMessage ack = new AckMessage((short) 3, null);
            connections.send(connectionId, ack);
            terminate = true;
        } else {
            ErrorMessage error = new ErrorMessage((short) 3);
            connections.send(connectionId, error);
        }
    }

    private void processFollowMessage(FollowMessage message) {
        if (dataBase.isRegistered(connectionId) && dataBase.isLoggedIn(connectionId)) {
            List<String> usersToFollow = message.getUsersToFollow();
            LinkedList<String> addedOrRemovedFollowers = new LinkedList<>();
            if (message.isFollow()) {
                for (String followUser : usersToFollow) {
                    String temp = dataBase.addFollower(userName, followUser);
                    if (!temp.equals(""))
                        addedOrRemovedFollowers.add(temp);
                }
            } else {
                for (String unfollowUser : message.getUsersToFollow()) {
                    String temp = dataBase.removeFollower(userName, unfollowUser);
                    if (!temp.equals(""))
                        addedOrRemovedFollowers.add(temp);
                }
            }
            AckFollowMessage ackFollowMessage = new AckFollowMessage(addedOrRemovedFollowers);
            connections.send(connectionId, ackFollowMessage);

        } else {
            ErrorMessage error = new ErrorMessage((short) 4);
            connections.send(connectionId, error);
        }
    }

    private void processPostMessage(PostMessage message) {
        if (dataBase.isRegistered(connectionId) && dataBase.isLoggedIn(connectionId)) {
            for (String taggedUser : message.getTaggedUsers()) {
                if (dataBase.isRegistered(dataBase.getIdFromUserName(taggedUser))) {
                    NotificationMessage notificationMessage = new NotificationMessage(
                            false, userName, message.getPost());
                    int temp = dataBase.getIdFromUserName(taggedUser);
                    connections.send(temp, notificationMessage);
                }
            }
            dataBase.addPost(connectionId, message);
            connections.send(connectionId, new AckMessage((short) 5, null));
        } else {
            connections.send(connectionId, new ErrorMessage((short) 5));
        }
    }

    private void processPrivateMessage(PrivateMessage message) {
        if (dataBase.isRegistered(connectionId) && dataBase.isLoggedIn(connectionId) && dataBase.isRegistered(dataBase.getIdFromUserName(message.getReceiverUser())) ) {
            NotificationMessage notificationMessage = new NotificationMessage(true, userName, message.getContent());
            dataBase.addPrivateMessage(connectionId, message);
            connections.send(dataBase.getIdFromUserName(message.getReceiverUser()), notificationMessage);
            connections.send(connectionId, new AckMessage((short) 6, null));
        } else {
            connections.send(connectionId, new ErrorMessage((short) 6));
        }
    }

    private void processUserListMessage() {
        if (dataBase.isRegistered(connectionId) && dataBase.isLoggedIn(connectionId)) {
            AckUserListMessage ack = new AckUserListMessage(dataBase.getUserNamesList());
            connections.send(connectionId, ack);
        } else {
            connections.send(connectionId, new ErrorMessage((short) 7));
        }
    }

    private void processStatMessage(StatMessage message) {
        if (dataBase.isRegistered(connectionId) && dataBase.isLoggedIn(connectionId) &&
                dataBase.isRegistered(dataBase.getIdFromUserName(message.getuserToCheck()))) {
            int idToCheck = dataBase.getIdFromUserName(message.getuserToCheck());
            short numOfPosts = (short) dataBase.getClientsPostList(idToCheck).size();
            short numOfFollowers = (short) dataBase.getClientFollowersList(message.getuserToCheck()).size();
            short numOfFollowing = (short) dataBase.getClientFollowList(message.getuserToCheck()).size();
            AckStatMessage toSend = new AckStatMessage(numOfPosts, numOfFollowers, numOfFollowing);
            connections.send(connectionId, toSend);
        } else {
            connections.send(connectionId, new ErrorMessage((short) 8));
        }
    }


    public int getConnectionId() {
        return connectionId;
    }

    public Connections getConnections() {
        return connections;
    }

}
