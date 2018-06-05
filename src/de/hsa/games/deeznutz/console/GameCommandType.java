package de.hsa.games.deeznutz.console;

public enum GameCommandType implements CommandTypeInfo {

    HELP("help", "print this help"),
    EXIT("exit", "quit this program"),
    ALL("all", "print all entities"),
    HS("hs", "prints highscores"),
    LEFT("a", "move left"),
    UP("w", "move up"),
    DOWN("s", "move down"),
    RIGHT("d", "move right"),
    MASTER_ENERGY("master_energy", "print master energy"),
    SPAWN_MINI("spawn_mini", "<energy> <x-direction> <y-direction> spawn mini squirrel", int.class, int.class, int.class);

    private String command;
    private String info;
    private Class<?>[] paramTypes;

    GameCommandType(String command, String info, Class<?>... paramTypes) { //... zero or more Class objects may be passed
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
