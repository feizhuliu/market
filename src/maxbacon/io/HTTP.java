package maxbacon.io;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;

/**
 * This is just a simplified HTTP client that works as one expects without all that streaming non-sense.
 */
public class HTTP {
   private final HttpClient client;

   public static interface ResultValidatorAndFilter {
      public String perform(String result) throws Exception;
   }

   public static final ResultValidatorAndFilter NO_VALIDATION_NO_FILTERING = new ResultValidatorAndFilter() {
                                                                              public String perform(String result) throws Exception {
                                                                                 return result;
                                                                              }
                                                                           };

   public final ResultValidatorAndFilter        resultValidatorAndFilter;

   public HTTP(ResultValidatorAndFilter resultValidatorAndFilter) {
      this.client = new DefaultHttpClient();
      this.resultValidatorAndFilter = resultValidatorAndFilter;
   }

   private String _getUtf8String(String url) throws Exception {
      try {
         HttpGet httpget = new HttpGet(url);
         ResponseHandler<String> responseHandler = new BasicResponseHandler();
         return resultValidatorAndFilter.perform(client.execute(httpget, responseHandler));
      } finally {
      }
   }

   public String getUtf8String(String url, int maxAttempts, int startingBackOff) throws Exception {
      int _backOff = startingBackOff;
      int _attempts = 0;
      while (true) {
         try {
            return _getUtf8String(url);
         } catch (Exception err) {
            if (_attempts == maxAttempts) {
               throw err;
            }
            Thread.sleep(_backOff);
            _backOff *= 2;
            _attempts++;
         }
      }
   }
}
