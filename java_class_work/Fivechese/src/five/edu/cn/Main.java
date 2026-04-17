package five.edu.cn;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class Main {

    public static void main(String a[]) {
        JFrame f = new JFrame("🎮 五子棋对战");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        BackgroundPanel backgroundPanel = new BackgroundPanel();
        backgroundPanel.setLayout(new BorderLayout(5, 5));
        
        ChessPanel chessPanel = ChessPanel.getInstance();
        NetPanel netPanel = NetPanel.getInstance();
        Chatpanl chatPanel = Chatpanl.getInstance();
        
        chessPanel.setOpaque(false);
        chatPanel.setOpaque(false);
        
        chessPanel.setPreferredSize(new Dimension(700, 600));
        
        JScrollPane chessScrollPane = new JScrollPane(chessPanel);
        chessScrollPane.setOpaque(false);
        chessScrollPane.getViewport().setOpaque(false);
        chessScrollPane.setBorder(null);
        chessScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chessScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chessScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chessScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        backgroundPanel.add(netPanel, BorderLayout.NORTH);
        backgroundPanel.add(chessScrollPane, BorderLayout.CENTER);
        backgroundPanel.add(chatPanel, BorderLayout.EAST);
        
        f.setContentPane(backgroundPanel);
        f.setSize(1150, 800);
        f.setMinimumSize(new Dimension(900, 600));
        f.setLocationRelativeTo(null);
        f.setVisible(true);
        
        new Thread() {
            public void run() {
                new BackGroundMusic();
                BackGroundMusic.main(null);
            }
        }.start();
    }
}

class BackgroundPanel extends JPanel {
    private static final long serialVersionUID = 1L;
    private Image backgroundImage;
    private Image scaledImage;

    public BackgroundPanel() {
        setOpaque(true);
        try {
            backgroundImage = new ImageIcon("painting/view.jpg").getImage();
            if (backgroundImage != null && backgroundImage.getWidth(null) > 0) {
                scaledImage = backgroundImage;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (backgroundImage != null) {
                    int width = getWidth();
                    int height = getHeight();
                    if (width > 0 && height > 0) {
                        scaledImage = backgroundImage.getScaledInstance(
                            width, height, Image.SCALE_SMOOTH);
                    }
                }
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        
        if (scaledImage != null) {
            g2d.drawImage(scaledImage, 0, 0, getWidth(), getHeight(), this);
        } else {
            java.awt.GradientPaint gradient = new java.awt.GradientPaint(
                0, 0, new Color(139, 119, 101),
                getWidth(), getHeight(), new Color(160, 120, 90));
            g2d.setPaint(gradient);
            g2d.fillRect(0, 0, getWidth(), getHeight());
        }
    }
}

