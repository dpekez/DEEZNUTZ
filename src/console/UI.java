package console;

import core.BoardView;
import core.MoveCommand;

public interface UI {

    public MoveCommand getMoveCommand();
    public void render(BoardView view);
}
