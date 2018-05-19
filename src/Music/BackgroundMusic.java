package Music;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class BackgroundMusic {
    public static BackgroundMusic sound1 = new BackgroundMusic("bolt.wav");
    private Clip clip;

    private BackgroundMusic(String fileName) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(BackgroundMusic.class.getResource(fileName));
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loop() {
        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
