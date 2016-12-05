package com.github.diplombmstu.vrg.communication;

import com.github.diplombmstu.vrg.communication.events.WebSocketCloseEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketConnectEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketErrorEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketTextEvent;
import com.google.common.eventbus.EventBus;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;

/**
 * WebSocket server jetty endpoint
 */
@ClientEndpoint
@ServerEndpoint(value = "/communication/")
public class ServletCommunicationEndpoint extends PerfectWebSocketAdapter
{
    static final EventBus EVENT_BUS = new EventBus();

    @OnClose
    @Override
    public void onWebSocketClose(CloseReason reason)
    {
        EVENT_BUS.post(new WebSocketCloseEvent(reason));
    }

    @OnError
    @Override
    public void onWebSocketError(Throwable cause)
    {
        EVENT_BUS.post(new WebSocketErrorEvent(cause));
    }

    @OnMessage
    @Override
    public void onWebSocketText(String message)
    {
        EVENT_BUS.post(new WebSocketTextEvent(message));
    }

    @OnOpen
    @Override
    public void onWebSocketConnect(Session session)
    {
        EVENT_BUS.post(new WebSocketConnectEvent(session));
    }
}