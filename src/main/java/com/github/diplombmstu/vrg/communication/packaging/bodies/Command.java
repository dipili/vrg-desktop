package com.github.diplombmstu.vrg.communication.packaging.bodies;

/**
 * TODO add comment
 */
public interface Command<T> extends PacketBody
{
    int getCode();

    T getBody();
}
