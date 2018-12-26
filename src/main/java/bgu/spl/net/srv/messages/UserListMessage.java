package bgu.spl.net.srv.messages;

public class UserListMessage extends Message {

    public UserListMessage() {}

    @Override
    public String getType() {
        return "UserListMessage";
    }
}
