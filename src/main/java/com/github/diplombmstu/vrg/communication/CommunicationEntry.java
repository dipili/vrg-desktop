package com.github.diplombmstu.vrg.communication;

import com.github.diplombmstu.vrg.common.ExceptionHelper;

import javax.websocket.*;
import java.util.logging.Logger;

/**
 * TODO add comment
 */
public class CommunicationEntry extends PerfectWebSocketAdapter
{
    private static final Logger LOGGER = Logger.getLogger(CommunicationEntry.class.getName());
    private Session session;

    public Session getSessionInstance()
    {
        return session;
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

    @OnOpen
    public void onWebSocketConnect(Session session)
    {
        LOGGER.info(String.format("Socket Connected: %s", session));
        this.session = session;
    }

    @OnClose
    @Override
    public void onWebSocketClose(CloseReason reason)
    {
        LOGGER.info(String.format("Socket Closed: %s", reason));
        session = null;
    }
}
