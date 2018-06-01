package de.hsa.games.deeznutz;

import de.hsa.games.deeznutz.gui.FxUI;
import de.hsa.games.deeznutz.music.BackgroundMusic;
import de.hsa.games.deeznutz.console.ScanException;
import de.hsa.games.deeznutz.core.*;
import javafx.application.Application;
import javafx.stage.Stage;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Launcher extends Application {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private static Launcher launcher = new Launcher();
    private static BoardConfig boardConfig = new BoardConfig("default.properties");
    private Scanner scanner = new Scanner(System.in);
    private static Game game;

    public static void main(String[] args) {
        launcher.menu();
    }

    private void menu() {
        System.out.println("Choose Game Mode: [1] Multithr. Console [2] Singlethr. Console [3] GUI [4] Exit");
        switch (scanner.nextInt()) {
            case 1:
                logger.info("Game Type: Multithreaded Console");
                fighterMenu(true, "console");
                break;
            case 2:
                logger.log(Level.INFO, "Game Type: Singlethreaded Console");
                fighterMenu(false, "console");
                break;
            case 3:
                logger.log(Level.INFO, "Game Type: GUI");
                fighterMenu(true, "gui");
                break;
            case 4:
                logger.log(Level.INFO, "Game Menu Exit");
                System.exit(0);
            default:
                logger.log(Level.INFO, "User is retarded, choosing Game Type: GUI");
                fighterMenu(true, "gui");
                break;
        }
    }

    private void fighterMenu(boolean threaded, String display) {
        System.out.println("Choose Fight Mode: [1] Single [2] Against Bot [3] Bot only [4] Exit");
        switch (scanner.nextInt()) {
            case 1:
                logger.log(Level.INFO, "Fight Mode: single");
                game = new GameImpl(threaded, boardConfig);
                break;
            case 2:
                logger.log(Level.INFO, "Fight Mode: Bot against User");
                game = new GameImplBotUser(threaded, boardConfig);
                break;
            case 3:
                logger.log(Level.INFO, "Fight Mode: Bots only");
                game = new GameImplBotOnly(threaded, boardConfig);
                break;
            case 4:
                logger.log(Level.INFO, "Fighter Menu Exit");
                System.exit(0);
            default:
                logger.log(Level.INFO, "User is retarded, choosing Fight Mode: single");
                game = new GameImpl(threaded, boardConfig);
                break;
        }

        if (display.equalsIgnoreCase("gui"))
            Application.launch();
        else
            startGame(threaded, game);
    }

    private static void startGame(boolean threaded, Game game) {
        if (threaded) {
            logger.log(Level.INFO, "Start Game Multithreaded");

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
            timer.schedule(timerTask, 2000, 1);
            game.ui.multiThreadCommandProcess();
        } else {
            logger.log(Level.INFO, "Start Game Singlethreaded");

            game.run();
        }
    }

    @Override
    public void start(Stage primaryStage) throws ScanException {
        logger.log(Level.INFO, "Start GUI Game");

        FxUI fxUI = FxUI.createInstance(boardConfig.getBoardSize());
        BackgroundMusic.backgroundMusic.loop();

        game.setUi(fxUI);
        fxUI.setGame(game);
        primaryStage.setScene(fxUI);
        primaryStage.setTitle("DEEZNUTZ");

        fxUI.getWindow().setOnCloseRequest(evt -> {
            logger.log(Level.INFO, "End GUI Game");
            System.exit(-1);
        });

        primaryStage.show();
        startGame(true, game);
    }

}
