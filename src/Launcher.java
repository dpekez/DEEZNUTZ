import console.ScanException;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws Exception {

        Game game = new GameImpl();
        game.run();

    }
}
