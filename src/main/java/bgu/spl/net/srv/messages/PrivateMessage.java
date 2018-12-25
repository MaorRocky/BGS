package bgu.spl.net.srv.messages;

public class PrivateMessage {
    private String receiverUser;
    private String content;

    public PrivateMessage(String receiverUser, String content) {
        this.receiverUser = receiverUser;
        this.content = content;
    }

    public String getReceiverUser() {
        return receiverUser;
    }

    public String getContent() {
        return content;
    }
}
