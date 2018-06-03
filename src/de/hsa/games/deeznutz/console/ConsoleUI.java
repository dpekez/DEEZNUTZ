package de.hsa.games.deeznutz.console;

import de.hsa.games.deeznutz.UI;
import de.hsa.games.deeznutz.core.BoardView;
import de.hsa.games.deeznutz.core.MoveCommand;
import de.hsa.games.deeznutz.core.State;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.entities.MasterSquirrel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.reflect.InvocationTargetException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConsoleUI implements UI {
    private final static Logger logger = Logger.getLogger(ConsoleUI.class.getName());

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
        if (!threaded) {
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
            // The class keyword can also be used in the form
            // Class.class to get a Class object without needing
            // an instance of that class. For example, String.class
            // can be used instead of doing new String().getClass().
            // ConsoleUI.class.getMethod(commandType.getName(), commandType.getParamTypes()).invoke(this, params);
            this.getClass().getMethod(commandType.getName(), commandType.getParamTypes()).invoke(this, params);

        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            outputStream.println("No such command, try again: " + command);
        }
    }

    @SuppressWarnings("unused")
    public void exit() {
        logger.info("Exit program");
        System.exit(0);
    }

    @SuppressWarnings("unused")
    public void help() {
        logger.info("Print help to Console");
        for (CommandTypeInfo cmdti : GameCommandType.values()) {
            System.out.println(cmdti.getName() + " " + cmdti.getHelpText());
        }
    }

    @SuppressWarnings("unused")
    public void all() {
        logger.info("Print all entities and their location");
        System.out.println(state.getBoard().getEntitySet());
    }

    @SuppressWarnings("unused")
    public void a() {
        logger.finest("Move left");
        returnCommand = new MoveCommand(XY.LEFT);
    }

    @SuppressWarnings("unused")
    public void w() {
        logger.finest("Move up");
        returnCommand = new MoveCommand(XY.UP);
    }

    @SuppressWarnings("unused")
    public void d() {
        logger.finest("Move right");
        returnCommand = new MoveCommand(XY.RIGHT);
    }

    @SuppressWarnings("unused")
    public void s() {
        logger.finest("Move down");
        returnCommand = new MoveCommand(XY.DOWN);
    }

    @SuppressWarnings("unused")
    public void master_energy() {
        logger.info("Print the MasterSquirrel energy");
        outputStream.print(state.getBoard().getMasterSquirrel().getEnergy());
    }

    @SuppressWarnings("unused")
    public void spawn_mini(int energy, int x, int y) {
        logger.info("Spawn a MiniSquirrel");
        MasterSquirrel daddy = state.getBoard().getMasterSquirrel();
        XY direction = new XY(x, y);
        if (state.getBoard().getMasterSquirrel().getEnergy() >= energy) {
            state.getBoard().insertMiniSquirrel(energy, direction, daddy);
        } else {
            logger.warning("Not enough energy to spawn MiniSquirrel");
            throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (state.getBoard().getMasterSquirrel().getEnergy()) + " Energie");
        }
    }

    @Deprecated
    public void spawnMiniSquirrel(Object[] parameters) {
        logger.info("Spawn a MiniSquirrel");
        try {
            int energy = (Integer) parameters[0];
            XY direction = new XY((Integer) parameters[1], (Integer) parameters[2]);
            MasterSquirrel daddy = state.getBoard().getMasterSquirrel();
            if (state.getBoard().getMasterSquirrel().getEnergy() >= energy) {
                state.getBoard().insertMiniSquirrel(energy, direction, daddy);
            } else {
                logger.warning("Not enough energy to spawn MiniSquirrel");
                throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (state.getBoard().getMasterSquirrel().getEnergy()) + " Energie");
            }
        } catch (NotEnoughEnergyException e) {
            e.getMessage();
            logger.warning(e.getMessage());
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
                    case MASTER_SQUIRREL_BOT:
                        c = 'X';
                        break;
                    case MINI_SQUIRREL_BOT:
                        c = 'x';
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
        outputStream.println(message());
    }

    private Object message() {
        return state.getBoard().getMasterSquirrel();
    }

}
