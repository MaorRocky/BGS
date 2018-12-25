package bgu.spl.net.srv.messages;

import java.util.LinkedList;
import java.util.List;

public class FollowMessage extends Message {
    private boolean follow;
    private List<String> usersToFollow;
    private int numOfUsersToFollow;

    public FollowMessage(boolean follow, String toProcess) {
        this.follow = follow;
        process(toProcess);
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

    @Override
    public void process(String toProcess) {

    }
}
