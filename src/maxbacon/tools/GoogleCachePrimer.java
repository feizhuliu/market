package maxbacon.tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import org.joda.time.DateTime;

import maxbacon.io.CACHE.Getter;
import maxbacon.io.CACHE.Internet;
import maxbacon.io.CACHE.Disk;
import maxbacon.parser.GoogleChartParser;
import maxbacon.urls.URLS;

public class GoogleCachePrimer {
   private static long WAIT_BETWEEN_ASKS = 10 * 1000L;

   public static void main(String[] args) throws Exception {
      // one week ago
      DateTime start = new DateTime().minusDays(7).withMillisOfDay(0);

      // parse and validate the arguments
      if (args.length != 1) {
         System.err.println("Sorry, this requires at least one argument and it must be a file");
         return;
      }
      File path = new File(args[0]);
      if (!path.exists() || !path.isDirectory()) {
         System.err.println("Sorry, the given argument must be a directory");
         return;
      }

      // construct how we talk to the outside world
      Getter internet = new Disk(new Internet(GoogleChartParser.VALIDATE_HTTP), path);

      // read from STDIN
      BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
      String ln;
      while ((ln = console.readLine()) != null) {

         // filter out nonsense
         ln = ln.trim().toUpperCase();
         if (ln.length() == 0)
            continue;
         if (ln.charAt(0) == '#')
            continue;
         if (ln.indexOf(':') < 0) {
            System.err.println("invalid line:" + ln);
            continue;
         }
         String[] exchangeAndSymbol = ln.split(Pattern.quote(":"));
         String url = URLS.urlGoogleOneYearStartingAt(exchangeAndSymbol[1], exchangeAndSymbol[0], start) + "#" + exchangeAndSymbol[1];
         try {
            internet.get(url);
         } catch (Exception err) {
            err.printStackTrace();
         }
         Thread.sleep(WAIT_BETWEEN_ASKS);
      }
   }
}
