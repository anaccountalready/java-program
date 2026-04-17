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

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.border.LineBorder;

public class Main {

    public static void main(String a[]) {
        JFrame f = new JFrame("🎮 五子棋对战");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout(0, 0));
        
        JPanel mainPanel = new JPanel(new BorderLayout(0, 0));
        mainPanel.setBackground(new Color(210, 180, 140));
        
        NetPanel netPanel = NetPanel.getInstance();
        netPanel.setBorder(new LineBorder(new Color(101, 67, 33), 2, true));
        netPanel.setOpaque(true);
        netPanel.setBackground(new Color(139, 90, 43));
        netPanel.setPreferredSize(new Dimension(1000, 60));
        netPanel.setMinimumSize(new Dimension(800, 60));
        
        ChessPanel chessPanel = ChessPanel.getInstance();
        chessPanel.setOpaque(false);
        chessPanel.setPreferredSize(new Dimension(750, 650));
        
        JScrollPane chessScrollPane = new JScrollPane(chessPanel);
        chessScrollPane.setOpaque(false);
        chessScrollPane.getViewport().setOpaque(false);
        chessScrollPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        chessScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chessScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chessScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chessScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        chessScrollPane.setBackground(new Color(210, 180, 140));
        
        Chatpanl chatPanel = Chatpanl.getInstance();
        chatPanel.setOpaque(true);
        chatPanel.setBackground(new Color(245, 222, 179));
        chatPanel.setBorder(new LineBorder(new Color(139, 90, 43), 2, true));
        chatPanel.setPreferredSize(new Dimension(280, 600));
        
        mainPanel.add(netPanel, BorderLayout.NORTH);
        mainPanel.add(chessScrollPane, BorderLayout.CENTER);
        mainPanel.add(chatPanel, BorderLayout.EAST);
        
        f.add(mainPanel, BorderLayout.CENTER);
        f.setSize(1200, 850);
        f.setMinimumSize(new Dimension(1000, 700));
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

