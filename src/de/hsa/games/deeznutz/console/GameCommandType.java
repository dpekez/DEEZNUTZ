package de.hsa.games.deeznutz.console;

public enum GameCommandType implements CommandTypeInfo {

    HELP("help", "  H            ", "print this help"),
    EXIT("exit", "  ESC / Q ", "quit this program"),
    ALL("all", "  E            ", "print all entities"),
    HS("hs", "  T            ", "prints highscores"),
    LEFT("a", "  A            ", "move left"),
    UP("w", "  W           ", "move up"),
    DOWN("s", "  S            ", "move down"),
    RIGHT("d", "  D            ", "move right"),
    MASTER_ENERGY("master_energy", "  R            ", "print master energy"),
    SPAWN_MINI("spawn_mini", "  M          ", "<energy> <x-direction> <y-direction> spawn mini squirrel", int.class, int.class, int.class);

    private String command;
    private String commandFxUI;
    private String info;
    private Class<?>[] paramTypes;

    GameCommandType(String command, String commandFxUI, String info, Class<?>... paramTypes) {
        //... zero or more Class objects may be passed
        this.commandFxUI = commandFxUI;
        this.command = command;
        this.info = info;
        this.paramTypes = paramTypes;
    }

    @Override
    public String getName() {
        return this.command;
    }

    @Override
    public String getCommandFxUI() {
        return this.commandFxUI;
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
