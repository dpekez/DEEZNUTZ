package core;

import botapi.ControllerContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.logging.Logger;

public class DebugHandler implements InvocationHandler {
    private final ControllerContext view;

    public DebugHandler(ControllerContext view) {
        this.view = view;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        StringBuilder stringBuilder = new StringBuilder();
        Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
        stringBuilder.append("* method:").append(method).append(" parms");
        if (args != null) {
            for (Object arg : args)
                stringBuilder.append(" ").append(arg);
            stringBuilder.append("\n");
        }
        Object result = null;
        result = method.invoke(view, args);
        return result;
    }
}


