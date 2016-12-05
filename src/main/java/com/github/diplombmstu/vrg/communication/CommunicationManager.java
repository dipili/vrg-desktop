package com.github.diplombmstu.vrg.communication;

import com.github.diplombmstu.vrg.common.VrgCommons;
import com.github.diplombmstu.vrg.communication.syncronization.VrgSynchroniseSpamer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.websocket.jsr356.server.deploy.WebSocketServerContainerInitializer;

import javax.servlet.ServletException;
import javax.websocket.DeploymentException;
import javax.websocket.server.ServerContainer;
import java.util.logging.Logger;

/**
 * TODO add comment
 */
public class CommunicationManager
{
    private static final Logger LOGGER = Logger.getLogger(CommunicationManager.class.getName());
    private Server server;
    private VrgSynchroniseSpamer synchroniseSpamer;

    public void start() throws Exception
    {
        this.server = createCommunicationServer(VrgCommons.COMMUNICATION_SERVER_PORT);

//        ServletCommunicationEndpoint.EVENT_BUS.register(this);// TODO remove or uncomment

        // Wrapping servlet endpoint
        CommunicationEntry entry = new CommunicationEntry();
        WebsocketEventRouter router = new WebsocketEventRouter(ServletCommunicationEndpoint.EVENT_BUS, entry);
        router.start();

        synchroniseSpamer = new VrgSynchroniseSpamer();
        synchroniseSpamer.start(VrgCommons.SYNC_PORT);

        LOGGER.warning("Starting communication server.");
        this.server.start();
    }

    // TODO remove or uncomment
//    @Subscribe
//    public void onClientConnect(WebSocketConnectEvent event)
//    {
//        synchroniseSpamer.stop();
//    }
//
//    @Subscribe
//    public void onClientDisconnect(WebSocketCloseEvent event)
//    {
//        try
//        {
//            synchroniseSpamer.start(VrgCommons.SYNC_PORT);
//        }
//        catch (UnknownHostException | SocketException e)
//        {
//            e.printStackTrace(); // TODO remove or uncomment
//        }
//    }

    public void stop() throws Exception
    {
        LOGGER.warning("Stopping communication server.");
//        ServletCommunicationEndpoint.EVENT_BUS.unregister(this);
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
