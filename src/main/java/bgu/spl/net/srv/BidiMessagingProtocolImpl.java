package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.*;
import bgu.spl.net.srv.messages.Message;

import java.io.UncheckedIOException;

public class BidiMessagingProtocolImpl implements bgu.spl.net.api.bidi.BidiMessagingProtocol {
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
                break;
            case "LoginMessage":
                break;
            case "LogoutMessage":
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
    public boolean shouldTerminate() {
        return false;
    }


    private void RegisterMessage() {
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Connections getConnections() {
        return connections;
    }
}
