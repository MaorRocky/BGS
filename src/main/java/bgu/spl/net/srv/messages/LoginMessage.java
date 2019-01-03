package bgu.spl.net.srv.messages;

public class LoginMessage extends Message {
    private String userName;
    private String password;

    public LoginMessage(String toProcess) {
        userName = "";
        password = "";
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
        while (index <= toProcess.length() - 1 && toProcess.charAt(index) != '\0') { //creating the userName
            userName = userName + toProcess.charAt(index);
            index ++;
        }
        index++;
        //TODO check why popString returns extra two \0 in the end of the string
        while (index <= toProcess.length() - 1 && toProcess.charAt(index) != '\0') {
            password = password + toProcess.charAt(index);
            index++;
        }
        //index < toProcess.lastIndexOf('\0')
    }

    @Override
    public String getType() {
        return "LoginMessage";
    }
}
