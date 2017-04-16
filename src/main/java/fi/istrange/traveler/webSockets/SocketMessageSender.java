package fi.istrange.traveler.webSockets;

import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author aleksandr
 */
public abstract class SocketMessageSender {
    private SocketClient client;
    private final String webSocketAddress;

    public SocketMessageSender(String url) {
        webSocketAddress = url;
    }

    protected void initializeWebSocket() throws URISyntaxException {
        System.out.println("REST service: open websocket client at " + webSocketAddress);
        this.client = new SocketClient(new URI(webSocketAddress + "/0"));
    }
    //TODO: write our own method(s) using this as an example
/*
    protected void sendMessageOverSocket(
            Object task,
            PushType pushType,
            ActionInitiator initiator
    ) {
        if (this.client == null) {
            try {
                initializeWebSocket();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }

        try {
            ObjectMapper mapper = new ObjectMapper();

            PushNotification notification =
                    new PushNotification(pushType, task, initiator);

            //Object to JSON in String
            String jsonInString = mapper.writeValueAsString(notification);

            this.client.sendMessage(jsonInString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
}

