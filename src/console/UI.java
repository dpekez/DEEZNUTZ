package console;

import core.BoardView;
import core.MoveCommand;

public interface UI {

    MoveCommand getCommand() throws ScanException;

    void render(BoardView view);

    void multiThreadCommandProcess() throws ScanException;

}
