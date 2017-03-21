package com.github.diplombmstu.mixing;

import javax.media.*;

/**
 * TODO add comment
 */
public class RtpReceiver implements ControllerListener
{
    private Player player;

    @Override
    public void controllerUpdate(ControllerEvent controllerEvent)
    {
        System.out.println(controllerEvent);
    }

    public void start()
    {
        try
        {
            String media_url = "rtsp://:8554/audio";
            System.out.println("Receiver URL= " + media_url);
            MediaLocator media_locator = new MediaLocator(media_url);
            player = Manager.createPlayer(media_locator);
            if (player != null)
                player.addControllerListener(this);
            else
                System.out.println("Player cannot be created");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        try
        {
            player.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        if (player == null)
            return;

        player.stop();
        player.deallocate();
        player.close();
        player = null;

        System.out.println("Player stopped");
    }
}
