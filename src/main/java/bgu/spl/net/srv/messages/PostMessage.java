package bgu.spl.net.srv.messages;

import java.util.LinkedList;
import java.util.List;

public class PostMessage extends Message {
    private String post;
    private List<String> taggedUsers;

    public PostMessage(String toProcess) {
        taggedUsers = new LinkedList<>();
        post = "";
        process(toProcess);

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
            int len = tmp.length();
            while (tag < len && buffer.charAt(tag) != ' ') {
                tagToAdd = tagToAdd + buffer.charAt(tag);
                tag++;
            }

            buffer.delete(0, tag);
            taggedUsers.add(tagToAdd);
        }
    }



    public void process(String toProcess) {
        int i = 0;
        while (toProcess.charAt(i) != '\0') {
            post += toProcess.charAt(i);
            i++;
        }
        findTaggedUsers();
    }

    @Override
    public String getType() {
        return "PostMessage";
    }
}
