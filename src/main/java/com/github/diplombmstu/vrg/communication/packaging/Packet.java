package com.github.diplombmstu.vrg.communication.packaging;

/**
 * TODO add comment
 */
public class Packet
{
    private PacketHeader header;
    private Object body;

    public Object getBody()
    {
        return body;
    }

    public void setBody(Object body)
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
