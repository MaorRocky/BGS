package bgu.spl.net.srv.messages;

public class AckMessage {
    private int messageOpcode;
    private String optional;

    public AckMessage(int messageOpcode, String optional) {
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }

    public int getMessageOpcode() {
        return messageOpcode;
    }

    public String getOptional() {
        return optional;
    }
}
