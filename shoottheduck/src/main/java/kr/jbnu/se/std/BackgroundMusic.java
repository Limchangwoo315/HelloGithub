package kr.jbnu.se.std;

import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class BackgroundMusic {
    private Clip clip;

    public BackgroundMusic() {
        try {
            // Load the WAV file
            URL musicUrl = this.getClass().getResource("/Music/BackgroundMusic.wav");
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicUrl);

            // Get a clip resource
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.setFramePosition(0); // Rewind to the beginning
            clip.start();
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void loop() {
        if (clip != null) {
            clip.loop(Clip.LOOP_CONTINUOUSLY); // Play in a loop
        }
    }
}
