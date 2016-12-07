package com.github.diplombmstu.vrg.communication.packaging.bodies;

import java.util.UUID;

/**
 * TODO add comment
 */
abstract public class Request
{
    private UUID uuid;

    public UUID getUuid()
    {
        return uuid;
    }

    public void setUuid(UUID uuid)
    {
        this.uuid = uuid;
    }
}
