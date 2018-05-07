package console;

import core.BoardView;
import core.MoveCommand;
import core.State;
import core.XY;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class ConsoleUI implements UI {

    private PrintStream outputStream;
    private BufferedReader inputStream;
    private Command command;
    private GameCommandType[] gameCommandTypes;
    private State state;

    public ConsoleUI() {
        this.outputStream = System.out;
        this.inputStream = new BufferedReader(new InputStreamReader(System.in));
        this.gameCommandTypes = GameCommandType.values();
    }

    @Override
    public MoveCommand getCommand() {


        CommandScanner commandScanner = new CommandScanner(gameCommandTypes, inputStream);
        while (true) { // the loop over all commands with one input line for every command

            Command command = null;
            try {
                command = commandScanner.next();
            } catch (ScanException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (command != null) {
                switch ((GameCommandType) command.getCommandType()) {
                    case EXIT:
                        exit();
                        break;
                    case HELP:
                        help();
                        break;
                    case ALL:
                        return all();
                    case LEFT:
                        return new MoveCommand(new XY(-1,0));
                    case UP:
                        return new MoveCommand(new XY(0,-1));
                    case DOWN:
                        return new MoveCommand(new XY(0,1));
                    case RIGHT:
                        return new MoveCommand(new XY(1,0));
                    case MASTER_ENERGY:
                        return master_energy();
                    case SPAWN_MINI:
                        //TODO
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

        System.out.println("Number of Entities: " + view.getEntityCount());

    }
    private void help(){
        for(GameCommandType commandType: GameCommandType.values()){
            outputStream.println("<" +commandType.getName() + "> - " + commandType.getHelpText());
        }
    }
    private void exit(){
        System.exit(0);
    }

    private MoveCommand all(){
        return new MoveCommand(new XY(0,0));
    }
    private MoveCommand master_energy(){
        return master_energy();
    }
}
