package com.github.diplombmstu.vrg.communication.senders;

import javax.websocket.Session;

/**
 * TODO add comment
 */
public class Sender
{
    private Session session;

    public Sender(Session session)
    {
        this.session = session;
    }

    protected Session getSession()
    {
        return session;
    }
}