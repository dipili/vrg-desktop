package com.github.diplombmstu.vrg.communication.events;

import org.eclipse.jetty.websocket.api.Session;

/**
 * TODO add comment
 */
public class WebSocketConnectEvent
{
    private Session session;

    public WebSocketConnectEvent(Session session)
    {
        this.session = session;
    }

    public Session getSession()
    {
        return session;
    }
}
