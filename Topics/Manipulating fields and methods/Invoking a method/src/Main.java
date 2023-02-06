import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

class MethodsDemo {

    public static void main(String[] args) {
        try {
            System.out.println(Class.forName("SomeClass").getDeclaredMethods()[0].invoke(null));
        } catch (Exception ignored) {
        }
    }
}