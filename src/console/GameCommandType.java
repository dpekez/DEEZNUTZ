package console;

public enum GameCommandType implements CommandTypeInfo {
    HELP("help"," * list all commands"),
    EXIT("exit"," * exit program"),
    ALL("all"," * hilfetext zu all"),
    LEFT("a"," * hilfetext zu left"),
    UP("w"," * hilfetext zu up"),
    DOWN("s"," * hilfetext zu down"),
    RIGHT("d"," * hilfetext zu right"),
    MASTER_ENERGY("master_energy"," * hilfetext zu master_energy"),
    SPAWN_MINI("spawn_mini"," * hilfetext zu spawn_mini");

    private String command;
    private String info;
    private Class<?>[] paramTypes;

    GameCommandType(String command, String info, Class<?>... paramTypes) {
        this.command = command;
        this.info = info;
        this.paramTypes = paramTypes;
    }

    @Override
    public String getName() {
        return this.command;
    }

    @Override
    public String getHelpText() {
        return this.info;
    }

    @Override
    public Class<?>[] getParamTypes() {
        return this.paramTypes;
    }
}



