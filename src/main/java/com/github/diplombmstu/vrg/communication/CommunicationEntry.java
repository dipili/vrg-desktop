package com.github.diplombmstu.vrg.communication;

import com.github.diplombmstu.vrg.common.ExceptionHelper;
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
        LOGGER.info(String.format("Socket Closed: %s", reason));
        super.onWebSocketClose(statusCode, reason);
    }

    @OnOpen
    public void onWebSocketConnect(Session session)
    {
        LOGGER.info(String.format("Socket Connected: %s", session));
        super.onWebSocketConnect(session);
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        LOGGER.severe(String.format("Web socket error. %s", ExceptionHelper.buildStackTrace(cause)));
    }

    @OnMessage
    public void onWebSocketText(String message)
    {
        LOGGER.info(String.format("Received TEXT message: %s", message));
    }
}
