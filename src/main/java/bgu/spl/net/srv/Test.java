package bgu.spl.net.srv;

import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.srv.messages.Message;
import bgu.spl.net.srv.messages.RegisterMessage;

import java.util.Arrays;

public class Test {
    public static void main (String[] args) {

        //RegisterMessage register = new RegisterMessage("na1da0v\0t1\0");
        //System.out.println(register.getUserName());
        //System.out.println(register.getPassword());
        short opcode = 2;
        String userName = "n0";
        String password = "t1";
        byte[] opcodeBytes = shortToBytes(opcode);
        byte[] userNameBytes = userName.getBytes();
        byte[] passwordBytes = password.getBytes();
        byte[] toDecode = new byte[2 + opcodeBytes.length + userNameBytes.length + passwordBytes.length];
        for (int i = 0; i < opcodeBytes.length; i++) {
            toDecode[i] = opcodeBytes[i];
        }
        for (int i = 0; i < userNameBytes.length; i++) {
            toDecode[i + opcodeBytes.length] = userNameBytes[i];
        }
        toDecode[opcodeBytes.length + userNameBytes.length] = '\0';
        for (int i = 0; i < passwordBytes.length; i++) {
            toDecode[i + 1 + opcodeBytes.length + userName.length()] = passwordBytes[i];
        }
        toDecode[toDecode.length - 1] = '\0';
        System.out.println(Arrays.toString(toDecode));

        MessageEncoderDecoderImpl encdec = new MessageEncoderDecoderImpl();
        for (int i = 0; i < toDecode.length; i++) {
            encdec.decodeNextByte(toDecode[i]);
        }






    }

    private static byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }




}
