package console;

import core.BoardView;
import core.MoveCommand;

import java.io.IOException;


public interface UI {

    MoveCommand getCommand() throws IOException, ScanException;
    void render(BoardView view);
    void multiThreadCommandProcess() throws ScanException;
}
