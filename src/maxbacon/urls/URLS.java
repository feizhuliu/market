package maxbacon.urls;

import org.joda.time.DateTime;

/**
 * Simplified ways of querying the various services that are out there.
 * 
 * @author bacon
 * 
 */
public class URLS {

   public static String urlGoogleOneYearStartingAt(String symbol, String exchange, DateTime start) {
      return "https://www.google.com/finance/getprices?q=" + symbol.toUpperCase().trim() + "&x=" + exchange.toUpperCase().trim() + "&i=60&p=1Y&f=d,c,v,o,h,l&df=cpct&auto=1&ts=" + start.getMillis();
   }

   public static String urlYahooHistorical(String symbol, DateTime start, DateTime finish) {
      StringBuilder url = new StringBuilder();
      url.append("http://ichart.yahoo.com/table.csv?s=");
      url.append(symbol);
      url.append("&a=" + (start.getMonthOfYear() - 1));
      url.append("&b=" + start.getDayOfMonth());
      url.append("&c=" + start.getYear());
      url.append("&d=" + (finish.getMonthOfYear() - 1));
      url.append("&e=" + finish.getDayOfMonth());
      url.append("&f=" + finish.getYear());
      url.append("&g=d&ignore=.csv");
      return url.toString();
   }
}
