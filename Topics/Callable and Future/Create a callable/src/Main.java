import java.util.Scanner;
import java.util.concurrent.Callable;

class CallableUtil {
    public static Callable<String> getCallable() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                Scanner scanner = new Scanner(System.in);
                return scanner.nextLine();
            }
        };
    }
}