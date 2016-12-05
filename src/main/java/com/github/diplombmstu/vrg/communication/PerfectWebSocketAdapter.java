package com.github.diplombmstu.vrg.communication;

import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import javax.websocket.CloseReason;
import javax.websocket.Session;

/**
 * TODO add comment
 */
public abstract class PerfectWebSocketAdapter extends WebSocketAdapter
{
    abstract public void onWebSocketConnect(Session session);
    abstract public void onWebSocketClose(CloseReason reason);
}
