package bgu.spl.net.srv.messages;

public class LoginMessage extends Message {
    private String userName;
    private String password;

    public LoginMessage(String toProcess) {
        process(toProcess);
    }

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }

    public void process(String toProcess) {
        int index = 0;
        while (toProcess.charAt(index) != '\0') { //creating the userName
            userName = userName + toProcess.charAt(index);
            index ++;
        }
        index++;
        while (index < toProcess.lastIndexOf('\0')) {
            password = password + toProcess.charAt(index);
            index++;
        }
    }
}
