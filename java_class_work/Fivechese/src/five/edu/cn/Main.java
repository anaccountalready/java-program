package five.edu.cn;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

public class Main {

    public static void main(String a[]) {
        JFrame f = new JFrame("五子棋对战");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new BorderLayout());
        
        JPanel mainPanel = new JPanel(new BorderLayout());
        
        NetPanel netPanel = NetPanel.getInstance();
        netPanel.setPreferredSize(new Dimension(1200, 80));
        netPanel.setMinimumSize(new Dimension(1000, 80));
        
        ChessPanel chessPanel = ChessPanel.getInstance();
        chessPanel.setPreferredSize(new Dimension(750, 650));
        
        JScrollPane chessScrollPane = new JScrollPane(chessPanel);
        chessScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        chessScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        chessScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        chessScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        
        Chatpanl chatPanel = Chatpanl.getInstance();
        chatPanel.setPreferredSize(new Dimension(300, 600));
        
        mainPanel.add(netPanel, BorderLayout.NORTH);
        mainPanel.add(chessScrollPane, BorderLayout.CENTER);
        mainPanel.add(chatPanel, BorderLayout.EAST);
        
        f.add(mainPanel, BorderLayout.CENTER);
        f.setSize(1200, 850);
        f.setMinimumSize(new Dimension(1100, 750));
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
