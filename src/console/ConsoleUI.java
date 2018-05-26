package console;

import core.*;
import entities.MasterSquirrel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;

public class ConsoleUI implements UI {

    private State state;
    private PrintStream outputStream;
    private BufferedReader inputStream;
    private GameCommandType[] gameCommandTypes;
    private boolean threaded;
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
            outputStream.println("No such command, try again: " + command);
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
        System.out.println(state.getBoard().getEntitySet());
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
        MasterSquirrel daddy = state.getBoard().getMasterSquirrel();
        XY direction = new XY(x, y);
        if (state.getBoard().getMasterSquirrel().getEnergy() >= energy) {
            state.getBoard().insertMiniSquirrel(energy, direction, daddy);
        } else {
            throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (state.getBoard().getMasterSquirrel().getEnergy()) + " Energie");
        }
    }

    @Deprecated
    public void spawnMiniSquirrel(Object[] parameters) {
        int energy = (Integer) parameters[0];
        XY direction = new XY((Integer) parameters[1], (Integer) parameters[2]);
        MasterSquirrel daddy = state.getBoard().getMasterSquirrel();
        if (state.getBoard().getMasterSquirrel().getEnergy() >= energy) {
            state.getBoard().insertMiniSquirrel(energy, direction, daddy);
        } else {
            throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (state.getBoard().getMasterSquirrel().getEnergy()) + " Energie");
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
