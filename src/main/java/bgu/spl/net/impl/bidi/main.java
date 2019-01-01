package bgu.spl.net.impl.bidi;

import bgu.spl.net.srv.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.MessageEncoderDecoderImpl;
import bgu.spl.net.srv.Server;

public class main {
    public static void main(String[] args) {

        //DataBase dataBase = new DataBase();
        Server.threadPerClient(7777,
                () -> new BidiMessagingProtocolImpl(),
                () -> new MessageEncoderDecoderImpl()
        ).serve();
    }
}