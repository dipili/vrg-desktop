package com.github.diplombmstu.vrg.communication;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import java.util.logging.Logger;

/**
 * TODO add comment
 */
public class CommunicationEntry extends WebSocketAdapter
{
    private static final Logger LOGGER = Logger.getLogger(CommunicationEntry.class.getName());

    @OnClose
    public void onWebSocketClose(int statusCode, String reason)
    {
        super.onWebSocketClose(statusCode, reason);
    }

    @OnOpen
    public void onWebSocketConnect(Session session)
    {
        super.onWebSocketConnect(session);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
    }

    @OnMessage
    public void onWebSocketText(String message)
    {
        System.out.println(message);
    }
}
