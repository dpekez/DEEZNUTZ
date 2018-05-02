package console;

import core.BoardView;
import core.MoveCommand;
import core.XY;

import java.util.Scanner;

public class ConsoleUI implements UI {

    private Scanner userInput = new Scanner(System.in);

    @Override
    public MoveCommand getMoveCommand() {
        switch (userInput.next()) {
            case "w":
                return new MoveCommand(new XY(0, -1));
            case "a":
                return new MoveCommand(new XY(-1, 0));
            case "s":
                return new MoveCommand(new XY(0, 1));
            case "d":
                return new MoveCommand(new XY(1, 0));
            default:
                return null;
        }
    }

    @Override
    public void render(BoardView view) {
        for (int y = 0; y < view.getSize().getY(); y++) {
            for (int x = 0; x < view.getSize().getX(); x++) {

                char c;

                switch(view.getEntityType(x, y)) {
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

}
