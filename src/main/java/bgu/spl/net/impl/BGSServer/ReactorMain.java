package bgu.spl.net.impl.BGSServer;

import bgu.spl.net.srv.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.MessageEncoderDecoderImpl;
import bgu.spl.net.srv.Server;

public class ReactorMain {
    public static void main(String[] args) {

        DataBase dataBase = new DataBase();
        Server.reactor(Integer.parseInt(args[0]), Integer.parseInt(args[1]),
                () -> new BidiMessagingProtocolImpl(dataBase),
                () -> new MessageEncoderDecoderImpl()
        ).serve();

    }
}
