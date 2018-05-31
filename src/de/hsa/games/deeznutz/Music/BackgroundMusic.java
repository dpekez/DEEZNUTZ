package de.hsa.games.deeznutz.Music;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BackgroundMusic {
    private final static Logger logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    public static BackgroundMusic backgroundMusic = new BackgroundMusic("bolt.wav");
    private Clip clip;

    private BackgroundMusic(String fileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(BackgroundMusic.class.getResource(fileName));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            logger.log(Level.SEVERE, "A exception was thrown", e);
        }
    }

    public void loop() {
            if (clip != null) {
                new Thread(() -> {
                    synchronized (clip) {
                        clip.stop();
                        clip.setFramePosition(0);
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(-20);
                    }
                }).start();
            }
    }
}
