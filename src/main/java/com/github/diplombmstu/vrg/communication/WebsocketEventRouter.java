package com.github.diplombmstu.vrg.communication;

import com.github.diplombmstu.vrg.communication.events.WebSocketCloseEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketConnectEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketErrorEvent;
import com.github.diplombmstu.vrg.communication.events.WebSocketTextEvent;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

/**
 * TODO add comment
 */
public class WebsocketEventRouter
{
    private final EventBus eventBus;
    private final WebSocketAdapter webSocketAdapter;

    WebsocketEventRouter(EventBus eventBus, WebSocketAdapter webSocketAdapter)
    {
        this.eventBus = eventBus;
        this.webSocketAdapter = webSocketAdapter;
    }

    public void start()
    {
        eventBus.register(this);
    }

    public void stop()
    {
        eventBus.unregister(this);
    }

    @Subscribe
    public void onWebSocketConnect(WebSocketConnectEvent event)
    {
        webSocketAdapter.onWebSocketConnect(event.getSession());
    }

    @Subscribe
    public void onWebSocketError(WebSocketErrorEvent event)
    {
        webSocketAdapter.onWebSocketError(event.getCause());
    }

    @Subscribe
    public void onWebSocketText(WebSocketTextEvent event)
    {
        webSocketAdapter.onWebSocketText(event.getMessage());
    }

    @Subscribe
    public void onWebSocketClose(WebSocketCloseEvent event)
    {
        webSocketAdapter.onWebSocketClose(event.getStatusCode(), event.getReason());
    }
}
