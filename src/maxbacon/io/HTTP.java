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

   public HTTP() {
      this.client = new DefaultHttpClient();
   }

   private String _getUtf8String(String url) throws Exception {
      try {
         HttpGet httpget = new HttpGet(url);
         ResponseHandler<String> responseHandler = new BasicResponseHandler();
         return client.execute(httpget, responseHandler);
      } finally {
         client.getConnectionManager().shutdown();
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
