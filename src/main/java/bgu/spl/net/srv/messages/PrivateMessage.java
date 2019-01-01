package bgu.spl.net.srv.messages;

public class PrivateMessage extends Message {
    private String receiverUser;
    private String content;

    public PrivateMessage(String toProcess) {
        receiverUser = "";
        content = "";
        process(toProcess);
    }

    public String getReceiverUser() {
        return receiverUser;
    }

    public String getContent() {
        return content;
    }


    public void process(String toProcess) {
        int index = 0;
        while (toProcess.charAt(index) != '\0') {
            receiverUser = receiverUser + toProcess.charAt(index);
            index++;
        }
        index++;
        while (toProcess.charAt(index) != '\0') {
            content = content + toProcess.charAt(index);
            index++;
        }
    }

    @Override
    public String getType() {
        return "PrivateMessage";
    }
}
