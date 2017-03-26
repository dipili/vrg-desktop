package com.github.diplombmstu.converter;

import java.io.IOException;
import java.nio.file.Path;

/**
 * TODO add comment
 */
public interface Converter
{
    void convert(Path input, Path output) throws IOException;
}
