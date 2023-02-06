import java.lang.reflect.Method;

class MethodFinder {

    public static String findMethod(String methodName, String[] classNames) {
        String result = "";
        for (String className :
                classNames) {
            try {
                Method[] methods = Class.forName(className).getMethods();
                for (Method method :
                        methods) {
                    if (method.getName().equals(methodName)) {
                        return className;
                    }
                }
            } catch (ClassNotFoundException ignored) {
            }

        }
        return result;
    }
}