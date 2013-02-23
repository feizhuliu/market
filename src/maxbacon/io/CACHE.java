package maxbacon.io;

import java.io.File;
import java.util.HashMap;

/**
 * A very simple model for working with data via multiple tiers
 */
public class CACHE {

   public static interface Getter {
      public String get(String key) throws Exception;
   }

   public static class Internet implements Getter {
      private final HTTP http = new HTTP();

      @Override
      public String get(String key) throws Exception {
         return http.getUtf8String(key, 5, 100);
      }
   }

   public static class Disk implements Getter {
      private final Getter real;
      private final File   path;

      public Disk(Getter real, File path) {
         if (!path.exists()) {
            path.mkdirs();
         }
         if (!path.exists()) {
            throw new RuntimeException("The path:" + path.toString() + " does not exist");
         }
         if (!path.isDirectory()) {
            throw new RuntimeException("The path:" + path.toString() + " is not a  directory");
         }
         this.real = real;
         this.path = path;
      }

      public String get(String key) throws Exception {
         String lookup = HASH.md5(key) + "-";
         lookup += HASH.sha1(lookup + key);
         File subdir = new File(path, lookup.charAt(0) + "");
         if (!subdir.exists())
            subdir.mkdir();
         File entry = new File(subdir, lookup);
         if (entry.exists()) {
            return FILE.get(entry);
         } else {
            String contents = real.get(key);
            FILE.put(entry, contents);
            return contents;
         }
      }
   }

   public static class InfiniteMemory implements Getter {
      private final HashMap<String, String> map = new HashMap<String, String>();
      private final Getter                  real;

      public InfiniteMemory(Getter real) {
         this.real = real;
      }

      public String get(String key) throws Exception {
         if (map.containsKey(key)) {
            return map.get(key);
         }
         String contents = real.get(key);
         map.put(key, contents);
         return contents;
      }
   }
}
