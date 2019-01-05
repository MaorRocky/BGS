package bgu.spl.net.srv.messages;

public class StatMessage extends Message {
    private String userToCheck;

    public StatMessage(String toProcess) {
        process(toProcess);
        System.out.println("i just open a new stat message");
    }

    public String getuserToCheck() {
        return userToCheck;
    }


    public void process(String toProcess) {
        userToCheck = toProcess;
    }

    @Override
    public String getType() {
        return "StatMessage";
    }
}
