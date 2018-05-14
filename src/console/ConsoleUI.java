package console;

import core.BoardView;
import core.MoveCommand;
import core.State;
import core.XY;
import entities.MasterSquirrel;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class ConsoleUI implements UI {

    private PrintStream outputStream;
    private BufferedReader inputStream;
    private GameCommandType[] gameCommandTypes;
    private State state;
    private MoveCommand command;
    private boolean threaded;


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


        if(command == null)
            return new MoveCommand(new XY(0, 0));
        else {
            MoveCommand temp = command;
            command = null;
            return temp;
        }
    }


    @Override
    public void multiThreadCommandProcess() throws ScanException {
        while(true)
            this.command = getCommandSingleThread();
    }


    public MoveCommand getCommandSingleThread() throws ScanException {
        CommandScanner commandScanner = new CommandScanner(gameCommandTypes, inputStream, outputStream);
        Command command;
        command = commandScanner.next();

        switch ((GameCommandType) command.getCommandType()) {
            case EXIT:
                exit();
                break;
            case HELP:
                help();
                break;
            case ALL:
                all();
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
                masterEnergy();
                break;
            case SPAWN_MINI:
                try {
                    spawnMiniSquirrel(command.getParameters());
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

                System.out.print(c);
            }

            System.out.println();
        }

    }


    private void help() {
        for (GameCommandType commandType : GameCommandType.values()) {
            outputStream.println("<" + commandType.getName() + "> - " + commandType.getHelpText());
        }
    }

    private void exit() {
        System.out.println("Bye bye");
        System.exit(0);
    }

    private void all() {
        System.out.println(state.getBoard().getEntitySet());
    }

    private void masterEnergy() {
        System.out.println(state.getBoard().getMasterSquirrel().getEnergy());
    }

    private void spawnMiniSquirrel(Object[] parameters) throws NotEnoughEnergyException {
        int energy = (Integer) parameters[0];
        XY direction = new XY((Integer) parameters[1], (Integer) parameters[2]);
        MasterSquirrel daddy = state.getBoard().getMasterSquirrel();

        if (state.getBoard().getMasterSquirrel().getEnergy() >= energy) {
            state.getBoard().insertMiniSquirrel(energy, direction, daddy);
        } else {
            throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (state.getBoard().getMasterSquirrel().getEnergy()) + " Energie");
        }
    }

}
