package five.edu.cn;

import java.awt.BorderLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import five.edu.cn.util.BackgroundMusic;
import five.edu.cn.view.ChatPanel;
import five.edu.cn.view.ChessPanel;
import five.edu.cn.view.NetPanel;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(Main.class.getName());
    private static final String WINDOW_TITLE = "五子棋";
    private static final int DEFAULT_WIDTH = 800;
    private static final int DEFAULT_HEIGHT = 600;

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to set system look and feel", e);
        }

        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });

        startBackgroundMusic();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame(WINDOW_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        try {
            frame.add(ChessPanel.getInstance(), BorderLayout.CENTER);
            frame.add(NetPanel.getInstance(), BorderLayout.NORTH);
            frame.add(ChatPanel.getInstance(), BorderLayout.EAST);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize UI components", e);
            System.exit(1);
        }

        frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        LOGGER.info("Game window created successfully");
    }

    private static void startBackgroundMusic() {
        new Thread(() -> {
            try {
                BackgroundMusic music = BackgroundMusic.getInstance();
                if (music.initialize()) {
                    music.play(true);
                    LOGGER.info("Background music started");
                } else {
                    LOGGER.warning("Failed to initialize background music");
                }
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error starting background music", e);
            }
        }, "BackgroundMusic-Thread").start();
    }
}
