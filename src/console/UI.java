package console;

import core.BoardView;
import core.MoveCommand;

public interface UI {

    public abstract Command getCommand();
    public void render(BoardView view);
}
