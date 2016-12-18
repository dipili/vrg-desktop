package com.github.diplombmstu.vrg.communication.senders;

import com.github.diplombmstu.vrg.communication.packaging.bodies.Command;
import com.github.diplombmstu.vrg.communication.packaging.bodies.CommandHeader;
import com.google.gson.Gson;

import javax.websocket.Session;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * TODO add comment
 */
public class CommandSender extends Sender
{
    public CommandSender(Session session)
    {
        super(session);
    }

    public void send(Command<?> command) throws IOException
    {
        CommandHeader commandHeader = new CommandHeader(command.getCode());
        String jsonString = new Gson().toJson(commandHeader);
        getSession().getBasicRemote().sendText(jsonString);

        byte[] body = (byte[]) command.getBody();
        ByteBuffer byteBuffer = ByteBuffer.wrap(body);
        getSession().getBasicRemote().sendBinary(byteBuffer);
    }
}
