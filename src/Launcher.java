import console.ScanException;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Launcher {
    public static void main(String[] args) throws Exception {

        if(args.length >= 1)
            if(args[0].equalsIgnoreCase("multi")) {
                startGameMultiThreaded(new GameImpl(true));
                return;
            }
        startGameSingleThreaded(new GameImpl(false));
    }


    private static void startGameSingleThreaded(Game game) throws Exception {
        System.out.println("single");
        game.run();
    }


    private static void startGameMultiThreaded(Game game) throws ScanException {
        System.out.println("multi");

        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    game.run();
                } catch (IOException | ScanException e) {
                    e.printStackTrace();
                }
            }
        };

        timer.schedule(timerTask, 0);

        game.ui.multiThreadCommandProcess();
    }
}
