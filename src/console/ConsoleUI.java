package console;

import core.*;
import entities.MasterSquirrel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;


public class ConsoleUI implements UI {

    private PrintStream outputStream;
    private BufferedReader inputStream;
    private GameCommandType[] gameCommandTypes;
    private MasterSquirrel masterSquirrel;
    private Board board;


    public ConsoleUI() {
        this.outputStream = System.out;
        this.inputStream = new BufferedReader(new InputStreamReader(System.in));
        this.gameCommandTypes = GameCommandType.values();
    }


    @Override
    public MoveCommand getCommand() throws IOException, ScanException {

        CommandScanner commandScanner = new CommandScanner(gameCommandTypes, inputStream);


        while (true) {

            Command command;
            command = commandScanner.next();

            if (command != null) {

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
                        //todo
                        masterEnergy();
                        break;
                    case SPAWN_MINI:
                        //todo: parameter rausfiltern
                        //todo: parameter uebergeben
                        //todo: wo minisquirrel sinnigerweise instanziieren, hier!?
                        spawnMiniSquirrel();
                        break;
                        default:
                            return null;
                }
            }
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

                System.out.print(c);
            }

            System.out.println();
        }

    }


    private void help() {
        for (GameCommandType commandType: GameCommandType.values()) {
            outputStream.println("<" + commandType.getName() + "> - " + commandType.getHelpText());
        }
    }

    private void exit() {
        System.out.println("Bye bye");
        System.exit(0);
    }

    public void all() {
        //todo
    }

    public void masterEnergy() {
        //todo
    }

    public void spawnMiniSquirrel() {
        //todo
    }

}
