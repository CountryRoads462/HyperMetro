    import java.util.Objects;
    import java.util.concurrent.*;


class FutureUtils {

    public static int determineCallableDepth(Callable callable) throws Exception {
        int depth = 1;
        while (true) {
            if (callable.call() instanceof Callable) {
                callable = (Callable) callable.call();
                depth++;
            } else {
                break;
            }
        }
        return depth;
    }

}