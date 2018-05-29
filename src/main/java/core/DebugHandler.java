package core;

import botapi.ControllerContext;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
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

        stringBuilder.append("Methode : ").append(method).append(" , params : ").append(args).append("\n");

        Object result = null;
        try {
            result = method.invoke(view, args);
        } catch (IllegalAccessException ex) {
            logger.log(Level.FINER, ex.getMessage());
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
        }
        stringBuilder.append("* result:").append(result);
        logger.log(Level.FINER, stringBuilder.toString());
        return result;
    }
}


