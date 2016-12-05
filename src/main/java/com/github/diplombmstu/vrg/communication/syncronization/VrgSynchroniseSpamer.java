package com.github.diplombmstu.vrg.communication.syncronization;

import com.github.diplombmstu.vrg.common.VrgCommons;

import java.net.*;
import java.util.Timer;
import java.util.TimerTask;

/**
 * TODO add comment
 */
public class VrgSynchroniseSpamer
{
    private static final int SPAM_PERIOD = 500;
    private DatagramSocket socket;
    private Timer timer;

    public void start(int port) throws UnknownHostException, SocketException
    {
        socket = createSocket(port);
        timer = new Timer();
        timer.scheduleAtFixedRate(new SendMessageTask(VrgCommons.SYNC_CODE), 0, SPAM_PERIOD);
    }

    public void stop()
    {
        socket.close();
        timer.cancel();
    }

    private DatagramSocket createSocket(int port) throws SocketException, UnknownHostException
    {
        DatagramSocket socket = new DatagramSocket(port);
        socket.setBroadcast(true);
        socket.connect(InetAddress.getByName("255.255.255.255"), port);
        return socket;
    }

    private class SendMessageTask extends TimerTask
    {
        private String message;

        SendMessageTask(String message)
        {
            this.message = message;
        }

        @Override
        public void run()
        {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

            try
            {
                socket.send(packet);
            }
            catch (Exception e)
            {
                System.err.println("Sending failed. " + e.getMessage());
            }
        }
    }
}
