package five.edu.cn.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import five.edu.cn.controller.Control;

public class ChatPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(ChatPanel.class.getName());
    private static final String DEFAULT_BACKGROUND_IMAGE = "painting/R-C.jpg";

    private static volatile ChatPanel instance;
    private static final Object LOCK = new Object();

    private final JScrollPane scrollPane;
    private final JButton sendButton;
    private final JTextField inputField;
    private final JTextArea displayArea;
    private final JPanel topPanel;
    private final JPanel bottomPanel;
    private Image backgroundImage;

    private ChatPanel() {
        this.scrollPane = new JScrollPane();
        this.sendButton = new JButton("发送");
        this.inputField = new JTextField();
        this.displayArea = new JTextArea();
        this.topPanel = new JPanel();
        this.bottomPanel = new JPanel();

        initializeComponents();
        loadBackgroundImage();
    }

    public static ChatPanel getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ChatPanel();
                }
            }
        }
        return instance;
    }

    private void initializeComponents() {
        setLayout(new BorderLayout());

        displayArea.setEditable(false);
        displayArea.setLineWrap(true);
        displayArea.setWrapStyleWord(true);
        scrollPane.setViewportView(displayArea);

        topPanel.add(new JLabel("消息显示区"));
        topPanel.add(scrollPane);

        bottomPanel.add(new JLabel("消息输入区"));
        bottomPanel.add(inputField, BorderLayout.WEST);
        bottomPanel.add(sendButton, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(bottomPanel, BorderLayout.SOUTH);

        sendButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        inputField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateLayout();
            }
        });
    }

    private void loadBackgroundImage() {
        try {
            File imageFile = new File(DEFAULT_BACKGROUND_IMAGE);
            if (imageFile.exists()) {
                backgroundImage = new ImageIcon(DEFAULT_BACKGROUND_IMAGE).getImage();
            } else {
                URL resourceUrl = getClass().getClassLoader().getResource(DEFAULT_BACKGROUND_IMAGE);
                if (resourceUrl != null) {
                    backgroundImage = new ImageIcon(resourceUrl).getImage();
                } else {
                    LOGGER.warning("Background image not found: " + DEFAULT_BACKGROUND_IMAGE);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load background image", e);
        }
    }

    private void updateLayout() {
        if (getParent() == null) {
            return;
        }

        int width = getParent().getWidth();
        int height = getParent().getHeight();

        sendButton.setMinimumSize(new Dimension(5, 5));
        inputField.setMinimumSize(new Dimension(100, 20));
        scrollPane.setMinimumSize(new Dimension(200, 5));
        scrollPane.setPreferredSize(new Dimension(width / 3, height / 2));
        displayArea.setPreferredSize(new Dimension(width / 3, height / 2));
        inputField.setPreferredSize(new Dimension(width / 3, height / 4));
    }

    private void sendMessage() {
        String text = inputField.getText();
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        Control.getInstance().handleChatSend(text.trim());
        inputField.setText("");
    }

    public void appendMessage(String message) {
        if (message == null) {
            return;
        }
        displayArea.append(message + "\n");
        displayArea.setCaretPosition(displayArea.getDocument().getLength());
    }

    public void clearMessages() {
        displayArea.setText("");
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}
