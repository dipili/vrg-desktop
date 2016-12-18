package com.github.diplombmstu.vrg.communication.packaging.bodies;

/**
 * TODO add comment
 */
public class CommandHeader implements Command<Object>
{
    private int code;

    public CommandHeader(int code)
    {
        this.code = code;
    }

    @Override
    public int getCode()
    {
        return code;
    }

    @Override
    public Object getBody()
    {
        return null;
    }
}
