package de.hsa.games.deeznutz.gui;

import de.hsa.games.deeznutz.Game;
import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.UI;
import de.hsa.games.deeznutz.console.CommandTypeInfo;
import de.hsa.games.deeznutz.console.GameCommandType;
import de.hsa.games.deeznutz.console.NotEnoughEnergyException;
import de.hsa.games.deeznutz.core.BoardView;
import de.hsa.games.deeznutz.core.MoveCommand;
import de.hsa.games.deeznutz.core.XY;
import de.hsa.games.deeznutz.core.XYsupport;
import de.hsa.games.deeznutz.entities.Entity;
import de.hsa.games.deeznutz.entities.MasterSquirrel;
import de.hsa.games.deeznutz.entities.MiniSquirrel;
import de.hsa.games.deeznutz.music.BackgroundMusic;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class FxUI extends Scene implements UI {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private static final double CELL_SIZE = 20;
    private static MoveCommand command;
    private final Label msgLabel;
    private final Canvas boardCanvas;
    private Game game;
    private BackgroundMusic backgroundMusic;


    private FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.msgLabel = msgLabel;
        this.boardCanvas = boardCanvas;
        this.backgroundMusic = new BackgroundMusic("bolt.wav");
        backgroundMusic.loop();
    }

    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
        Label statusLabel = new Label();
        statusLabel.setMinHeight(50);
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel);
        statusLabel.setText("Get ready to rumble!");
        fxUI.setOnKeyPressed(control(fxUI));
        return fxUI;
    }

    /**
     * Handle all the key inputs to move or to print something for example.
     *
     * @param fxUI -fxUI
     * @return command
     */
    private static EventHandler<KeyEvent> control(FxUI fxUI) {
        return keyEvent -> {
            switch (keyEvent.getCode()) {
                case W:
                case UP:
                    command = new MoveCommand(XY.UP);
                    break;
                case D:
                case RIGHT:
                    command = new MoveCommand(XY.RIGHT);
                    break;
                case S:
                case DOWN:
                    command = new MoveCommand(XY.DOWN);
                    break;
                case A:
                case LEFT:
                    command = new MoveCommand(XY.LEFT);
                    break;
                case M:
                    try {
                        int energy = 100;

                        MasterSquirrel daddy = fxUI.game.getState().getBoard().getMainMasterSquirrel();
                        XY direction = XYsupport.generateRandomMoveVector();

                        if (fxUI.game.getState().getBoard().getMainMasterSquirrel().getEnergy() >= energy) {
                            MiniSquirrel mini = new MiniSquirrel(energy, daddy.getLocation().addVector(direction), daddy);
                            fxUI.game.getState().getBoard().insert(mini);
                            daddy.updateEnergy(-energy);
                        } else {
                            throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (fxUI.game.getState().getBoard().getMainMasterSquirrel().getEnergy()) + " Energie");
                        }
                    } catch (NotEnoughEnergyException e) {
                        logger.warning(e.getMessage());
                    }
                    break;
                case Q:
                case ESCAPE:
                    fxUI.game.state.saveHighscores();
                    logger.info("End GUI Game.");
                    System.exit(0);
                    break;
                case P:
                    fxUI.game.state.togglePause();
                    break;
                case T:
                    fxUI.game.state.printHighscores();
                    break;
                case X:
                    System.out.println(fxUI.game.state.getBoard());
                    break;
                case K:
                    fxUI.game.increaseFps(2);
                    break;
                case J:
                    fxUI.game.decreaseFps(2);
                    break;
                case H:
                    fxUI.game.state.togglePause();
                    GridPane helpPane = new GridPane();
                    helpPane.getChildren().add(new Label(fxUI.help()));
                    helpPane.setMinSize(400, 170);
                    Scene helpScene = new Scene(helpPane);
                    Stage helpStage = new Stage();
                    helpStage.setScene(helpScene);
                    helpStage.show();
                    helpStage.setTitle("HELP");
                    fxUI.help();
                    break;
                case E:
                    fxUI.game.state.togglePause();
                    GridPane entittyPane = new GridPane();
                    entittyPane.getChildren().add(new Label(fxUI.all()));
                    ScrollPane sp = new ScrollPane();
                    sp.setContent(entittyPane);
                    Scene entityScene = new Scene(sp, 400, 650);
                    Stage entityStage = new Stage();
                    entityStage.setScene(entityScene);
                    entityStage.show();
                    entityStage.setTitle(fxUI.game.state.getBoard().getEntities().length + " entites in the game");
                    fxUI.all();
                    break;
                case I:
                    fxUI.backgroundMusic.stopMusic();
                    break;
                case O:
                    fxUI.backgroundMusic.startMusic();
                    break;
                case PLUS:
                    fxUI.backgroundMusic.increaseVolume();
                    break;
                case MINUS:
                    fxUI.backgroundMusic.decreaseVolume();
            }
        };
    }

    @Override
    public void render(final BoardView view) {
        Platform.runLater(() -> repaintBoardCanvas(view));
    }

    @Override
    public void multiThreadCommandProcess() {
    }

    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Paint the whole board
     *
     * @param view BoardView
     */

    private void repaintBoardCanvas(BoardView view) {
        message("");
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
        XY viewSize = view.getSize();
        for (int x = 0; x < viewSize.getX(); x++) {
            for (int y = 0; y < viewSize.getY(); y++) {
                PrintEntity(view, gc, x, y);
            }
        }
        printImplosion(gc);
    }

    /**
     * Print the Implosion radius if a miniSquirrel implode
     *
     * @param gc GraphicsContext
     */

    private void printImplosion(GraphicsContext gc) {
        String master1 = game.state.getBoard().getMainMasterSquirrel().getName();
        Color color;
        int radius = game.state.getBoard().getImplosionRadius();

        for (MiniSquirrel miniSquirrel : game.state.getBoard().getMiniList()) {
            if (miniSquirrel.getName().equalsIgnoreCase(master1)) {
                color = Color.rgb(45, 45, 45, 0.2);
            } else {
                color = Color.rgb(231, 70, 98, 0.2);
            }
            gc.setFill(color);
            gc.fillOval(miniSquirrel.getLocation().getX() * CELL_SIZE + CELL_SIZE / 2 - CELL_SIZE * radius, miniSquirrel.getLocation().getY() * CELL_SIZE + CELL_SIZE / 2 - CELL_SIZE * radius, CELL_SIZE * 2 * radius, CELL_SIZE * 2 * radius);
        }

    }

    /**
     * Print all the Entitys
     * the goodEntities are circles ant the badEntyties are squares.
     *
     * @param view BoardView
     * @param gc   GraphicContext
     * @param x    xPosition
     * @param y    yPosition
     */

    private void PrintEntity(BoardView view, GraphicsContext gc, int x, int y) {
        if (view.getEntityType(x, y) != null) {
            switch (view.getEntityType(x, y)) {
                case NOTHING:
                    //gc.setFill(Color.rgb(210, 210, 210));
                    //gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
                case MASTER_SQUIRREL:
                case MASTER_SQUIRREL_BOT:
                    String master1 = game.state.getBoard().getMainMasterSquirrel().getName();

                    if (view.getEntity(x, y).getName().equalsIgnoreCase(master1)) {
                        gc.setFill(Color.rgb(45, 45, 45));
                        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        break;
                    } else {
                        gc.setFill(Color.rgb(231, 70, 98));
                        gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        break;
                    }

                case MINI_SQUIRREL:
                case MINI_SQUIRREL_BOT:
                    master1 = game.state.getBoard().getMainMasterSquirrel().getName();

                    if (view.getEntity(x, y).getName().equalsIgnoreCase(master1)) {
                        gc.setFill(Color.rgb(51, 51, 51));
                        gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        break;
                    } else {
                        gc.setFill(Color.rgb(231, 70, 98));
                        gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        break;
                    }

                case GOOD_PLANT:
                    gc.setFill(Color.FORESTGREEN);
                    gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
                case BAD_PLANT:
                    gc.setFill(Color.DARKGREEN);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
                case BAD_BEAST:
                    gc.setFill(Color.SADDLEBROWN);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
                case GOOD_BEAST:
                    gc.setFill(Color.ROSYBROWN);
                    gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
                case WALL:
                    gc.setFill(Color.GRAY);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
            }
        }
    }

    /**
     * Shows the Bot energys, the remainingGameTime, the rounds, the fps and pause if the game is paused
     *
     * @param msg String
     */

    private void message(final String msg) {
        String message = msg + game.message() + "\n Remaining Rounds: " + game.state.getGameRounds() + " Remaining Time: " + game.state.getGameDuration() + " FPS: " + game.getFps() + ((game.state.isGamePause()) ? " -PAUSED-" : "");
        Platform.runLater(() -> msgLabel.setText(message));
    }

    @Override
    public MoveCommand getCommand() {
        if (command == null)
            return new MoveCommand(new XY(0, 0));
        else {
            MoveCommand temp = command;
            command = null;
            return temp;
        }
    }

    public String all() {
        logger.info("Print all Entities");

        Entity[] entities = game.state.getBoard().getEntities();
        int iMax = entities.length - 1;

        StringBuilder b = new StringBuilder();
        for (int i = 0; ; i++) {
            b.append(String.valueOf(entities[i]));
            b.append("\n");
            if (i == iMax) {
                return b.toString();
            }
        }
    }

    public String help() {
        logger.info("Print help");
        List<String> help = new ArrayList<>();

        for (CommandTypeInfo cmdti : GameCommandType.values()) {
            help.add(cmdti.getCommandFxUI() + " " + cmdti.getHelpText());
        }
        return help.stream().collect(Collectors.joining("\15" + "\n"));
    }
}
