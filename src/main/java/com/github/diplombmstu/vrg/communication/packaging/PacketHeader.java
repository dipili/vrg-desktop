package com.github.diplombmstu.vrg.communication.packaging;

import java.util.UUID;

/**
 * TODO add comment
 */
public class PacketHeader
{
    private PacketType type;

    public PacketType getType()
    {
        return type;
    }

    public void setType(PacketType type)
    {
        this.type = type;
    }
}
