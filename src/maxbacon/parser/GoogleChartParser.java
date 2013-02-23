package maxbacon.parser;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Pattern;

import maxbacon.io.HTTP.ResultValidatorAndFilter;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.net.URLCodec;

/**
 * Google has a chart format; this parsers it (if it's in the right form)
 * 
 * @author bacon
 */
public class GoogleChartParser implements Iterator<Quote> {
   private static final String                  COMMA         = Pattern.quote(",");
   private final BufferedReader                 reader;
   private String                               ln;
   public final long                            interval;
   public final long                            offset;
   public final Map<String, String>             headers;
   private long                                 lastTime      = 0;

   public static final ResultValidatorAndFilter VALIDATE_HTTP = new ResultValidatorAndFilter() {
                                                                 public String perform(String result) throws Exception {
                                                                    if (result.indexOf("MARKET_OPEN_MINUTE=") < 0)
                                                                       throw new Exception("Not Valid:" + result.substring(0, Math.min(result.length(), 1000)));
                                                                    if (result.indexOf("COLUMNS=DATE,CLOSE,HIGH,LOW,OPEN,VOLUME") < 0)
                                                                       throw new Exception("Not Valid:" + result.substring(0, Math.min(result.length(), 1000)));
                                                                    if (result.indexOf("EXCHANGE") < 0)
                                                                       throw new Exception("Not Valid:" + result.substring(0, Math.min(result.length(), 1000)));
                                                                    return result;
                                                                 }
                                                              };

   private static String decode(String x) {
      try {
         return new String(URLCodec.decodeUrl(x.getBytes("UTF-8")), "UTF-8");
      } catch (UnsupportedEncodingException | DecoderException e) {
         throw new RuntimeException(e);
      }
   }

   public static Iterable<Quote> ofString(final String contents) {
      return new Iterable<Quote>() {
         public Iterator<Quote> iterator() {
            try {
               return new GoogleChartParser(new ByteArrayInputStream(contents.getBytes("UTF-8")));
            } catch (UnsupportedEncodingException e) {
               throw new RuntimeException(e);
            } catch (IOException e) {
               throw new RuntimeException(e);
            }
         }
      };
   }

   public GoogleChartParser(InputStream input) throws IOException {
      reader = new BufferedReader(new InputStreamReader(input));
      long _interval = -1;
      long _offset = -1;
      HashMap<String, String> _headers = new HashMap<>();
      try {
         while ((ln = reader.readLine()) != null) {
            ln = decode(ln.trim()).trim().toLowerCase();
            int kEqual = ln.indexOf('=');
            if (ln.indexOf('=') > 0) {
               String name = ln.substring(0, kEqual);
               String val = ln.substring(kEqual + 1);
               _headers.put(name, val);
               if (name.equals("columns")) {
                  if (!val.equals("date,close,high,low,open,volume")) {
                     throw new RuntimeException("sorry, this charting process is broken");
                  }
               }
               if (name.equals("interval")) {
                  _interval = Long.parseLong(val);
               }
               if (name.equals("timezone_offset")) {
                  _offset = Long.parseLong(val);
               }
            } else {
               return;
            }
         }
      } finally {
         this.interval = _interval;
         this.offset = _offset;
         this.headers = Collections.unmodifiableMap(_headers);
      }
   }

   @Override
   public boolean hasNext() {
      return ln != null;
   }

   @Override
   public Quote next() {
      String[] parts = ln.split(COMMA);
      long time = 0;
      if (parts[0].charAt(0) == 'a') {
         time = Long.parseLong(parts[0].substring(1));
         lastTime = time;
      } else {
         time = Long.parseLong(parts[0]) * interval + lastTime;
      }
      Quote q = new Quote(time, Double.parseDouble(parts[1]), Double.parseDouble(parts[2]), Double.parseDouble(parts[3]), Double.parseDouble(parts[4]), Long.parseLong(parts[5]));
      try {
         ln = reader.readLine();
      } catch (IOException e) {
         throw new RuntimeException(e);
      }
      return q;
   }

   @Override
   public void remove() {
   }
}
