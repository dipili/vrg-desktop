package com.github.diplombmstu.vrg.communication;

import com.github.diplombmstu.vrg.common.ExceptionHelper;
import com.github.diplombmstu.vrg.communication.events.WebSocketCloseEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketConnectEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketErrorEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketTextEvent;
import com.google.common.eventbus.EventBus;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.util.logging.Logger;

/**
 * WebSocket server jetty endpoint
 */
@ClientEndpoint
@ServerEndpoint(value = "/communication/")
public class ServletCommunicationEndpoint extends WebSocketAdapter
{
    static final EventBus EVENT_BUS = new EventBus();

    @OnClose
    public void onWebSocketClose(int statusCode, String reason)
    {
        EVENT_BUS.post(new WebSocketCloseEvent(statusCode, reason));
    }

    @OnOpen
    public void onWebSocketConnect(Session session)
    {
        EVENT_BUS.post(new WebSocketConnectEvent(session));
    }

    @OnError
    public void onWebSocketError(Throwable cause)
    {
        EVENT_BUS.post(new WebSocketErrorEvent(cause));
    }

    @OnMessage
    public void onWebSocketText(String message)
    {
        EVENT_BUS.post(new WebSocketTextEvent(message));
    }
}