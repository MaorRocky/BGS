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
    private short numOfUsers = -1;

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
                    //String check = popString();
                    //System.out.println(check);
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
                } else if (length == 5) {
                    numOfUsersBytes[1] = nextByte;
                    numOfUsers = bytesToShort(numOfUsersBytes);
                } else if (nextByte == '\0') {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == numOfUsers) {
                    toSend = new FollowMessage(numOfUsers, popString());
                }
                break;
            /*Post Message*/
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
                System.out.println(toSend.getType());
                break;

        }
        return toSend;
    }

    public byte[] encode(Message message) {

        byte[] toReturn;
        // Notification message
        if (message instanceof NotificationMessage) {
            char type;
            if (((NotificationMessage) message).isPrivate()) {
                type = '0';
            } else {
                type = '1';
            }
            Short s = 9; //opcode = 9
            byte[] opcode = shortToBytes(s);
            byte[] user = ((NotificationMessage) message).getPostingUser().getBytes();
            byte[] content = ((NotificationMessage) message).getContent().getBytes();
            toReturn = new byte[3 + opcode.length + user.length + content.length];
            copyFromTo(toReturn, opcode, 0);
            toReturn[opcode.length] = (byte) type;
            /*added +1 since we added the "type byte*/
            copyFromTo(toReturn, user, opcode.length + 1);
            toReturn[opcode.length + user.length] = '\0';
            /*added +2 since we added +1 for the type byte and we also added \0 char*/
            copyFromTo(toReturn, content, opcode.length + 2 + user.length);
            toReturn[toReturn.length - 1] = '\0';

        }
        // AckMessage for follow/unfollow
        else if (message instanceof AckFollowMessage) {
            byte[] opcode = shortToBytes((short) 10);
            byte[] messageOpcode = shortToBytes((short) 4);
            byte[] numOfUsers = shortToBytes(((AckFollowMessage) message).getNumOfUsers());
            String namesString = "";
            for (String name : ((AckFollowMessage) message).getUsers()) {
                namesString += name + '\0';
            }
            byte[] userNameListString = namesString.getBytes();
            toReturn = new byte[opcode.length + messageOpcode.length + numOfUsers.length + userNameListString.length];
            copyFromTo(toReturn, opcode, 0);
            copyFromTo(toReturn, messageOpcode, opcode.length);
            copyFromTo(toReturn, numOfUsers, opcode.length + messageOpcode.length);
            copyFromTo(toReturn, userNameListString, opcode.length + messageOpcode.length + numOfUsers.length);


        } else if (message instanceof AckStatMessage) {
            byte Opcode[] = shortToBytes((short) 10);
            byte messageOpcode[] = shortToBytes((short) 8);
            byte[] NumPosts = shortToBytes(((AckStatMessage) message).getNumOfPosts());
            byte[] NumFollowers = shortToBytes(((AckStatMessage) message).getNumOfFollowers());
            byte[] NumFollowing = shortToBytes(((AckStatMessage) message).getNumOfFollowing());
            toReturn = new byte[Opcode.length + messageOpcode.length + NumPosts.length
                    + NumFollowers.length + NumFollowing.length];
            copyFromTo(toReturn, Opcode, 0);
            copyFromTo(toReturn, messageOpcode, Opcode.length);
            copyFromTo(toReturn, NumPosts, Opcode.length + messageOpcode.length);
            copyFromTo(toReturn, NumFollowers, Opcode.length + messageOpcode.length + NumPosts.length);
            copyFromTo(toReturn, NumFollowing, Opcode.length + messageOpcode.length + NumFollowers.length + NumFollowing.length);

        } else if (message instanceof AckUserListMessage) {
            byte[] Opcode = shortToBytes((short) 10);
            byte[] messageOpcode = shortToBytes((short) 7);
            byte[] NumOfUsers = shortToBytes(((AckUserListMessage) message).getNumOfUsers());
            String UserNameListString = "";
            for (String UserName : ((AckUserListMessage) message).getUserList()) {
                UserNameListString += UserName + '\0';
            }
            byte[] UserNameListByteArray = UserNameListString.getBytes();
            toReturn = new byte[Opcode.length + messageOpcode.length + NumOfUsers.length + UserNameListByteArray.length];
            copyFromTo(toReturn, Opcode, 0);
            copyFromTo(toReturn, messageOpcode, Opcode.length);
            copyFromTo(toReturn, NumOfUsers, Opcode.length + messageOpcode.length);
            copyFromTo(toReturn, UserNameListByteArray, Opcode.length + messageOpcode.length + NumOfUsers.length);
        }

        // "normal" Ackmessage
        else if (message instanceof AckMessage) {
            short s = 10;
            byte[] opcode = shortToBytes(s);
            byte[] messageOpcode = shortToBytes(((AckMessage) message).getMessageOpcode());
          /*  byte[] optional = ((AckMessage) message).getOptional().getBytes();*/
            toReturn = new byte[opcode.length + messageOpcode.length];
            copyFromTo(toReturn, opcode, 0);
            copyFromTo(toReturn, messageOpcode, opcode.length);
            /*copyFromTo(toReturn, optional, opcode.length + messageOpcode.length);*/

        }
        // Error message
        else {
            short s = 11;
            byte[] opcode = shortToBytes(s);
            byte[] messageOpcode = shortToBytes(((ErrorMessage) message).getOpcode());
            toReturn = new byte[opcode.length + messageOpcode.length + 1];
            copyFromTo(toReturn, opcode, 0);
            copyFromTo(toReturn, messageOpcode, opcode.length);
            toReturn[toReturn.length - 1] = '\0';
        }
        return toReturn;
    }


    private short bytesToShort(byte[] byteArr) {
        short result = (short) ((byteArr[0] & 0xff) << 8);
        result += (short) (byteArr[1] & 0xff);
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


    private byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }


    private void copyFromTo(byte[] arr, byte toCopy[], int from) {
        for (int i = 0; i < toCopy.length; i++) {
            arr[i + from] = toCopy[i];
        }
    }


}
