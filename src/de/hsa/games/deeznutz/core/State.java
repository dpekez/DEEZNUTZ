package de.hsa.games.deeznutz.core;

import de.hsa.games.deeznutz.Launcher;
import de.hsa.games.deeznutz.entities.Entity;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Properties;
import java.util.logging.Logger;

public class State {
    private static final Logger logger = Logger.getLogger(Launcher.class.getName());

    private Board board;
    private BoardConfig boardConfig;
    private int gameDuration;
    private int gameRounds;
    private boolean gamePause = false;
    private HashMap<String, Integer> highScores;
    private HashMap<String, ArrayList<Integer>> superHighScores;
    private String highScoresFile = "highscores.properties";

    public State(BoardConfig boardConfig) {
        this.boardConfig = boardConfig;
        board = new Board(boardConfig);
        highScores = new HashMap<>();
        superHighScores = new HashMap<>();
        loadHighscores();
        printHighscores();
        gameDuration = board.getConfig().getGameDuration();
        gameRounds = board.getConfig().getGameRounds();
    }

    public Board getBoard() {
        return board;
    }

    public int getGameDuration() {
        return gameDuration;
    }

    public int getGameRounds() {
        return gameRounds;
    }

    public void togglePause() {
        gamePause = !gamePause;
    }

    public boolean isGamePause() {
        return gamePause;
    }

    public void update() {
        if (gamePause)
            return;

        if (gameDuration == 0) {
            updateHighscores();
            printHighscores();
            togglePause();
            gameDuration = board.getConfig().getGameDuration();
            gameRounds--;
            board = new Board(boardConfig);
            return;
        }

        if (gameRounds == 0) {
            saveHighscores();
            logger.info("End GUI Game.");
            System.exit(0);
        }

        board.setGameDurationLeft(gameDuration);
        logger.finest("Update FlattenedBoard.");
        board.update(flattenedBoard());
        gameDuration--;

    }


    private void updateHighscores() {
        for (Entity entity : board.getMasters()) {
            highScores.put(entity.getName(), entity.getEnergy());

            ArrayList<Integer> buffer;
            if (superHighScores.get(entity.getName()) != null) {
                buffer = superHighScores.get(entity.getName());
            } else {
                buffer = new ArrayList<>();
            }

            buffer.add(entity.getEnergy());

            superHighScores.put(entity.getName(), buffer);
        }
    }

    public void saveHighscores() {
        Properties properties = new Properties();

        for (ArrayList<Integer> entry : superHighScores.values()) {
            Collections.sort(entry);
            Collections.reverse(entry);
        }

        for (HashMap.Entry<String, ArrayList<Integer>> entries : superHighScores.entrySet()) {
            properties.setProperty(entries.getKey(), entries.getValue().toString());
        }

        try {
            properties.store(new FileOutputStream(highScoresFile), "DEEZNUTS HIGHSCORES");
            logger.info("Saving highscores...");
        } catch (IOException e) {
            logger.warning("Saving highscores file failed!");
        }

    }

    public void loadHighscores() {
        Properties properties = new Properties();

        try {
            properties.load(new FileInputStream(highScoresFile));
            logger.info("Loading highscores...");
        } catch (IOException e) {
            logger.warning("Loading highscores file failed!");
        }

        String string;
        String[] strings;
        for (Object object : properties.stringPropertyNames()) {
            string = properties.getProperty(object.toString(), "");
            string = string.substring(1, string.length() - 1);
            string = string.replaceAll(" ", "");
            strings = string.split(",");

            if (strings.length > 5) {
                for (int i = 0; i < 5; i++) {
                    strings[i] = strings[i];
                }
            }

            ArrayList<Integer> values = new ArrayList<>();
            for (String stringToParse : strings) {
                values.add(Integer.parseInt(stringToParse));
            }

            superHighScores.put(object.toString(), values);
        }

    }

    public void printHighscores() {

        for (ArrayList<Integer> entry : superHighScores.values()) {
            Collections.sort(entry);
            Collections.reverse(entry);
        }

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("Highscore List:\n");
        for (HashMap.Entry<String, ArrayList<Integer>> entries : superHighScores.entrySet()) {
            stringBuilder.append(entries.getKey()).append(": ");

            for (Integer entry : entries.getValue()) {
                stringBuilder.append(entry).append(" ");
            }

            if (highScores.get(entries.getKey()) != null)
                stringBuilder.append(" // This Round: ").append(highScores.get(entries.getKey())).append("\n");
            else
                stringBuilder.append("\n");

        }

        System.out.println(stringBuilder);
    }

    FlattenedBoard flattenedBoard() {
        return new FlattenedBoard(board);
    }

}
