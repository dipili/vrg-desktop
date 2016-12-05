package com.github.diplombmstu.vrg.communication.events;

/**
 * TODO add comment
 */
public class WebSocketCloseEvent
{
    private int statusCode;
    private String reason;

    public WebSocketCloseEvent(int statusCode, String reason)
    {
        this.statusCode = statusCode;
        this.reason = reason;
    }

    public int getStatusCode()
    {
        return statusCode;
    }

    public String getReason()
    {
        return reason;
    }
}
