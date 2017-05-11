package com.github.diplombmstu.vrg.communication.syncronization;

import com.github.diplombmstu.vrg.common.VrgCommons;

import java.io.IOException;
import java.net.*;
import java.util.Timer;

/**
 * TODO add comment
 */
public class VrgSynchroniseSpamer
{
    private static final int SPAM_PERIOD = 500;
    private DatagramSocket socket;
    private Timer timer;

    public void start(int port) throws IOException
    {
        socket = createSocket(port);
        timer = new Timer();

        SendMessageTask task = new SendMessageTask(VrgCommons.SYNC_CODE, socket);
        timer.scheduleAtFixedRate(task, 0, SPAM_PERIOD);
    }

    public void stop()
    {
        socket.close();
        timer.cancel();
    }

    private DatagramSocket createSocket(int port) throws IOException
    {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);

        return socket;
    }
}
