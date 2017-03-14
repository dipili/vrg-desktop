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
            String media_url = "rtp://192.168.1.112:8664/audio";
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
            System.out.println("Trying to realize the player");
            player.realize();
            while (player.getState() != Controller.Realized)
                Thread.sleep(100);

            System.out.println("Player realized");
            player.start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
