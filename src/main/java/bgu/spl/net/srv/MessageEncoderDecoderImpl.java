package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.messages.Message;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class MessageEncoderDecoderImpl implements MessageEncoderDecoder<Message> {
    private byte[] bytes = new byte[1 << 10];
    private int shortCounter = 0;
    private short nextShort;
    private int length = 0;
    private int nextZeroByteCounter;

    public Message decodeNextByte(byte nextByte) {
        if (length < 2) {
            pushByte(nextByte);
            return null;
        }
        nextShort = bytesToShort(bytes);
        switch (nextShort) {
            case 1:
                if (nextByte == 0) {
                    nextZeroByteCounter++;
                }
                if (nextZeroByteCounter == 2) {
                    Message toSend = ne
                }
                else {
                    pushByte(nextByte);
                    return null;
                }

        }
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
        return result;
    }
}
