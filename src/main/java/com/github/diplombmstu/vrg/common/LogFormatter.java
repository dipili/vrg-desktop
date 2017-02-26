package com.github.diplombmstu.vrg.common;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Custom LOG formatter.
 */
public class LogFormatter extends Formatter
{
    private SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");

    public LogFormatter()
    {
        super();
    }

    public String format(LogRecord record)
    {
        StringBuilder stringBuffer = new StringBuilder();

        Date date = new Date(record.getMillis());
        stringBuffer.append(String.format("[%s]", record.getLevel().getName()));
        stringBuffer.append(" ");

        stringBuffer.append(dateFormat.format(date));
        stringBuffer.append(" ");

        stringBuffer.append(record.getLoggerName());
        stringBuffer.append(" : ");

        stringBuffer.append(formatMessage(record));
        stringBuffer.append("\n");

        return stringBuffer.toString();
    }
}