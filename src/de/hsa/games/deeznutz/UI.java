package de.hsa.games.deeznutz;

import de.hsa.games.deeznutz.console.ScanException;
import de.hsa.games.deeznutz.core.BoardView;
import de.hsa.games.deeznutz.core.MoveCommand;

public interface UI {

    MoveCommand getCommand() throws ScanException;

    void render(BoardView view);

    void multiThreadCommandProcess() throws ScanException;

}
