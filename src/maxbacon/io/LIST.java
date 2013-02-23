package maxbacon.io;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class LIST {

   /**
    * I secretly hate this algorithm.
    */
   public static <T> List<T> reverse(List<T> list) {
      Stack<T> orderChangingStack = new Stack<>();
      ArrayList<T> reversedList = new ArrayList<T>();
      for (T item : list)
         orderChangingStack.push(item);
      while (!orderChangingStack.empty()) {
         reversedList.add(orderChangingStack.pop());
      }
      return reversedList;
   }
}
