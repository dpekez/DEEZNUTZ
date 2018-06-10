package de.hsa.games.deeznutz;

import de.hsa.games.deeznutz.console.ScanException;
import de.hsa.games.deeznutz.core.BoardConfig;
import de.hsa.games.deeznutz.core.GameImpl;
import de.hsa.games.deeznutz.core.GameImplBotOnly;
import de.hsa.games.deeznutz.core.GameImplBotUser;
import de.hsa.games.deeznutz.gui.FxUI;
import javafx.application.Application;
import javafx.stage.Stage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Logger;

public class Launcher extends Application {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private static Launcher launcher = new Launcher();
    private static BoardConfig boardConfig = new BoardConfig("Properties/default.properties");
    private static Game game;

    public static void main(String[] args) {
        launcher.menu();
    }

    /**
     * Change between diffrent game modes
     * like between a siglethreaded game on the console or an multithreaded game on the GUI
     */

    private void menu() {
        switch (boardConfig.getGameMode()) {
            case 1:
                logger.info("Game Type: Multithreaded Console");
                fighterMenu(true, "console");
                break;
            case 2:
                logger.info("Game Type: Singlethreaded Console");
                fighterMenu(false, "console");
                break;
            case 3:
                logger.info("Game Type: GUI");
                fighterMenu(true, "gui");
                break;
        }
    }

    /**
     * choose wich type of MasterSquirrel will be spawned on the board
     *
     * @param threaded yes or no
     * @param display  console or GUI
     */

    private void fighterMenu(boolean threaded, String display) {
        switch (boardConfig.getPlayerMode()) {
            case 1:
                logger.info("Fight Mode: Single");
                game = new GameImpl(threaded, boardConfig);
                break;
            case 2:
                logger.info("Fight Mode: Bot against User");
                game = new GameImplBotUser(threaded, boardConfig);
                break;
            case 3:
                logger.info("Fight Mode: Bots only");
                game = new GameImplBotOnly(threaded, boardConfig);
                break;
        }

        if (display.equalsIgnoreCase("gui"))
            Application.launch();
        else
            startGame(threaded, game);
    }

    private static void startGame(boolean threaded, Game game) {
        if (threaded) {
            logger.info("Starting Game Multithreaded...");


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

            System.out.println("Get ready to rumble!");
            timer.schedule(timerTask, 0, 1);
            game.ui.multiThreadCommandProcess();
        } else {
            logger.info("Starting Game Singlethreaded...");

            game.run();
        }
    }

    @Override
    public void start(Stage primaryStage) throws ScanException {
        logger.info("Starting GUI...");

        FxUI fxUI = FxUI.createInstance(boardConfig.getBoardSize());

        game.setUi(fxUI);
        fxUI.setGame(game);
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("DEEZNUTZ");

        fxUI.getWindow().setOnCloseRequest(evt -> {
            game.state.saveHighscores();
            logger.info("End GUI Game");
            System.exit(0);
        });

        primaryStage.show();
        primaryStage.toFront();
        startGame(true, game);
    }

}
