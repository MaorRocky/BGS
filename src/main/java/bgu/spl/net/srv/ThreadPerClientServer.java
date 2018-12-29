import bgu.spl.net.api.MessageEncoderDecoder;
import bgu.spl.net.api.MessagingProtocol;
import bgu.spl.net.srv.BaseServer;
import bgu.spl.net.srv.bidi.ConnectionHandler;

import java.util.function.Supplier;

public class ThreadPerClientServer extends BaseServer {
 
    public ThreadPerClientServer(
            int port,
            Supplier<MessagingProtocol> protocolFactory,
            Supplier<MessageEncoderDecoder> encoderDecoderFactory) {
 
        super(port, protocolFactory, encoderDecoderFactory);
    }
 
    @Override
    protected void execute(ConnectionHandler handler) {
        new Thread(handler).start();
    }
 
}