package maxbacon.images;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;

public class RiskPlotter {
   private static final int WHITE  = Color.WHITE.getRGB();
   private static final int GREEN  = Color.GREEN.getRGB();
   private static final int RED    = Color.RED.getRGB();
   private static final int ORANGE = Color.ORANGE.getRGB();

   public static enum RiskLevel {
      Unknown(WHITE),
      Green(GREEN),
      Orange(ORANGE),
      Red(RED);

      private int color;

      RiskLevel(final int color) {
         this.color = color;
      }
   }

   public static interface RiskQuery {
      public RiskLevel get(double x, double y, int iX, int iY);
   }

   public static void plot(File file, RiskQuery query, int width, int height) throws Exception {
      BufferedImage img = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
      double iw = 1.0 / (double) width;
      double ih = 1.0 / (double) height;
      for (int y = 0; y < height; y++) {
         for (int x = 0; x < width; x++) {
            RiskLevel level = query.get(x * iw, (height - 1 - y) * ih, x, y);
            img.setRGB(x, y, level.color);
         }
      }
      ImageIO.write(img, "png", file);
   }
}
