package bgu.spl.net.srv.messages;

public class NotificationMessage extends Message {
    private boolean isPrivate;
    private String postingUser;
    private String content;

    public NotificationMessage(boolean isPrivate, String postingUser, String content) {
        this.isPrivate = isPrivate;
        this.postingUser = postingUser;
        this.content = content;
    }

    public boolean isPrivate() {
        return isPrivate;
    }

    public String getPostingUser() {
        return postingUser;
    }

    public String getContent() {
        return content;
    }


}
