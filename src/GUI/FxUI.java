package GUI;

import console.Command;
import console.GameCommandType;
import console.UI;
import core.BoardView;
import core.MoveCommand;
import core.XY;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;


public class FxUI extends Scene implements UI {

    private static Command command = new Command(GameCommandType.NOTHING, new Object[0]);
    private static int CELL_SIZE = 5;
    private static int miniSuirrelEnergy = 200;
    private final Canvas boardCanvas;
    private final Label msgLabel;

    private FxUI(Parent parent, Canvas boardCanvas, Label msgLabel, int CELL_SIZE) {
        super(parent);
        this.boardCanvas = boardCanvas;
        this.msgLabel = msgLabel;
        FxUI.CELL_SIZE = CELL_SIZE;
    }

    public static FxUI createInstance(XY boardSize) {
        Canvas boardCanvas = new Canvas(boardSize.getX() * CELL_SIZE, boardSize.getY() * CELL_SIZE);
        Label statusLabel = new Label();
        VBox top = new VBox();
        top.getChildren().add(boardCanvas);
        top.getChildren().add(statusLabel);
        statusLabel.setText("Info Anzeige");
        final FxUI fxUI = new FxUI(top, boardCanvas, statusLabel, CELL_SIZE);
        fxUI.setOnKeyPressed(
                keyEvent -> {
                    switch (keyEvent.getCode()) {
                        case W:
                        case UP:
                            command = new Command(GameCommandType.UP, new Object[0]);
                            break;
                        case D:
                        case RIGHT:
                            command = new Command(GameCommandType.RIGHT, new Object[0]);
                            break;
                        case S:
                        case DOWN:
                            command = new Command(GameCommandType.DOWN, new Object[0]);
                            break;
                        case A:
                        case LEFT:
                            command = new Command(GameCommandType.LEFT, new Object[0]);
                            break;
                        case SPACE:
                            command = new Command(GameCommandType.SPAWN_MINI, new Object[]{miniSuirrelEnergy});
                            break;
                        default:
                            command = new Command(GameCommandType.NOTHING, new Object[0]);
                    }
                }
        );
        return fxUI;
    }


    @Override
    public void render(final BoardView view) {
        Platform.runLater(() -> repaintBoardCanvas(view));
    }

    @Override
    public void multiThreadCommandProcess() {

    }

    private void repaintBoardCanvas(BoardView view) {
        GraphicsContext gc = boardCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, boardCanvas.getWidth(), boardCanvas.getHeight());
        XY viewSize = view.getSize();
    }

    private void printAllEntity(GraphicsContext gc, BoardView view) {
        for (int x = 0; x < boardCanvas.getWidth(); x++) {
            for (int y = 0; y < boardCanvas.getHeight(); y++) {
                if (view.getEntityType(x, y) != null) {
                    switch (view.getEntityType(x, y)) {
                        case MASTER_SQUIRREL:
                            gc.setFill(Color.BLACK);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        case MINI_SQUIRREL:
                            gc.setFill(Color.BLACK);
                            gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        case GOOD_PLANT:
                            gc.setFill(Color.FORESTGREEN);
                            gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        case BAD_PLANT:
                            gc.setFill(Color.DARKGREEN);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        case BAD_BEAST:
                            gc.setFill(Color.SADDLEBROWN);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        case GOOD_BEAST:
                            gc.setFill(Color.ROSYBROWN);
                            gc.fillOval(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                        case WALL:
                            gc.setFill(Color.GRAY);
                            gc.fillRect(x * CELL_SIZE, y * CELL_SIZE, CELL_SIZE, CELL_SIZE);
                    }
                }
            }
        }
    }

    public void message(final String msg) {
        Platform.runLater(() -> msgLabel.setText(msg));
    }

    @Override
    public MoveCommand getCommand() {
        return null;
    }
}
