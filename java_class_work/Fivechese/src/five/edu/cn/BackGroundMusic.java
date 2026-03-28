package five.edu.cn;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BackGroundMusic {
    private static BackGroundMusic instance;
    private Clip clip;
    private File musicFile;
    
    private BackGroundMusic() {
        musicFile = new File("music\\bkmusic.wav");
    }
    
    public static synchronized BackGroundMusic getInstance() {
        if (instance == null) {
            instance = new BackGroundMusic();
        }
        return instance;
    }
    
    public void play() {
        new Thread() {
            public void run() {
                try {
                    if (clip != null && clip.isRunning()) {
                        return;
                    }
                    AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(musicFile);
                    clip = AudioSystem.getClip();
                    clip.open(audioInputStream);
                    clip.start();
                    clip.loop(Clip.LOOP_CONTINUOUSLY);
                } catch (LineUnavailableException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (UnsupportedAudioFileException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    
    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }
}
