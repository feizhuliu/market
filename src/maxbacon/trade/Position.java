package maxbacon.trade;

public class Position {
   public final double bid;
   public final double ask;

   private long        boughtAt = -1;
   private long        soldAt   = -1;

   public Position(double bid, double ask) {
      this.bid = bid;
      this.ask = ask;
   }

   private boolean bought() {
      return boughtAt > 0;
   }

   private boolean sold() {
      return soldAt > 0;
   }

   public void update(long at, double quote) {
      if (!bought()) {
         if (quote <= bid) {
            boughtAt = at;
            return;
         }
      }

      if (bought() && !sold()) {
         if (quote >= ask) {
            soldAt = at;
            return;
         }
      }
   }
   
   // our position could have made us money if we had only executed it
   public boolean passiveAggressive(double close) {
      return !bought() && close >= ask;
   }
   
   // the position made us money
   public boolean happy() {
      return bought() && sold();
   }

   // the position is losing us money now
   public boolean losing(double close) {
      return bought() && !sold() && bid >= close;
   }

   // the position is making us money, but it is open
   public boolean winning(double close) {
      return bought() && !sold() && bid < close;
   }
}
