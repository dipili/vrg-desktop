package com.github.diplombmstu.vrg.communication.packaging.bodies;

/**
 * TODO add comment
 */
public interface PacketBody<T>
{
    T getBody();

    boolean isBinary();
}
