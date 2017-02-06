package com.github.diplombmstu.vrg.communication.syncronization;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.TimerTask;

/**
 * TODO add comment
 */
class SendMessageTask extends TimerTask
{
    private final DatagramSocket socket;
    private String message;

    SendMessageTask(String message, DatagramSocket socket)
    {
        this.message = message;
        this.socket = socket;
    }

    @Override
    public void run()
    {
        try
        {
            byte[] buffer = message.getBytes();
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            socket.send(packet);
        }
        catch (Exception e)
        {
            System.err.println("Sending failed. " + e.getMessage());
        }
    }
}
