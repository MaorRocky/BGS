package bgu.spl.net.srv.messages;

public class StatusMessage {
    private String userToCheck;

    public StatusMessage(String userToCheck) {
        this.userToCheck = userToCheck;
    }

    public String getuserToCheck() {
        return userToCheck;
    }
}
