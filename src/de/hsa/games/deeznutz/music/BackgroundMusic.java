package de.hsa.games.deeznutz.music;

import de.hsa.games.deeznutz.Launcher;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.logging.Logger;

public class BackgroundMusic {
    private final static Logger logger = Logger.getLogger(Launcher.class.getName());

    private Clip clip;
    private int volume = 0;

    public BackgroundMusic(String fileName) {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(BackgroundMusic.class.getResource(fileName));
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            logger.severe(e.getMessage());
        }
    }

    public void loop() {
        clip.start();
        clip.setFramePosition(0);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }

    public void toggleMusic() {
        if (clip.isActive())
            clip.stop();
        else
            clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void decreaseVolume() {
        volume -= 1;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }

    public void increaseVolume() {
        volume += 1;
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }


}
