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

        /*while (index < toProcess.length()) {
            if (toProcess.charAt(index) != '\0' && (toProcess.charAt(index) != ' ')) {
                nameToAdd = nameToAdd + toProcess.charAt(index);
            }
            else {
                if (!nameToAdd.equals(""))
                    usersToFollow.add(nameToAdd);
                nameToAdd = "";
            }
            index++;
        }*/
        String temp = "";
        int lastWordindex = 3;
        for (int i = 3; i < toProcess.length(); i++) {
            if (toProcess.charAt(i) == '\0') {
                temp = toProcess.substring(lastWordindex, i);
                usersToFollow.add(temp);
                lastWordindex = i + 1;
            }
            if (i == toProcess.length()-1) {
                temp = toProcess.substring(lastWordindex);
                usersToFollow.add(temp);
            }
        }
    }

    @Override
    public String getType() {
        return "FollowMessage";
    }
}
