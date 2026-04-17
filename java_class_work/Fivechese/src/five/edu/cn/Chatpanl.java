package five.edu.cn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class Chatpanl extends JPanel {
    private static Chatpanl instance = new Chatpanl();
    private JScrollPane scrollpane = new JScrollPane();
    private JButton sendbt = new JButton("发送");
    private JButton emojiBt = new JButton("😊");
    private JTextField writeboard = new JTextField();
    public JTextArea readboard = new JTextArea();
    private JPanel pane1 = new JPanel();
    private JPanel pane2 = new JPanel();
    private JPanel inputPanel = new JPanel();
    Image imageIcon = new ImageIcon("painting/R-C.jpg").getImage();

    private Chatpanl() {
        setOpaque(false);
        setBorder(new EmptyBorder(10, 10, 10, 10));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                int width = getWidth();
                int height = getHeight();
                scrollpane.setPreferredSize(new Dimension(width - 30, height / 2));
                readboard.setPreferredSize(new Dimension(width - 30, height / 2));
                writeboard.setPreferredSize(new Dimension(width - 100, 30));
            }
        });

        setupUI();
        setupListeners();
    }

    private void setupUI() {
        setLayout(new BorderLayout(10, 10));

        pane1.setOpaque(false);
        pane1.setLayout(new BorderLayout(5, 5));

        JLabel titleLabel = new JLabel("💬 聊天窗口");
        titleLabel.setFont(new Font("微软雅黑", Font.BOLD, 16));
        titleLabel.setForeground(new Color(70, 70, 70));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setBorder(new EmptyBorder(5, 0, 5, 0));

        readboard.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        readboard.setEditable(false);
        readboard.setLineWrap(true);
        readboard.setWrapStyleWord(true);
        readboard.setBackground(new Color(255, 255, 240, 230));
        readboard.setForeground(new Color(50, 50, 50));
        readboard.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        scrollpane.setViewportView(readboard);
        scrollpane.setOpaque(false);
        scrollpane.getViewport().setOpaque(false);
        scrollpane.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        scrollpane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollpane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        pane1.add(titleLabel, BorderLayout.NORTH);
        pane1.add(scrollpane, BorderLayout.CENTER);

        inputPanel.setOpaque(false);
        inputPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel inputLabel = new JLabel("输入:");
        inputLabel.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        inputLabel.setForeground(new Color(70, 70, 70));

        writeboard.setFont(new Font("微软雅黑", Font.PLAIN, 14));
        writeboard.setPreferredSize(new Dimension(150, 35));
        writeboard.setBackground(new Color(255, 255, 255, 230));
        writeboard.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        writeboard.setMargin(new Insets(5, 8, 5, 8));

        emojiBt.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 16));
        emojiBt.setPreferredSize(new Dimension(45, 35));
        emojiBt.setFocusPainted(false);
        emojiBt.setBackground(new Color(230, 230, 250));
        emojiBt.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        emojiBt.setToolTipText("选择表情");

        sendbt.setFont(new Font("微软雅黑", Font.BOLD, 13));
        sendbt.setPreferredSize(new Dimension(70, 35));
        sendbt.setFocusPainted(false);
        sendbt.setBackground(new Color(100, 180, 255));
        sendbt.setForeground(Color.WHITE);
        sendbt.setBorder(new LineBorder(new Color(80, 160, 235), 1, true));
        sendbt.setOpaque(true);

        gbc.insets = new Insets(2, 2, 2, 2);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(inputLabel, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1.0;
        inputPanel.add(writeboard, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.weightx = 0;
        gbc.fill = GridBagConstraints.NONE;
        inputPanel.add(emojiBt, gbc);

        gbc.gridx = 2;
        gbc.gridy = 1;
        inputPanel.add(sendbt, gbc);

        pane2.setOpaque(false);
        pane2.setLayout(new BorderLayout());
        pane2.add(inputPanel, BorderLayout.CENTER);

        add(pane1, BorderLayout.CENTER);
        add(pane2, BorderLayout.SOUTH);
    }

    private void setupListeners() {
        sendbt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                sendMessage();
            }
        });

        writeboard.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    sendMessage();
                }
            }
        });

        emojiBt.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String emoji = EmojiPanel.showEmojiDialog(getParentFrame());
                if (emoji != null) {
                    writeboard.setText(writeboard.getText() + emoji);
                    writeboard.requestFocus();
                }
            }
        });
    }

    private java.awt.Frame getParentFrame() {
        java.awt.Component comp = this;
        while (comp != null && !(comp instanceof java.awt.Frame)) {
            comp = comp.getParent();
        }
        return (java.awt.Frame) comp;
    }

    private void sendMessage() {
        String text = writeboard.getText().trim();
        if (!text.isEmpty()) {
            NetHelper.getInstance().setChat(text);
            writeboard.setText("");
        }
    }

    public static Chatpanl getInstance() {
        return instance;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (imageIcon != null) {
            g.drawImage(imageIcon, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}
