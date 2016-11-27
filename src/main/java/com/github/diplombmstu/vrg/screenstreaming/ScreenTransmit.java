package com.github.diplombmstu.vrg.screenstreaming;

import java.net.SocketException;

/**
 * TODO add comment
 */
public class ScreenTransmit
{
    public static void main(String[] args) throws Exception
    {
        new Thread(() ->
                   {
                       ScreenStreamer streamer = new ScreenStreamer();

                       try
                       {
                           streamer.start();
                       }
                       catch (SocketException e)
                       {
                           e.printStackTrace();
                       }
                   }).start();

        for (;;)
        {
        }
    }
}
