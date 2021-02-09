package tool;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

public class DrawDigit {
    final private static int col = 28, row = 28;
    public static void draw(String path, double[] res) throws Exception {
        BufferedImage bi = new BufferedImage(row, col, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D g2 = (Graphics2D) bi.getGraphics();

        for (int i = 0; i < row; i++)
            for (int j = 0; j < col; j++) {
                double gray = res[i * 28 + j];
                g2.setColor(new Color((int) (gray * 255), (int) (gray * 255), (int) (gray * 255)));
                g2.fillRect(j, i, 1, 1);
            }

        ImageIO.write(bi, "JPEG", new FileOutputStream(path));
    }

    public static void main(String[] args) throws Exception {
        draw("", new double[] {});
    }
}
