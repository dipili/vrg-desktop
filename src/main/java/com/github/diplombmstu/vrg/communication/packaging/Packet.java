package com.github.diplombmstu.vrg.communication.packaging;

import com.github.diplombmstu.vrg.communication.packaging.bodies.PacketBody;

/**
 * TODO add comment
 */
public class Packet
{
    private PacketHeader header;
    private PacketBody body;

    public PacketBody getBody()
    {
        return body;
    }

    public void setBody(PacketBody body)
    {
        this.body = body;
    }

    public PacketHeader getHeader()
    {
        return header;
    }

    public void setHeader(PacketHeader header)
    {
        this.header = header;
    }
}
