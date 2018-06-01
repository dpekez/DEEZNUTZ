package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.botapi.ControllerContext;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
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

        stringBuilder.append("Methode : ").append(method).append(" , params : ").append(args).append("\n");

        Object result = null;
        try {
            result = method.invoke(view, args);
        } catch (IllegalAccessException e) {
            Logger.getLogger(Launcher.class.getName()).severe(e.getMessage());
        } catch (InvocationTargetException e) {
            throw e.getTargetException();
        } catch (Exception e) {
            Logger.getLogger(Launcher.class.getName()).severe(e.getMessage());
        }

        stringBuilder.append("* result:").append(result);
        // todo: solve this mystery
        // heavy performance impact
        //Logger.getLogger(Launcher.class.getName()).finer(stringBuilder.toString());
        return result;
    }

}
