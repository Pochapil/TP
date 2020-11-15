package ImageProc;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class MedianFilter {

    public static int numberOfElements = 9;
    public static int[] kernel = new int[numberOfElements];
    private static int[] window = new int[numberOfElements];
    private static int[][] pixels;


    private static int CountRGBOFPixel(int pixel) {
        //int red= (RGB>>16)&255;
        //int green= (RGB>>8)&255;
        //int blue= (RGB)&255;
        return (pixel & 0xff) + ((pixel >> 8) & 0xff) + ((pixel >> 16) & 0xff);
    }

    private static int[] SaveRGBOFPixel(int pixel) {
        int[] RGB = new int[3];
        for (int i = 0; i < RGB.length; i++) {
            RGB[i] = ((pixel >> 8 * i) & 0xff);
        }
        return RGB;
    }


    public static BufferedImage getImage() {
        BufferedImage output = null;
        try {
            BufferedImage input = ImageIO.read(new File("D:/ETO BILO NA RABOCHEM STOLE !!!!!/foto (moe)/Новая папка (2)/10.jpg"));
            int pixel;
            pixels = new int[input.getWidth()][input.getHeight()];
            for (int i = 0; i < input.getWidth(); i++) {
                for (int j = 0; j < input.getHeight(); j++) {
                    pixel = input.getRGB(i, j);
                    pixels[i][j] = CountRGBOFPixel(pixel);
                }
            }

            output = applyFilter(input, numberOfElements);
            ImageIO.write(output, "png", new File("filteredImage.png"));

        } catch (IOException e) {
            e.printStackTrace();
        }
        return output;
    }

    public static BufferedImage applyFilter(BufferedImage input, int numberOfElements) {
        BufferedImage output = new BufferedImage(input.getWidth(), input.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        int edge = (kernel.length - 1) / 4;
        for (int i = edge; i < input.getWidth() - edge; i++) {
            for (int j = edge; j < input.getHeight() - edge; j++) {
                applyToAPixel(input, output, i, j);
            }
        }
        return output;
    }

    public static void applyToAPixel(BufferedImage input, BufferedImage output, int x, int y) {
        for (int i = 0; i < (numberOfElements + 1) / 2; i++) {
            window[i] = pixels[x - (numberOfElements - 1) / 4 + i][y];
        }
        window[(numberOfElements + 1) / 2] = pixels[x][y - (numberOfElements - 1) / 4];
        window[(numberOfElements + 1) / 2 + 1] = pixels[x][y - (numberOfElements - 1) / 4 + 1];
        window[(numberOfElements + 1) / 2 + 2] = pixels[x][y - (numberOfElements - 1) / 4 + 3];
        window[(numberOfElements + 1) / 2 + 3] = pixels[x][y - (numberOfElements - 1) / 4 + 4];
        Map<Integer, Integer> pixels = new HashMap<>();
        for (int i = 0; i < window.length; i++) {
            pixels.put(i, window[i]);
        }
        Arrays.sort(window);
        int Avg = window[(numberOfElements - 1) / 2];
        int position = 0;
        for (int i = 1; i < window.length; i++) {
            if (pixels.get(i) == Avg) {
                position = i;
            }
        }
        if (position < 5) {
            output.setRGB(x, y, input.getRGB(x - (numberOfElements - 1) / 4 + position, y));
        } else if (position < 7) {
            output.setRGB(x, y, input.getRGB(x, y - (numberOfElements - 1) / 4 + position - 5));
        } else
            output.setRGB(x, y, input.getRGB(x, y - (numberOfElements - 1) / 4 + position - 4));
    }

}


