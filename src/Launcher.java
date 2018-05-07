import console.ScanException;

import java.io.IOException;

public class Launcher {
    public static void main(String[] args) throws IOException, ScanException {

        Game game = new GameImpl();
        game.run();

    }
}
