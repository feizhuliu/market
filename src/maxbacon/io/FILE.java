package maxbacon.io;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class FILE {
   public static void put(File file, String contents) throws IOException {
      Random rng = new Random();
      File temp = new File(file.getCanonicalPath() + ".temp." + System.currentTimeMillis() + "." + rng.nextInt());
      while (temp.exists()) {
         temp = new File(file.getCanonicalPath() + ".temp." + System.currentTimeMillis() + "." + rng.nextInt());
      }
      File dest = file;
      FileWriter writer = new FileWriter(temp);
      try {
         writer.write(contents);
         writer.flush();
      } finally {
         writer.close();
      }
      temp.renameTo(dest);
   }

   public static void put(String filename, String contents) throws IOException {
      put(new File(filename), contents);
   }

   public static String get(String filename) throws IOException {
      return get(new File(filename));
   }

   public static String get(File file) throws IOException {
      StringBuilder sb = new StringBuilder();
      FileReader reader = new FileReader(file);
      try {
         char[] cbuf = new char[64 * 1024];
         int rd;
         while ((rd = reader.read(cbuf)) >= 0) {
            sb.append(cbuf, 0, rd);
         }
         return sb.toString();
      } finally {
         reader.close();
      }
   }
}
