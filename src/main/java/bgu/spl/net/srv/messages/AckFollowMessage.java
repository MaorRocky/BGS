package bgu.spl.net.srv.messages;

import java.util.LinkedList;
import java.util.List;

public class AckFollowMessage extends AckMessage {
    private short numOfUsers;
    private LinkedList<String> users;

    public AckFollowMessage(LinkedList<String> users) {
        this.users = users;
        this.numOfUsers = (short)users.size();
    }

    public short getNumOfUsers() {
        return numOfUsers;
    }

    public LinkedList<String> getUsers() {
        return users;
    }
}
