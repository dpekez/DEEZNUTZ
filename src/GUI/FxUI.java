package GUI;

import console.NotEnoughEnergyException;
import console.UI;
import core.BoardView;
import core.GameImpl;
import core.MoveCommand;
import core.XY;
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


public class FxUI extends Scene implements UI {

    private static final double CELL_SIZE = 15;
    private final Label msgLabel;
    private final Canvas boardCanvas;
    private static MoveCommand command;
    private GameImpl gameimpl;


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
                    command = new MoveCommand(new XY(0, -1));
                    break;
                case D:
                case RIGHT:
                    command = new MoveCommand(new XY(1, 0));
                    break;
                case S:
                case DOWN:
                    command = new MoveCommand(new XY(0, 1));
                    break;
                case A:
                case LEFT:
                    command = new MoveCommand(new XY(-1, 0));
                    break;
                case M:
                    try {
                        fxUI.gameimpl.spawnMiniSquirrel(100, 1, 0);
                    } catch (NotEnoughEnergyException e) {
                        e.printStackTrace();
                    }
                    break;
                case Q:
                case ESCAPE:
                    fxUI.gameimpl.exit();
                    break;
                case X:
                    fxUI.gameimpl.all();
                    break;
                case H:
                    fxUI.gameimpl.help();
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
        this.gameimpl = game;
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
            }
        }
    }

    private void message(final String msg) {
        String message = msg + gameimpl.update();
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
