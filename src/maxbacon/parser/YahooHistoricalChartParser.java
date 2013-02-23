package maxbacon.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.regex.Pattern;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

/**
 * Parsing Yahoo's long term daily format
 */
public class YahooHistoricalChartParser implements Iterator<Quote> {
   private static final String  COMMA = Pattern.quote(",");

   private final BufferedReader reader;
   private String               ln;

   public static Iterable<Quote> ofString(final String contents) {
      return new Iterable<Quote>() {
         public Iterator<Quote> iterator() {
            try {
               return new YahooHistoricalChartParser(new ByteArrayInputStream(contents.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
               throw new RuntimeException(e);
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
         }
      };
   }

   public YahooHistoricalChartParser(InputStream input) throws IOException {
      this.reader = new BufferedReader(new InputStreamReader(input));
      ln = reader.readLine();
      String expected = "Date,Open,High,Low,Close,Volume,Adj Close";
      if (!expected.equalsIgnoreCase(ln)) {
         throw new IOException("The first line was not expected. got:[" + ln + "] expected:[" + expected + "]");
      }
      ln = reader.readLine();
   }

   @Override
   public boolean hasNext() {
      return ln != null;
   }

   private long day(int month, int day, int year) {
      return new DateTime(year, month, day, 0, 0).toDateTime(DateTimeZone.UTC).withMillisOfDay(0).getMillis();
   }

   private long parseDate(String x) {
      String[] d = x.split(Pattern.quote("-"));
      return day(Integer.parseInt(d[1]), Integer.parseInt(d[2]), Integer.parseInt(d[0]));
   }

   @Override
   public Quote next() {
      String[] columns = ln.split(COMMA);
      try {
         ln = reader.readLine();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      long time = parseDate(columns[0]);
      double open = Double.parseDouble(columns[1]);
      double high = Double.parseDouble(columns[2]);
      double low = Double.parseDouble(columns[3]);
      double close = Double.parseDouble(columns[4]);
      long volume = Long.parseLong(columns[5]);
      return new Quote(time, close, high, low, open, volume);
   }

   @Override
   public void remove() {
   }
}
