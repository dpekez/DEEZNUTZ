package console;


import java.util.Arrays;

public class Command {

    private CommandTypeInfo commandType;
    private Object[] parameters;

    public Command(CommandTypeInfo commandType, Object[] parameters) {
        this.commandType = commandType;
        this.parameters = parameters;
    }

    CommandTypeInfo getCommandType() {
        return commandType;
    }

    Object[] getParameters() {
        return parameters;
    }

    @Override
    public String toString() {
        return "Command{" +
                "commandType=" + commandType +
                ", parameters=" + (parameters == null ? null : Arrays.asList(parameters)) +
                '}';
    }
}
