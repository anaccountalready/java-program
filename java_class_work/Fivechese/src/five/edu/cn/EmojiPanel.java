package five.edu.cn;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class EmojiPanel extends JDialog {
    private static final long serialVersionUID = 1L;
    
    private String selectedEmoji = null;
    private static final String[] EMOJIS = {
        "😀", "😂", "🤣", "😊", "😍", "🤔", "😎", "🥳",
        "😢", "😡", "😱", "👍", "👎", "❤️", "🎉", "🔥",
        "⭐", "🌟", "✨", "💯", "🎮", "♟️", "⚫", "⚪",
        "🏆", "🎯", "💪", "👋", "🙏", "👏", "🤝", "🌹"
    };
    
    public EmojiPanel(java.awt.Frame parent) {
        super(parent, "选择表情", true);
        setUndecorated(true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
        mainPanel.setBorder(new LineBorder(new Color(100, 100, 100), 2, true));
        mainPanel.setBackground(new Color(245, 245, 220));
        
        JLabel title = new JLabel("表情");
        title.setFont(new Font("微软雅黑", Font.BOLD, 14));
        title.setBorder(new EmptyBorder(5, 10, 5, 10));
        
        JPanel emojiGrid = new JPanel();
        emojiGrid.setLayout(new GridLayout(4, 8, 5, 5));
        emojiGrid.setBackground(new Color(245, 245, 220));
        emojiGrid.setBorder(new EmptyBorder(5, 5, 5, 5));
        
        for (final String emoji : EMOJIS) {
            JButton emojiButton = new JButton(emoji);
            emojiButton.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 24));
            emojiButton.setPreferredSize(new Dimension(45, 45));
            emojiButton.setFocusPainted(false);
            emojiButton.setBackground(Color.WHITE);
            emojiButton.setBorder(new LineBorder(new Color(200, 200, 200), 1, true));
            emojiButton.setContentAreaFilled(true);
            emojiButton.setOpaque(true);
            
            emojiButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectedEmoji = emoji;
                    dispose();
                }
            });
            
            emojiButton.addMouseListener(new java.awt.event.MouseAdapter() {
                public void mouseEntered(java.awt.event.MouseEvent evt) {
                    emojiButton.setBackground(new Color(230, 230, 250));
                }
                public void mouseExited(java.awt.event.MouseEvent evt) {
                    emojiButton.setBackground(Color.WHITE);
                }
            });
            
            emojiGrid.add(emojiButton);
        }
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.setBackground(new Color(245, 245, 220));
        
        JButton closeButton = new JButton("关闭");
        closeButton.setFont(new Font("微软雅黑", Font.PLAIN, 12));
        closeButton.setFocusPainted(false);
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });
        buttonPanel.add(closeButton);
        
        mainPanel.add(title);
        mainPanel.add(emojiGrid);
        mainPanel.add(buttonPanel);
        
        add(mainPanel);
        pack();
        setLocationRelativeTo(parent);
    }
    
    public String getSelectedEmoji() {
        return selectedEmoji;
    }
    
    public static String showEmojiDialog(java.awt.Frame parent) {
        EmojiPanel dialog = new EmojiPanel(parent);
        dialog.setVisible(true);
        return dialog.getSelectedEmoji();
    }
}
