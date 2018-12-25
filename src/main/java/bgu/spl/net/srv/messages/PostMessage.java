package bgu.spl.net.srv.messages;

import java.util.LinkedList;
import java.util.List;

public class PostMessage {
    private String post;
    private List<String> taggedUsers;

    public PostMessage(String post) {
        this.post = post;
        taggedUsers = new LinkedList<>();
        getTaggedUsers();
    }

    public String getPost() {
        return post;
    }

    private void getTaggedUsers() {
        String tmp = post;
        StringBuffer buffer = new StringBuffer(tmp);
        while (buffer.indexOf("@") != -1) {
            String tagToAdd = "";
            int tag = buffer.indexOf("@") + 1;
            while (buffer.charAt(tag) != ' ') {
                tagToAdd = tagToAdd + buffer.charAt(tag);
                tag++;
            }
            buffer.delete(0, tag);
            taggedUsers.add(tagToAdd);
        }
    }
}
