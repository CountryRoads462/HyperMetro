import java.util.*;
import java.util.concurrent.*;


class FutureUtils {

    public static int howManyIsDone(List<Future> items) {
        int number = 0;
        for (Future future :
                items) {
            if (future.isDone()) {
                number++;
            }
        }
        return number;
    }

}