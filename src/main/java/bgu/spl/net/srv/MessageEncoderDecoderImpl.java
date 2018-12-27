package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.messages.*;

import java.nio.charset.StandardCharsets;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10];
    private short nextShort;
    private int length = 0;
    private int nextZeroByteCounter = 0;
    private byte[] numOfUsersBytes = new byte[2];
    private short numOfUsers;

    public Message decodeNextByte(byte nextByte) {
        pushByte(nextByte);
        if (length < 2) {
            return null;
        }
        if (length == 2) {
            nextShort = bytesToShort(bytes);
        }
        Message toSend = null;
        switch (nextShort) {
            case 1: //register
                if (nextByte == '\0') {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == 2) {
                    toSend = new RegisterMessage(popString());
                }
                break;

            case 2: //login
                if (nextByte == '\0') {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == 2) {
                    toSend = new LoginMessage(popString());
                }
                break;

            case 3://logout
                toSend = new LogoutMessage();
                popString();
                break;

            case 4:
                if (length == 4) {
                    numOfUsersBytes[0] = nextByte;
                }
                else if (length == 5) {
                    numOfUsersBytes[1] = nextByte;
                    numOfUsers = bytesToShort(numOfUsersBytes);
                }
                else if (nextByte == '\0') {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == numOfUsers-1) {
                    toSend = new FollowMessage(numOfUsers, popString());
                }
                break;

            case 5:
                if (nextByte == '\0') {
                    toSend = new PostMessage(popString());
                }
                break;

            case 6:
                if (nextByte == '\0') {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == 2) {
                    toSend = new PrivateMessage(popString());
                }
                break;

            case 7:
                toSend = new UserListMessage();
                popString();
                break;

            case 8:
                if (nextByte == '\0') {
                    toSend = new StatMessage(popString());
                }
                break;

        }
        return toSend;
    }

    public byte[] encode(Message message) {

        byte[] toReturn;
        if (message instanceof NotificationMessage) {
            char type;
            if (((NotificationMessage) message).isPrivate()) {
                type = '0';
            }
            else {
                type = '1';
            }
            Short s = 9; //opcode = 9
            byte[] opcode = shortToBytes(s);
            byte[] user = ((NotificationMessage) message).getPostingUser().getBytes();
            byte[] content = ((NotificationMessage) message).getContent().getBytes();
            toReturn = new byte[3 + opcode.length + user.length + content.length];
            copyFromTo(toReturn,opcode,0);
            copyFromTo(toReturn,user,opcode.length);
            copyFromTo(toReturn,content,opcode.length+user.length);
            toReturn[toReturn.length-1] = '\0';
        }

        else if (message instanceof AckMessage) {
            short s = 10;
            byte[] opcode = shortToBytes(s);
            byte[] messageOpcode = shortToBytes(((AckMessage) message).getMessageOpcode());
            byte[] optional = ((AckMessage) message).getOptional().getBytes();
            toReturn = new byte[opcode.length + messageOpcode.length + optional.length + 1];
            copyFromTo(toReturn,opcode,0);
            copyFromTo(toReturn,messageOpcode,opcode.length);
            copyFromTo(toReturn,optional,opcode.length+optional.length);
            toReturn[toReturn.length-1] = '\0';
        }

        else {
            short s = 11;
            byte[] opcode = shortToBytes(s);
            byte[] messageOpcode = shortToBytes(((ErrorMessage) message).getOpcode());
            toReturn = new byte[opcode.length + messageOpcode.length + 1];
            copyFromTo(toReturn,opcode,0);
            copyFromTo(toReturn,messageOpcode,opcode.length);
            toReturn[toReturn.length-1] = '\0';
        }
        return toReturn;
    }


    private short bytesToShort(byte[] byteArr)  {
        short result = (short)((byteArr[0] & 0xff) << 8);
        result += (short)(byteArr[1] & 0xff);
        return result;
    }

    private void pushByte(byte nextByte) {
        bytes[length] = nextByte;
        length++;
    }

    private String popString() {
        String result = new String(bytes, 2, length, StandardCharsets.UTF_8);
        length = 0;
        nextZeroByteCounter = 0;
        return result;
    }


    public byte[] shortToBytes(short num)
    {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte)((num >> 8) & 0xFF);
        bytesArr[1] = (byte)(num & 0xFF);
        return bytesArr;
    }

    public static void copyFromTo(byte[] arr, byte toCopy[], int from ){
        for (int i = 0; i <toCopy.length ; i++) {
            arr[i+from] = toCopy[i];
        }
    }
}
