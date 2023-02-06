import java.util.List;
import java.util.concurrent.*;


class FutureUtils {

    public static int executeCallableObjects(List<Future<Callable<Integer>>> items) {
        int sum = 0;
        try {
            for (int i = items.size() - 1; i >= 0; i--) {
                Future<Callable<Integer>> elem = items.get(i);
                sum += elem.get().call();
            }
        } catch (Exception ignored) {
        }
        return sum;
    }
}