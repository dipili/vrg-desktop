package com.github.diplombmstu.converter;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * TODO add comment
 */
public class Converter3d
{
    private double b = 6.8;
    private double wsm = 50;
    private double wpx = 1920;

    public Converter3d(double b, double wsm, double wpx)
    {
        this.b = b;
        this.wsm = wsm;
        this.wpx = wpx;
    }

    public void convert(Path input, Path depthMap, Path output) throws Exception
    {
        BufferedImage inputImage = Utils.readImage(input);
        BufferedImage depthMapImage = Utils.readImage(depthMap);

        int inputWidth = inputImage.getWidth();
        int inputHeight = inputImage.getHeight();

        if (inputHeight != depthMapImage.getHeight() && inputWidth != depthMapImage.getWidth())
            throw new Exception("Images have different sizes.");

        BufferedImage leftImage = Utils.createSameSizeImage(inputImage);
        BufferedImage rightImage = Utils.createSameSizeImage(inputImage);

        Map<Double, List<Pair<Integer, Integer>>> depths = findDepths(inputImage, depthMapImage);
        Set<Double> sortedKeys = depths.keySet()
                .stream()
                .sorted((o1, o2) -> o1 > o2 ? 1 : (o1.equals(o2) ? 0 : -1))
                .collect(Collectors.toSet());

        for (Double sortedKey : sortedKeys)
        {
            for (Pair<Integer, Integer> coordinates : depths.get(sortedKey))
                shift(inputImage, depthMapImage, leftImage, rightImage, coordinates);
        }

        int outputImageWidth = leftImage.getWidth();
        int outputImageHeight = leftImage.getHeight() * 2;
        BufferedImage outputImage = new BufferedImage(outputImageWidth, outputImageHeight, BufferedImage.TYPE_INT_ARGB);

        insertImageIntoImage(leftImage,
                             outputImage,
                             outputImageWidth / 2 - inputImage.getWidth() / 2,
                             outputImageHeight - (outputImageHeight / 4 + inputImage.getHeight() / 2));

        insertImageIntoImage(rightImage,
                             outputImage,
                             outputImageWidth / 2 - inputImage.getWidth() / 2,
                             outputImageHeight - (outputImageHeight / 4 * 3 + inputImage.getHeight() / 2));

        ImageIO.write(outputImage, "png", output.toFile());
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

    private void shift(BufferedImage inputImage,
                       BufferedImage depthMapImage,
                       BufferedImage leftImage,
                       BufferedImage rightImage,
                       Pair<Integer, Integer> coordinates)
    {
        int inputWidth = inputImage.getWidth();

        int i = coordinates.getKey();
        int j = coordinates.getValue();

        double L = new Color(depthMapImage.getRGB(i, j)).getRed();

        double Psm = b * (1 - L / 127.5);
        double Ppx = wsm / wpx / Psm;

        int lx = (int) (i + Ppx / 2);
        int rx = (int) (i - Ppx / 2);

        int inputColor = inputImage.getRGB(i, j);

        if (lx < inputWidth)
        {
            leftImage.setRGB(lx, j, inputColor);
            leftImage.setRGB(i, j, rx >= 0 ? inputImage.getRGB(rx, j) : inputImage.getRGB(i, j));
        }

        if (rx >= 0)
        {
            rightImage.setRGB(rx, j, inputColor);
            rightImage.setRGB(i, j, lx < inputWidth ? inputImage.getRGB(lx, j) : inputImage.getRGB(i, j));
        }
    }

    private Map<Double, List<Pair<Integer, Integer>>> findDepths(BufferedImage inputImage, BufferedImage depthMapImage)
    {
        int inputWidth = inputImage.getWidth();
        int inputHeight = inputImage.getHeight();

        Map<Double, List<Pair<Integer, Integer>>> result = new HashMap<>();

        for (int i = 0; i < inputWidth; i++)
        {
            for (int j = 0; j < inputHeight; j++)
            {
                double depthRed = new Color(depthMapImage.getRGB(i, j)).getRed();

                if (!result.containsKey(depthRed))
                {
                    ArrayList<Pair<Integer, Integer>> values = new ArrayList<>();
                    values.add(new Pair<>(i, j));
                    result.put(depthRed, values);
                }
                else
                    result.get(depthRed).add(new Pair<>(i, j));
            }
        }

        return result;
    }
}
