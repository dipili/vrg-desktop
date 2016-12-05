package com.github.diplombmstu.vrg.communication.events;

/**
 * TODO add comment
 */
public class WebSocketTextEvent
{
    private String message;

    public WebSocketTextEvent(String message)
    {
        this.message = message;
    }

    public String getMessage()
    {
        return message;
    }
}
