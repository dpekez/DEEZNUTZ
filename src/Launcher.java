import GUI.FxUI;
import console.ScanException;
import core.BoardConfig;
import core.Game;
import core.GameImpl;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Launcher extends Application {
    private static Launcher launcher = new Launcher();
    private Timer timer = new Timer();

    public static void main(String[] args) throws Exception {

        if (args.length >= 1)
            if (args[0].equalsIgnoreCase("multi")) {
                startGameMultiThreaded(new GameImpl(true));
                return;
            }
        //startGameSingleThreaded(new GameImpl(true));
        launcher.startGUIGame(args);
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
        timer.schedule(timerTask, 1000);
        game.ui.multiThreadCommandProcess();
    }

    private void startGUIGame(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        BoardConfig boardConfig = new BoardConfig();
        FxUI fxUI = FxUI.createInstance(boardConfig.getBoardSize());
        final Game game = new GameImpl(true);
        game.setUi(fxUI);
        fxUI.setGameImpl((GameImpl) game);
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("DEEZNUTZ");
        primaryStage.setAlwaysOnTop(true);
        fxUI.getWindow().setOnCloseRequest(evt -> System.exit(-1));
        primaryStage.show();
        try {
            startGameMultiThreaded(game);
        } catch (ScanException e) {
            e.printStackTrace();
        }

    }
}
