package com.github.diplombmstu.vrg.common;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Helper for handling exceptions.
 */
public class ExceptionUtils
{
    public static String buildStackTrace(Throwable exception)
    {
        StringWriter stringWriter = new StringWriter();
        PrintWriter printWriter = new PrintWriter(stringWriter);
        exception.printStackTrace(printWriter);
        return stringWriter.toString();
    }
}
