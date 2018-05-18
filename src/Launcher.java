import GUI.FxUI;
import Music.BackgroundMusic;
import console.ScanException;
import core.BoardConfig;
import core.Game;
import core.GameImpl;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;


public class Launcher extends Application {
    private Scanner scanner = new Scanner(System.in);
    private static Launcher launcher = new Launcher();

    private static void startGameMultiThreaded(Game game) throws ScanException {
        System.out.println("multi");
        Timer timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    game.run();
                } catch (ScanException e) {
                    e.printStackTrace();
                }
            }
        };
        timer.schedule(timerTask, 0);
        game.ui.multiThreadCommandProcess();
    }

    public static void main(String[] args) throws Exception {
        if (args.length >= 1)
            if (args[0].equalsIgnoreCase("multi")) {
                startGameMultiThreaded(new GameImpl(true));
                return;
            }
        launcher.menu(args, launcher);
    }

    private static void startGameSingleThreaded(Game game) throws ScanException {
        System.out.println("single");
        game.run();
    }

    private void menu(String[] args, Launcher launcher) throws Exception {
        System.out.println("Wählen sie einen Spielmodus: [1] Spiel auf der Konsole [2] Spiel mit GUI [3] Verlassen");
        switch (scanner.nextInt()) {
            case 1:
                launcher.gameMode(args);
                break;
            case 2:
                launcher.startGUIGame(args);
                break;
            case 3:
                System.exit(0);
        }
    }

    private void gameMode(String[] args) throws Exception {
        System.out.println("Wählen sie zwischen den Spielmodi: [1] Multithreaded [2] Siglethreaded [3] Verlassen ");
        switch (scanner.nextInt()) {
            case 1:
                startGameSingleThreaded(new GameImpl(true));
                break;
            case 2:
                startGameSingleThreaded(new GameImpl(false));
                break;
            case 3:
                System.exit(0);
        }
    }

    private void startGUIGame(String[] args) {
        Application.launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws ScanException {
        BoardConfig boardConfig = new BoardConfig();
        FxUI fxUI = FxUI.createInstance(boardConfig.getBoardSize());
        final Game game = new GameImpl(true);
        BackgroundMusic.sound1.loop();
        game.setUi(fxUI);
        fxUI.setGameImpl((GameImpl) game);
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("DEEZNUTZ");
        primaryStage.setAlwaysOnTop(false);
        fxUI.getWindow().setOnCloseRequest(evt -> System.exit(-1));
        primaryStage.show();
        startGameMultiThreaded(game);
    }
}
