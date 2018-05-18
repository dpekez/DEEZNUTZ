package console;

import core.*;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class ConsoleUI implements UI {

    private State state;
    private boolean threaded;
    private GameImpl gameImpl;
    private MoveCommand command;
    private PrintStream outputStream;
    private BufferedReader inputStream;
    private GameCommandType[] gameCommandTypes;


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
        if(!threaded)
            return getCommandSingleThread();

        if (command == null) {
            return new MoveCommand(new XY(0, 0));
        } else {
            MoveCommand temp = command;
            command = null;
            return temp;
        }
    }

    @Override
    public void multiThreadCommandProcess() throws ScanException {
        while (true) {
            this.command = getCommandSingleThread();
        }
    }

    private MoveCommand getCommandSingleThread() throws ScanException {
        CommandScanner commandScanner = new CommandScanner(gameCommandTypes, inputStream);
        Command command;
        command = commandScanner.next();

        switch ((GameCommandType) command.getCommandType()) {
            case EXIT:
                gameImpl.exit();
                break;
            case HELP:
                gameImpl.help();
                break;
            case ALL:
                gameImpl.all();
                break;
            case LEFT:
                return new MoveCommand(new XY(-1, 0));
            case UP:
                return new MoveCommand(new XY(0, -1));
            case DOWN:
                return new MoveCommand(new XY(0, 1));
            case RIGHT:
                return new MoveCommand(new XY(1, 0));
            case MASTER_ENERGY:
                gameImpl.masterEnergy();
                break;
            case SPAWN_MINI:
                try {
                    gameImpl.spawnMiniSquirrel(command.getParameters());
                } catch (NotEnoughEnergyException e) {
                    e.printStackTrace();
                }
                break;
            default:
                return new MoveCommand(new XY(0, 0));
        }
        return new MoveCommand(new XY(0, 0));
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
