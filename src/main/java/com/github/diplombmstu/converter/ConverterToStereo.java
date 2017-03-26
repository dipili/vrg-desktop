package com.github.diplombmstu.converter;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.file.Path;

/**
 * TODO add comment
 */
public class ConverterToStereo implements Converter
{
    private static final int STEREO_IMAGE_SIZE = 4096;

    @Override
    public void convert(Path input, Path output) throws IOException
    {
        BufferedImage inputImage = readImage(input);
        BufferedImage outputImage = new BufferedImage(STEREO_IMAGE_SIZE,
                                                      STEREO_IMAGE_SIZE,
                                                      BufferedImage.TYPE_INT_ARGB);

        insertImageIntoImage(inputImage,
                             outputImage,
                             STEREO_IMAGE_SIZE / 2 - inputImage.getWidth() / 2,
                             STEREO_IMAGE_SIZE - (STEREO_IMAGE_SIZE / 4 + inputImage.getHeight() / 2));

        insertImageIntoImage(inputImage,
                             outputImage,
                             STEREO_IMAGE_SIZE / 2 - inputImage.getWidth() / 2,
                             STEREO_IMAGE_SIZE - (STEREO_IMAGE_SIZE / 4 * 3 + inputImage.getHeight() / 2));

        File outputFile = output.toFile();
        ImageIO.write(outputImage, "png", outputFile);
    }

    private void insertImageIntoImage(BufferedImage source, BufferedImage target, int x, int y)
    {
        for (int i = 0; i < source.getWidth(); i++)
        {
            for (int j = 0; j < source.getHeight(); j++)
            {
                int targetX = x + i;
                int targetY = y + j;

                target.setRGB(targetX, targetY, source.getRGB(i, j));
            }
        }
    }

    private BufferedImage readImage(Path imagePath) throws IOException
    {
        try (FileInputStream fis = new FileInputStream(imagePath.toFile());
             FileChannel channel = fis.getChannel();
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream())
        {
            channel.transferTo(0, channel.size(), Channels.newChannel(byteArrayOutputStream));
            return ImageIO.read(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
        }
    }
}
