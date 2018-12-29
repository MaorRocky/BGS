package bgu.spl.net.srv;

import bgu.spl.net.srv.*;

import java.util.Arrays;

public class Tester {
    public static void main(String[] args) {

        MessageEncoderDecoderImpl messageEncoderDecoder = new MessageEncoderDecoderImpl();

        short followMessageOpcode = 4;
        byte[] followMessageOpcodeArr = shortToBytes(followMessageOpcode);
        System.out.println("followMessageOpcodeArr byte array: " + Arrays.toString(followMessageOpcodeArr));
        char[] follow = {0};
        byte[] followByteArr = new String(follow).getBytes();
        System.out.println("**");
        System.out.println(Arrays.toString(followByteArr));
        System.out.println("**");
        short numOfUsers = 3;
        byte[] numOfUsersByteArr = shortToBytes(numOfUsers);
        System.out.println("numofusersarray :" + Arrays.toString(numOfUsersByteArr));
        String userNameList = "<Maor><Nadav><Dan>";
        byte[] userNameListByteArr = userNameList.getBytes();
        byte[] byteMessageArray = new byte[followMessageOpcodeArr.length + followByteArr.length + numOfUsersByteArr.length
                + userNameListByteArr.length];
        copyFromTo(byteMessageArray, followMessageOpcodeArr, 0);
        copyFromTo(byteMessageArray, followByteArr, followMessageOpcodeArr.length);
        copyFromTo(byteMessageArray, numOfUsersByteArr, followMessageOpcodeArr.length + followByteArr.length);
        copyFromTo(byteMessageArray, userNameListByteArr, followMessageOpcodeArr.length + followByteArr.length +
                numOfUsersByteArr.length);
        String message = new String(byteMessageArray, 0, byteMessageArray.length);
        System.out.println("The message string is : " + message);
        System.out.println(" the byte array : " + Arrays.toString(byteMessageArray));

        /*/////////////////////////////////////////////////*/
        for (byte bit : byteMessageArray
        ) {
            messageEncoderDecoder.decodeNextByte(bit);
        }


    }


    public static void copyFromTo(byte[] arr, byte toCopy[], int from) {
        for (int i = 0; i < toCopy.length; i++) {
            arr[i + from] = toCopy[i];
        }
    }


    public static byte[] shortToBytes(short num) {
        byte[] bytesArr = new byte[2];
        bytesArr[0] = (byte) ((num >> 8) & 0xFF);
        bytesArr[1] = (byte) (num & 0xFF);
        return bytesArr;
    }


}
