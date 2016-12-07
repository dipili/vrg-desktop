package com.github.diplombmstu.vrg.communication.packaging;

/**
 * TODO add comment
 * TODO add serializer
 */
public enum PacketType
{
    Dummy(-1), Request(0);

    private final int id;

    PacketType(int id)
    {
        this.id = id;
    }

    @Override
    public String toString()
    {
        return String.valueOf(id);
    }
}
