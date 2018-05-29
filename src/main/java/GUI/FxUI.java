package GUI;

import console.NotEnoughEnergyException;
import console.UI;
import core.BoardView;
import core.GameImpl;
import core.MoveCommand;
import core.XY;
import entities.MasterSquirrel;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.logging.Level;
import java.util.logging.Logger;

public class FxUI extends Scene implements UI {
    private static Logger logger = Logger.getLogger(FxUI.class.getName());

    private static final double CELL_SIZE = 15;
    private static MoveCommand command;
    private final Label msgLabel;
    private final Canvas boardCanvas;
    private GameImpl gameImpl;


    private FxUI(Parent parent, Canvas boardCanvas, Label msgLabel) {
        super(parent);
        this.msgLabel = msgLabel;
        this.boardCanvas = boardCanvas;
    }

    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel);
        statusLabel.setText("");
        fxUI.setOnKeyPressed(control(fxUI));
        return fxUI;
    }

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
                        int x = 1;
                        int y = 0;

                        MasterSquirrel daddy = fxUI.gameImpl.getState().getBoard().getMasterSquirrel();
                        XY direction = new XY(x, y);

                        if (fxUI.gameImpl.getState().getBoard().getMasterSquirrel().getEnergy() >= energy) {
                            fxUI.gameImpl.getState().getBoard().insertMiniSquirrel(energy, direction, daddy);
                        } else {
                            throw new NotEnoughEnergyException("Das MasterSquirrel hat nur " + (fxUI.gameImpl.getState().getBoard().getMasterSquirrel().getEnergy()) + " Energie");
                        }
                    } catch (NotEnoughEnergyException e) {
                        logger.log(Level.WARNING, e.getMessage());
                    }

                    break;
                case Q:
                case ESCAPE:
                    System.exit(0);
                    break;
                case X:
                    // todo: graphical entity window
                    // or simple console print out
                    break;
                case H:
                    // todo: graphical help window
                    // or simple console print out
                    break;
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

    public void setGameImpl(GameImpl game) {
        this.gameImpl = game;
    }

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
    }

    private void PrintEntity(BoardView view, GraphicsContext gc, int x, int y) {
        if (view.getEntityType(x, y) != null) {
            switch (view.getEntityType(x, y)) {
                case MASTER_SQUIRREL:
                    gc.setFill(Color.BLACK);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
                case MINI_SQUIRREL:
                    gc.setFill(Color.BLACK);
                    gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
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
                case MASTER_SQUIRREL_BOT:
                    gc.setFill(Color.RED);
                    gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
                case MINI_SQUIRREL_BOT:
                    gc.setFill(Color.RED);
                    gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    break;
            }
        }
    }

    private void message(final String msg) {
        String message = msg + gameImpl.update();
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

}
