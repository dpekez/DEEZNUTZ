package console;

import core.BoardView;
import core.MoveCommand;

public interface UI {

    public abstract MoveCommand getCommand();
    public void render(BoardView view);
}
