package console;

import core.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

public class ConsoleUI implements UI {

    private State state;
    private boolean threaded;
    private GameImpl gameImpl;
    private MoveCommand command;
    private PrintStream outputStream;
    private BufferedReader inputStream;
    private GameCommandType[] gameCommandTypes;

    private MoveCommand returnCommand;

    public ConsoleUI(State state, boolean threaded) {
        this.state = state;
        this.outputStream = System.out;
        this.inputStream = new BufferedReader(new InputStreamReader(System.in));
        this.gameCommandTypes = GameCommandType.values();
        this.threaded = threaded;
    }

    @Override
    public MoveCommand getCommand() throws ScanException {
        // alles beim Alten wenn nicht multithreaded
        if(!threaded) {
            getCommandSingleThread();
            return worstHackEver();
        }
        return worstHackEver();
    }

    private MoveCommand worstHackEver() {
        if (returnCommand == null) {
            return new MoveCommand(new XY(0, 0));
        } else {
            MoveCommand temp = returnCommand;
            returnCommand = null;
            return temp;
        }
    }

    @SuppressWarnings("InfiniteLoopStatement")
    @Override
    public void multiThreadCommandProcess() {
        while (true) {
            getCommandSingleThread();
        }
    }

    private void getCommandSingleThread() {
        CommandScanner commandScanner = new CommandScanner(gameCommandTypes, inputStream);
        Command command = commandScanner.next();

        Object[] params = command.getParameters();
        GameCommandType commandType = (GameCommandType) command.getCommandType();
        try {
            this.getClass().getMethod(commandType.getName(), commandType.getParamTypes()).invoke(this, params);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unused")
    public void exit() {
        System.exit(0);
    }

    @SuppressWarnings("unused")
    public void help() {
        for(CommandTypeInfo cmdti: GameCommandType.values()) {
            System.out.println(cmdti.getName() + " " + cmdti.getHelpText());
        }
    }

    @SuppressWarnings("unused")
    public void all() {
        gameImpl.all();
    }

    @SuppressWarnings("unused")
    public void a() {
        returnCommand = new MoveCommand(XY.LEFT);
    }

    @SuppressWarnings("unused")
    public void w() {
        returnCommand = new MoveCommand(XY.UP);
    }

    @SuppressWarnings("unused")
    public void d() {
        returnCommand = new MoveCommand(XY.RIGHT);
    }

    @SuppressWarnings("unused")
    public void s() {
        returnCommand = new MoveCommand(XY.DOWN);
    }

    @SuppressWarnings("unused")
    public void master_energy() {
        outputStream.print(state.getBoard().getMasterSquirrel().getEnergy());
    }

    @SuppressWarnings("unused")
    public void spawn_mini(int energy, int x, int y) {
        try {
            gameImpl.spawnMiniSquirrel(energy, x, y);
        } catch (NotEnoughEnergyException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render(BoardView view) {
        for (int y = 0; y < view.getSize().getY(); y++) {
            for (int x = 0; x < view.getSize().getX(); x++) {
                char c;
                switch (view.getEntityType(x, y)) {
                    case BAD_BEAST:
                        c = 'B';
                        break;
                    case GOOD_BEAST:
                        c = 'b';
                        break;
                    case BAD_PLANT:
                        c = 'P';
                        break;
                    case GOOD_PLANT:
                        c = 'p';
                        break;
                    case MASTER_SQUIRREL:
                        c = 'M';
                        break;
                    case MINI_SQUIRREL:
                        c = 'm';
                        break;
                    case WALL:
                        c = '#';
                        break;
                    case NOTHING:
                    default:
                        c = ' ';
                        break;
                }
                outputStream.print(c);
            }
            outputStream.println();
        }
    }

}
