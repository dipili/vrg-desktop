package com.github.diplombmstu.vrg.communication.events;

import javax.websocket.Session;

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
