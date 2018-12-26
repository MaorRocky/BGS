package bgu.spl.net.srv.messages;

public class AckMessage extends Message{
    private short messageOpcode;
    private String optional;

    public AckMessage(short messageOpcode, String optional) {
        this.messageOpcode = messageOpcode;
        this.optional = optional;
    }

    public short getMessageOpcode() {
        return messageOpcode;
    }

    public String getOptional() {
        return optional;
    }

    @Override
    public String getType() {
        return "AckMessage";
    }
}
