package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.messages.*;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10];
    private short nextShort;
    private int length = 0;
    private int nextZeroByteCounter = 0;
    private boolean follow;
    private byte[] numOfUsersBytes = new byte[2];
    private short numOfUsers;

    public Message decodeNextByte(byte nextByte) {
        if (length < 2) {
            pushByte(nextByte);
            return null;
        }
        if (length == 2) {
            nextShort = bytesToShort(bytes);
        }
        Message toSend = null;
        switch (nextShort) {
            case 1: //register
                pushByte(nextByte);
                if (nextByte == 0) {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == 2) {
                    toSend = new RegisterMessage(popString());
                }
                break;

            case 2: //login
                pushByte(nextByte);
                if (nextByte == 0) {
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
                pushByte(nextByte);
                if (length == 3 && nextByte == 0) {
                    follow = true;
                }
                else if (length == 3 && nextByte == 1) {
                    follow = false;
                }
                else if (length == 4) {
                    numOfUsersBytes[0] = nextByte;
                }
                else if (length == 5) {
                    numOfUsersBytes[1] = nextByte;
                    numOfUsers = bytesToShort(numOfUsersBytes);
                }
                else if (nextByte == 0) {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == numOfUsers-1) {
                    toSend = new FollowMessage(follow, popString());
                }
                break;



        }
        return toSend;
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
        String result = new String(bytes, 0, length, StandardCharsets.UTF_8);
        length = 0;
        nextZeroByteCounter = 0;
        return result;
    }
}
