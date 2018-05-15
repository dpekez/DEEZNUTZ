import GUI.FxUI;
import console.ConsoleUI;
import console.ScanException;
import core.BoardConfig;
import core.State;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.event.EventHandler;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;


public class Launcher extends Application {

    private static BoardConfig boardConfig = new BoardConfig();

    public static void main(String[] args) throws Exception {

        if(args.length >= 1) {
            if (args[0].equalsIgnoreCase("multi")) {
                Application.launch(args);
                //startGameMultiThreaded(new GameImpl (new ConsoleUI( new State(), true ), new State(), true) );
                return;
            }
        }

        //(new GameImpl(new ConsoleUI(true), boardConfig, true)).run();
        //startGameSingleThreaded(new GameImpl(false));
    }


    @Override
    public void start(Stage primaryStage) throws Exception {

        FxUI fxUI = FxUI.createInstance(boardConfig.getBoardSize());
        final Game game = new GameImpl(fxUI, new State(), true);

        primaryStage.setScene(fxUI);
        primaryStage.setTitle("DEEZ NUTS");
        fxUI.getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent evt) {
                System.exit(-1);
            }
        });
        primaryStage.show();

        startGameMultiThreaded(game);
    }


    private static void startGameSingleThreaded(Game game) throws Exception {
        System.out.println("single");
        //(new GameImpl(new ConsoleUI(true), boardConfig, true)).run();
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
