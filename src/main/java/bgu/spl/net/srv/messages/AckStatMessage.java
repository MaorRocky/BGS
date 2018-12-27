package bgu.spl.net.srv.messages;

public class AckStatMessage extends AckMessage {
    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;
}
