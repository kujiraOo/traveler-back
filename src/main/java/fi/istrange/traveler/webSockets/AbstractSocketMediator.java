package fi.istrange.traveler.webSockets;

/**
 * Created by aleksandr on 16.4.2017.
 */
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 *
 * @author aleksandr
 */
public abstract class AbstractSocketMediator {
    protected static Set<Session> peers = Collections.synchronizedSet(new HashSet());

    @OnMessage
    public String onMessage(
            String message,
            Session session,
            @PathParam("client-id") String clientId
    ) {
        try {
            System.out.println("received message from client " + clientId);
            for (Session s : peers) {
                try {
                    s.getBasicRemote().sendText(message);
                    System.out.println("send message to peer ");
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "message was received by socket mediator and processed: " + message;
    }

    @OnOpen
    public void onOpen(
            Session session,
            @PathParam("client-id") String clientId
    ) {
        peers.add(session);
/*
        try {
            session.getBasicRemote().sendText("good to be in touch");
        } catch (IOException e) {
        } */
    }

    @OnClose
    public void onClose(
            Session session,
            @PathParam("client-id") String clientId
    ) {
        peers.remove(session);
    }
}
