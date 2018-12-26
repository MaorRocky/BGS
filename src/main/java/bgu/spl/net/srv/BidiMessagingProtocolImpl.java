package bgu.spl.net.srv;

import bgu.spl.net.api.bidi.*;
import bgu.spl.net.srv.messages.Message;
import bgu.spl.net.srv.messages.RegisterMessage;

public class BidiMessagingProtocolImpl<T> implements bgu.spl.net.api.bidi.BidiMessagingProtocol<T> {
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


    private void RegisterMessage(RegisterMessage message) {
        start();
    }

    public int getConnectionId() {
        return connectionId;
    }

    public Connections getConnections() {
        return connections;
    }

}
