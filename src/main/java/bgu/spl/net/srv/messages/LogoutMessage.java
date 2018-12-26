package bgu.spl.net.srv.messages;

public class LogoutMessage extends Message {

    public LogoutMessage(){}

    @Override
    public String getType() {
        return "LogoutMessage";
    }
}
