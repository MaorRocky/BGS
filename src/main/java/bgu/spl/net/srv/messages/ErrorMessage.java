package bgu.spl.net.srv.messages;

public class ErrorMessage extends Message {
    private short opcode;

    public ErrorMessage(short opcode) {
        this.opcode = opcode;
    }

    public short getOpcode() {
        return opcode;
    }

    @Override
    public String getType() {
        return "ErrorMessage";
    }
}
