package bgu.spl.net.srv.messages;

public class AckStatMessage extends AckMessage {
    private short numOfPosts;
    private short numOfFollowers;
    private short numOfFollowing;

    public AckStatMessage( short numOfPosts, short numOfFollowers, short numOfFollowing) {
        this.numOfPosts = numOfPosts;
        this.numOfFollowers = numOfFollowers;
        this.numOfFollowing = numOfFollowing;
    }

    public short getNumOfPosts() {
        return numOfPosts;
    }

    public short getNumOfFollowers() {
        return numOfFollowers;
    }

    public short getNumOfFollowing() {
        return numOfFollowing;
    }
}
