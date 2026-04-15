package five.edu.cn.util;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class BackgroundMusic implements Closeable, LineListener {
    private static final Logger LOGGER = Logger.getLogger(BackgroundMusic.class.getName());
    private static final String DEFAULT_MUSIC_PATH = "music/bkmusic.wav";

    private static volatile BackgroundMusic instance;
    private static final Object LOCK = new Object();

    private Clip clip;
    private AudioInputStream audioInputStream;
    private volatile boolean isPlaying;
    private volatile boolean isInitialized;
    private String musicPath;

    private BackgroundMusic() {
        this.musicPath = DEFAULT_MUSIC_PATH;
        this.isPlaying = false;
        this.isInitialized = false;
    }

    public static BackgroundMusic getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new BackgroundMusic();
                }
            }
        }
        return instance;
    }

    public void setMusicPath(String path) {
        if (path == null || path.trim().isEmpty()) {
            LOGGER.warning("Music path is null or empty");
            return;
        }
        this.musicPath = path;
        this.isInitialized = false;
    }

    public boolean initialize() {
        if (isInitialized) {
            return true;
        }

        try {
            closeResources();

            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                URL resourceUrl = getClass().getClassLoader().getResource(musicPath);
                if (resourceUrl != null) {
                    audioInputStream = AudioSystem.getAudioInputStream(resourceUrl);
                } else {
                    LOGGER.warning("Music file not found: " + musicPath);
                    return false;
                }
            } else {
                audioInputStream = AudioSystem.getAudioInputStream(musicFile);
            }

            clip = AudioSystem.getClip();
            clip.addLineListener(this);
            clip.open(audioInputStream);
            isInitialized = true;
            LOGGER.info("Background music initialized successfully");
            return true;

        } catch (UnsupportedAudioFileException e) {
            LOGGER.log(Level.SEVERE, "Unsupported audio file format: " + musicPath, e);
            return false;
        } catch (LineUnavailableException e) {
            LOGGER.log(Level.SEVERE, "Audio line unavailable", e);
            return false;
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Error reading music file: " + musicPath, e);
            return false;
        }
    }

    public void play() {
        play(true);
    }

    public void play(boolean loop) {
        if (!isInitialized && !initialize()) {
            LOGGER.warning("Cannot play music: initialization failed");
            return;
        }

        if (clip == null) {
            LOGGER.warning("Clip is null, cannot play");
            return;
        }

        try {
            if (clip.isRunning()) {
                clip.stop();
            }
            clip.setFramePosition(0);
            clip.start();
            isPlaying = true;

            if (loop) {
                clip.loop(Clip.LOOP_CONTINUOUSLY);
            }

            LOGGER.info("Background music started");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error playing music", e);
        }
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPlaying = false;
            LOGGER.info("Background music stopped");
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            isPlaying = false;
            LOGGER.info("Background music paused");
        }
    }

    public void resume() {
        if (clip != null && !clip.isRunning() && isInitialized) {
            clip.start();
            isPlaying = true;
            LOGGER.info("Background music resumed");
        }
    }

    public boolean isPlaying() {
        return isPlaying && clip != null && clip.isRunning();
    }

    private void closeResources() {
        try {
            if (clip != null) {
                if (clip.isRunning()) {
                    clip.stop();
                }
                clip.close();
                clip = null;
            }
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Error closing clip", e);
        }

        try {
            if (audioInputStream != null) {
                audioInputStream.close();
                audioInputStream = null;
            }
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Error closing audio input stream", e);
        }

        isPlaying = false;
        isInitialized = false;
    }

    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type == LineEvent.Type.STOP) {
            isPlaying = false;
            LOGGER.fine("Music playback stopped");
        } else if (type == LineEvent.Type.START) {
            isPlaying = true;
            LOGGER.fine("Music playback started");
        } else if (type == LineEvent.Type.CLOSE) {
            isPlaying = false;
            isInitialized = false;
            LOGGER.fine("Audio line closed");
        }
    }

    @Override
    public void close() {
        closeResources();
        instance = null;
        LOGGER.info("BackgroundMusic instance closed");
    }

    public static void main(String[] args) {
        BackgroundMusic music = BackgroundMusic.getInstance();
        if (music.initialize()) {
            music.play(true);
        }
    }
}
