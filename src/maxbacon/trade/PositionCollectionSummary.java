package maxbacon.trade;

import java.util.Collection;

import maxbacon.images.SimpleRiskGraph.RiskBars;

public class PositionCollectionSummary {
   public static RiskBars assessRiskOfPositions(Collection<Position> positions, double last) {
      double total = 0;
      int happy = 0;
      int winning = 0;
      int passiveAggressive = 0;
      int losing = 0;
      for (Position p : positions) {
         total++;
         if (p.happy())
            happy++;
         if (p.winning(last))
            winning++;
         if (p.passiveAggressive(last))
            passiveAggressive++;
         if (p.losing(last))
            losing++;
      }
      return new RiskBars(happy / total, winning / total, passiveAggressive / total, losing / total);
   }
}
