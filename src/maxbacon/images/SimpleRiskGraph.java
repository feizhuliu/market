package maxbacon.images;

import java.io.File;
import java.util.HashMap;

import maxbacon.images.RiskPlotter.RiskLevel;
import maxbacon.images.RiskPlotter.RiskQuery;

public class SimpleRiskGraph {

   public static class RiskBars {
      public final double green;
      public final double orange;
      public final double red;

      public RiskBars(double green, double orange, double red) {
         this.green = green;
         this.orange = orange;
         this.red = red;
      }
   }

   public interface SimpleRiskGraphData {
      RiskBars get(double x);
   }

   public static void plot(File file, final SimpleRiskGraphData graph, int width, int height) throws Exception {
      final HashMap<Integer, RiskBars> cache = new HashMap<>();
      final RiskQuery query = new RiskQuery() {
         public RiskLevel get(double x, double y, int iX, int iY) {
            RiskBars bars = cache.get(iX);
            if (bars == null) {
               bars = graph.get(x);
               cache.put(iX, bars);
            }
            double step = bars.green;
            if (y < step)
               return RiskLevel.Green;
            step += bars.orange;
            if (y < step)
               return RiskLevel.Orange;
            step += bars.red;
            if (y < step)
               return RiskLevel.Red;
            return RiskLevel.Unknown;

         }
      };
      RiskPlotter.plot(file, query, width, height);
   }
}
