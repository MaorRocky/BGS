package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.*;
import bgu.spl.net.srv.messages.*;

import java.util.List;

public class BidiMessagingProtocolImpl<T> implements BidiMessagingProtocol<T> {
    private InformationHolder information = InformationHolder.getInstance();
    private int connectionId;
    private Connections connections;


    @Override
    public void start(int connectionId, Connections connections) {
        this.connectionId = connectionId;
        this.connections = connections;
    }

    @Override
    public void process(Object message) {
        Message msg = (Message) message;
        String messageType = msg.getType();
        switch (messageType) {
            case "RegisterMessage":
                RegisterMessage((RegisterMessage) msg);
                break;
            case "LoginMessage":
                LoginMessage((LoginMessage) msg);
                break;
            case "LogoutMessage":
                LogoutMessage();
                break;
            case "FollowMessage":
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
    /*TODO we need to implement this method*/
    public boolean shouldTerminate() {
        return false;
    }


    private void RegisterMessage(RegisterMessage message) {
        this.start(message.getUserName().hashCode(), information.getConnections());
        if (!information.isRegistered(connectionId)) {
            information.registerClient(connectionId, message);
            AckMessage ack = new AckMessage((short) 1, null);
            connections.send(connectionId, ack);
        }
    }

    private void LoginMessage(LoginMessage message) {
        if (information.login(connectionId, message)) {
            AckMessage ack = new AckMessage((short) 2, null);
            connections.send(connectionId, ack);
        } else {
            ErrorMessage error = new ErrorMessage((short) 2);
            connections.send(connectionId, error);
        }
    }

    private void LogoutMessage() {
        if (information.isLoggedIn(connectionId)) {
            information.logOut(connectionId);
            AckMessage ack = new AckMessage((short) 3, null);
            connections.send(connectionId, ack);
        }
    }

    private void FollowMessage(FollowMessage message) {
        if (information.isLoggedIn(connectionId)) {
            List<String> list = message.getUsersToFollow();

        } else {
            ErrorMessage error = new ErrorMessage((short) 2);
            connections.send(connectionId, error);
        }
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Connections getConnections() {
        return connections;
    }

}
