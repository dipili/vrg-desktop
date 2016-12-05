package com.github.diplombmstu.vrg.communication.events;

/**
 * TODO add comment
 */
public class WebSocketErrorEvent
{
    private Throwable cause;

    public WebSocketErrorEvent(Throwable cause)
    {
        this.cause = cause;
    }

    public Throwable getCause()
    {
        return cause;
    }
}
