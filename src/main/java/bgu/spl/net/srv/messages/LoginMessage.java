package bgu.spl.net.srv.messages;

public class LoginMessage {
    private String userName;
    private String password;

    public LoginMessage(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
