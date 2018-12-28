package bgu.spl.net.srv.messages;

import java.util.LinkedList;

public class AckUserListMessage extends AckMessage {
    private short numOfUsers;
    private LinkedList<String> userList;

    public AckUserListMessage(LinkedList<String> userList) {
        this.numOfUsers = (short) userList.size();
        this.userList = userList;
    }

    public short getNumOfUsers() {
        return numOfUsers;
    }

    public LinkedList<String> getUserList() {
        return userList;
    }
}
