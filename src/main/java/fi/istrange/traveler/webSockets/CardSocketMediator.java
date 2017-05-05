package fi.istrange.traveler.webSockets;

import javax.websocket.server.ServerEndpoint;
/**
 * Created by aleksandr on 16.4.2017.
 */
@ServerEndpoint("/cardSocket/{client-id}")
public class CardSocketMediator extends AbstractSocketMediator {

}

