package maxbacon.parser;

/**
 * The essential stock quote
 */
public class Quote {
   public final long   time;
   public final double close;
   public final double high;
   public final double low;
   public final double open;
   public final long   volume;

   public Quote(long time, double close, double high, double low, double open, long volume) {
      this.time = time;
      this.close = close;
      this.high = high;
      this.low = low;
      this.open = open;
      this.volume = volume;
   }
}
