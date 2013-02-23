package maxbacon.io;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Hex;

public class HASH {

   private static String hex(byte[] y) {
      return new String(Hex.encodeHex(y));
   }

   public static String md5(String x) {
      try {
         return hex(MessageDigest.getInstance("MD5").digest(x.getBytes("UTF-8")));
      } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
   }

   public static String sha1(String x) {
      try {
         return hex(MessageDigest.getInstance("SHA1").digest(x.getBytes("UTF-8")));
      } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
         throw new RuntimeException(e);
      }
   }
}
