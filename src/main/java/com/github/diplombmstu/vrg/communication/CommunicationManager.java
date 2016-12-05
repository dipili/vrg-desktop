package com.github.diplombmstu.vrg.communication;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.websocket.server.ServerContainer;
import java.util.logging.Logger;

/**
 * TODO add comment
 */
public class CommunicationManager
{
    private static final Logger LOGGER = Logger.getLogger(CommunicationManager.class.getName());
    private Server server;

    public void start(int port) throws Exception
    {
        LOGGER.warning("Starting communication server.");
        server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServerContainer serverContainer = WebSocketServerContainerInitializer.configureContext(context);
        serverContainer.addEndpoint(ServletCommunicationEndpoint.class);

        // Wrapping servlet endpoint
        CommunicationEntry entry = new CommunicationEntry();
        WebsocketEventRouter router = new WebsocketEventRouter(ServletCommunicationEndpoint.EVENT_BUS, entry);
        router.start();

        server.start();
        LOGGER.info(server.dump());
    }

    public void stop() throws Exception
    {
        LOGGER.warning("Stopping communication server.");
        server.stop();
        server.destroy();
        server = null;
    }
}
