package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.*;
import bgu.spl.net.srv.messages.*;

import java.util.LinkedList;
import java.util.List;

public class BidiMessagingProtocolImpl implements BidiMessagingProtocol<Message> {
    private DataBase dataBase = DataBase.getInstance();
    private int connectionId;
    private Connections connections;
    private String userName;
    private String password;

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
                RegisterMessage((RegisterMessage) message);
                break;
            case "LoginMessage":
                LoginMessage((LoginMessage) message);
                break;
            case "LogoutMessage":
                LogoutMessage();
                break;
            case "FollowMessage":
                FollowMessage((FollowMessage)message);
                break;
            case "PostMessage":
                break;
            case "PrivateMessage":
                break;
            case "UserListMessage":
                break;
            case "StatMessage":
                break;
        }

    }

    @Override
    public boolean shouldTerminate() {
        return false;
    }


    private void RegisterMessage(RegisterMessage message) {
        if (!dataBase.isRegistered(connectionId)) {
            dataBase.registerClient(connectionId, message);
            userName = message.getUserName();
            password = message.getPassword();
            AckMessage ack = new AckMessage((short) 1, null);
            connections.send(connectionId, ack);
        }
        else {
            ErrorMessage error = new ErrorMessage((short) 1);
            connections.send(connectionId,error);
        }
    }

    private void LoginMessage(LoginMessage message) {
        if (dataBase.login(connectionId, message)) { // checks userName, password and if the client is already logged in
            AckMessage ack = new AckMessage((short) 2, null);
            connections.send(connectionId, ack);
        } else {
            ErrorMessage error = new ErrorMessage((short) 2);
            connections.send(connectionId, error);
        }
    }

    private void LogoutMessage() {
        if (dataBase.isLoggedIn(connectionId)) {
            dataBase.logOut(connectionId);
            AckMessage ack = new AckMessage((short) 3, null);
            connections.send(connectionId, ack);
        }
        else {
            ErrorMessage error = new ErrorMessage((short) 3);
            connections.send(connectionId, error);
        }
    }

    private void FollowMessage(FollowMessage message) {
        if (dataBase.isLoggedIn(connectionId)){
            List<String> usersToFollow = message.getUsersToFollow();
            LinkedList<String> addedOrRemovedFollowers = new LinkedList<>();
            if (message.isFollow()) {
                for (String followUser: usersToFollow) {
                    String temp = dataBase.addFollower(userName, followUser);
                    if (!temp.equals(""))
                    addedOrRemovedFollowers.add(temp);
                }
            }
            else {
                for (String unfollowUser:message.getUsersToFollow()) {
                    String temp =   dataBase.removeFollower(userName,unfollowUser);
                    if (!temp.equals(""))
                        addedOrRemovedFollowers.add(temp);
                }
            }
            AckFollowMessage ackFollowMessage = new AckFollowMessage(addedOrRemovedFollowers);
            connections.send(connectionId,ackFollowMessage);

        }
        else {
            ErrorMessage error = new ErrorMessage((short) 4);
            connections.send(connectionId, error);
        }
    }

    private void PostMessage(PostMessage message){
        for (String taggedUser:message.getTaggedUsers()) {
            NotificationMessage notificationMessage = new NotificationMessage(
                    false,userName,message.getPost());
            int temp = dataBase.getIdFromUserName(taggedUser);
            if (temp!=-1){
                connections.send(temp,notificationMessage);
            }
        }
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Connections getConnections() {
        return connections;
    }

}
