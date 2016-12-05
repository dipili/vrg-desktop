package com.github.diplombmstu.vrg.communication.events;

import javax.websocket.CloseReason;

/**
 * TODO add comment
 */
public class WebSocketCloseEvent
{
    private CloseReason reason;

    public WebSocketCloseEvent(CloseReason reason)
    {
        this.reason = reason;
    }

    public CloseReason getReason()
    {
        return reason;
    }
}
