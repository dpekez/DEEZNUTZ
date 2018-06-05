package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.entities.Entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

public class State {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private Board board;
    private HashMap<String, Integer> highScores;

    public State(BoardConfig boardConfig) {
        board = new Board(boardConfig);
        highScores = new HashMap<>();
        loadHighscores();
    }

    public Board getBoard() {
        return board;
    }

    public void update() {
        logger.finest("Update FlattenedBoard.");
        board.update(flattenedBoard());

        updateHighscores();
    }

    private void updateHighscores() {
        for (Entity entity: board.getMasters()) {
            if (!highScores.containsKey(entity.getName()))
                highScores.put(entity.getName(), entity.getEnergy());
            if (highScores.get(entity.getName()) < entity.getEnergy())
                highScores.put(entity.getName(), entity.getEnergy());
        }
    }

    public void saveHighscores() {
        Properties properties = new Properties();

        properties.putAll(highScores);

        for (HashMap.Entry<String,Integer> entry: highScores.entrySet()) {
            properties.put(entry.getKey(), entry.getValue().toString());
        }

        try {
            properties.store(new FileOutputStream("highscores.properties"), "DEEZNUTS HIGHSCORES");
            logger.info("Saving highscores...");
        } catch (IOException e) {
            logger.warning("Saving highscores file failed!");
        }
    }

    public void loadHighscores() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream("highscores.properties"));
            logger.info("Loading highscores...");
        } catch (IOException e) {
            logger.warning("Loading highscores file failed!");
        }

        for (String key: properties.stringPropertyNames()) {
            highScores.put(key, Integer.parseInt(properties.getProperty(key)));
        }
    }

    public void printHighscores() {
        System.out.println(highScores);
    }

    FlattenedBoard flattenedBoard() {
        return new FlattenedBoard(board);
    }

}
