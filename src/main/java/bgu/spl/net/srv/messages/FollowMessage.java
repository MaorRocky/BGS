package bgu.spl.net.srv.messages;

import java.util.LinkedList;
import java.util.List;

public class FollowMessage extends Message {
    private boolean follow;
    private List<String> usersToFollow;
    private int numOfUsersToFollow;

    public FollowMessage(int numOfUsersToFollow, String toProcess) {
        this.numOfUsersToFollow = numOfUsersToFollow;
        usersToFollow = new LinkedList<>();
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


    public void process(String toProcess) {
        String nameToAdd = "";
        if (toProcess.charAt(0) == '0') {
            follow = true;
        }
        else if (toProcess.charAt(0) == '1') {
            follow = false;
        }
        int index = 3;
        while (index < toProcess.length()) {
            if (toProcess.charAt(index) != '0') {
                nameToAdd = nameToAdd + toProcess.charAt(index);
            }
            else {
                usersToFollow.add(nameToAdd);
                nameToAdd = "";
            }
            index++;
        }
    }

    @Override
    public String getType() {
        return "FollowMessage";
    }
}
