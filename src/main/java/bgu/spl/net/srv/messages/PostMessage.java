package bgu.spl.net.srv.messages;

import java.util.LinkedList;
import java.util.List;

public class PostMessage extends Message {
    private String post;
    private List<String> taggedUsers;

    public PostMessage(String toProcess) {
        this.post = post;
        taggedUsers = new LinkedList<>();
        findTaggedUsers();
    }

    public String getPost() {
        return post;
    }

    public List<String> getTaggedUsers() {
        return taggedUsers;
    }

    private void findTaggedUsers() {
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

    @Override
    public void process(String toProcess) {
        
    }
}
