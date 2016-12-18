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
        socket = createSocket();
        timer = new Timer();
        timer.scheduleAtFixedRate(new SendMessageTask(VrgCommons.SYNC_CODE, port), 0, SPAM_PERIOD);
    }

    public void stop()
    {
        socket.close();
        timer.cancel();
    }

    private DatagramSocket createSocket() throws SocketException, UnknownHostException
    {
        DatagramSocket socket = new DatagramSocket();
        socket.setBroadcast(true);
//        socket.connect(InetAddress.getByName("255.255.255.255"), port);
        return socket;
    }

    private class SendMessageTask extends TimerTask
    {
        private final int port;
        private String message;

        SendMessageTask(String message, int port)
        {
            this.message = message;
            this.port = port;
        }

        @Override
        public void run()
        {
            try
            {
                byte[] buffer = message.getBytes();
                DatagramPacket packet;

                packet = new DatagramPacket(buffer, buffer.length, InetAddress.getByName("192.168.0.255"), port);
                socket.send(packet);
            }
            catch (Exception e)
            {
                System.err.println("Sending failed. " + e.getMessage());
            }
        }
    }
}
