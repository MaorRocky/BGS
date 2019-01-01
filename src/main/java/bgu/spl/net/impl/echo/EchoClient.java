package bgu.spl.net.impl.echo;

import bgu.spl.net.api.bidi.BidiMessagingProtocol;
import bgu.spl.net.srv.BidiMessagingProtocolImpl;
import bgu.spl.net.srv.DataBase;
import bgu.spl.net.srv.MessageEncoderDecoderImpl;
import bgu.spl.net.srv.Server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class EchoClient {

    public static void main(String[] args) throws IOException {

        //DataBase dataBase = new DataBase();
        Server.threadPerClient(7777,
                () -> new BidiMessagingProtocolImpl(),
                () -> new MessageEncoderDecoderImpl()
        ).serve();
    }
}
