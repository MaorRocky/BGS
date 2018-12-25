package bgu.spl.net.srv.messages;

import java.util.LinkedList;
import java.util.List;

public class FollowMessage extends Message {
    private boolean follow;
    private List<String> usersToFollow;
    private int numOfUsersToFollow;

    public FollowMessage(boolean follow, LinkedList<String> usersToFollow) {
        this.follow = follow;
        this.usersToFollow = (LinkedList<String>)usersToFollow.clone();
        numOfUsersToFollow = usersToFollow.size();
    }

    public boolean isFollow() {
        return follow;
    }

    public List<String> getUsersToFollow() {
        return usersToFollow;
    }

    public int getNumOfUsersToFollow() {
        return numOfUsersToFollow;
    }
}
