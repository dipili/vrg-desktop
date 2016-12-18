package com.github.diplombmstu.vrg.communication;

import com.github.diplombmstu.vrg.common.VrgCommons;
import com.github.diplombmstu.vrg.communication.events.WebSocketConnectEvent;
import com.github.diplombmstu.vrg.communication.packaging.bodies.SetImageCommand;
import com.github.diplombmstu.vrg.communication.senders.CommandSender;
import com.github.diplombmstu.vrg.communication.syncronization.VrgSynchroniseSpamer;
import com.google.common.eventbus.Subscribe;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * TODO add comment
 */
public class CommunicationManager
{
    private static final Logger LOGGER = Logger.getLogger(CommunicationManager.class.getName());
    private Server server;
    private VrgSynchroniseSpamer synchroniseSpamer;
    private CommandSender commandSender;

    public CommandSender getCommandSender()
    {
        return commandSender;
    }

    public void start() throws Exception
    {
        this.server = createCommunicationServer(VrgCommons.COMMUNICATION_SERVER_PORT);

        ServletCommunicationEndpoint.EVENT_BUS.register(this);

        // Wrapping servlet endpoint
        CommunicationEntry entry = new CommunicationEntry();
        WebsocketEventRouter router = new WebsocketEventRouter(ServletCommunicationEndpoint.EVENT_BUS, entry);
        router.start();

        commandSender = new CommandSender(entry.getSessionInstance());

        synchroniseSpamer = new VrgSynchroniseSpamer();
        synchroniseSpamer.start(VrgCommons.SYNC_PORT);

        LOGGER.warning("Starting communication server.");
        this.server.start();
    }

    @Subscribe
    public void onClientConnect(WebSocketConnectEvent event)
    {
        try
        {
            LOGGER.info("Sending image");
            commandSender.send(new SetImageCommand("test_image.jpg"));
        }
        catch (IOException e)
        {
            e.printStackTrace(); // TODO handle
        }
    }

    public void stop() throws Exception
    {
        LOGGER.warning("Stopping communication server.");
        synchroniseSpamer.stop();
        server.stop();
        server.destroy();
    }

    private Server createCommunicationServer(int port) throws ServletException, DeploymentException
    {
        Server server = new Server();

        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.addConnector(connector);

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);

        ServerContainer serverContainer = WebSocketServerContainerInitializer.configureContext(context);
        serverContainer.addEndpoint(ServletCommunicationEndpoint.class);

        LOGGER.info(server.dump());
        return server;
    }
}
