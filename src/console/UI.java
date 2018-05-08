package console;

import core.BoardView;
import core.MoveCommand;

import java.io.IOException;

public interface UI {

    public abstract MoveCommand getCommand() throws IOException, ScanException;
    public void render(BoardView view);
}
