package five.edu.cn;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class NetPanel extends JPanel {
    private static final Color PANEL_BG = new Color(139, 69, 19);
    private static final Color BUTTON_BG = new Color(205, 133, 63);
    private static final Color BUTTON_FG = new Color(255, 248, 220);
    private static final Color BUTTON_HOVER_BG = new Color(222, 184, 135);
    private static final Color LABEL_FG = new Color(255, 248, 220);
    private static final Color TEXTFIELD_BG = new Color(255, 255, 240);
    private static final Color TEXTFIELD_FG = new Color(70, 70, 70);

    private JButton createRoomButton = new JButton("🏠 创建房间");
    private JButton joinRoomButton = new JButton("🚪 加入房间");
    private JButton disconnectButton = new JButton("❌ 断开连接");
    private JTextField ipTF = new JTextField(12);
    private JTextField portTF = new JTextField(6);
    private JTextField nameTF = new JTextField(10);
    private JLabel roleLabel = new JLabel("角色: 未连接");
    private JLabel statusLabel = new JLabel("状态: 未连接");

    private static NetPanel instance = new NetPanel();

    private NetPanel() {
        setOpaque(true);
        setBackground(PANEL_BG);
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 8));
        setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
        setPreferredSize(new Dimension(800, 55));
        setMinimumSize(new Dimension(600, 55));

        setupStyledLabel("用户名:", 12);
        setupStyledTextField(nameTF, "玩家" + (int) (Math.random() * 1000));

        setupStyledLabel("服务器IP:", 12);
        setupStyledTextField(ipTF, "localhost");

        setupStyledLabel("端口:", 12);
        setupStyledTextField(portTF, "8900");

        setupStyledButton(createRoomButton);
        setupStyledButton(joinRoomButton);
        setupStyledButton(disconnectButton);
        disconnectButton.setEnabled(false);

        setupStatusLabel(roleLabel);
        setupStatusLabel(statusLabel);

        createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String userName = nameTF.getText().trim();
                String portStr = portTF.getText().trim();

                if (userName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入用户名！");
                    return;
                }

                int port;
                try {
                    port = Integer.parseInt(portStr);
                    if (port < 1024 || port > 65535) {
                        JOptionPane.showMessageDialog(null, "端口号必须在1024-65535之间！");
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "请输入有效的端口号！");
                    return;
                }

                setConnectedState(true);
                NetHelper.getInstance().createRoom(userName, port);
                statusLabel.setText("状态: 已创建房间(端口:" + port + ")");
            }
        });

        joinRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String userName = nameTF.getText().trim();
                String ip = ipTF.getText().trim();
                String portStr = portTF.getText().trim();

                if (userName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入用户名！");
                    return;
                }
                if (ip.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入服务器IP！");
                    return;
                }

                int port;
                try {
                    port = Integer.parseInt(portStr);
                    if (port < 1024 || port > 65535) {
                        JOptionPane.showMessageDialog(null, "端口号必须在1024-65535之间！");
                        return;
                    }
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "请输入有效的端口号！");
                    return;
                }

                setConnectedState(true);
                NetHelper.getInstance().joinRoom(ip, port, userName);
                statusLabel.setText("状态: 已连接到 " + ip + ":" + port);
            }
        });

        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                NetHelper.getInstance().disconnect();
                Control.getInstance().resetNetMode();

                setConnectedState(false);

                roleLabel.setText("角色: 未连接");
                statusLabel.setText("状态: 未连接");

                Chatpanl.getInstance().readboard.append("=== 已断开连接，已切换到本地模式 ===\n");
            }
        });
    }

    private void setupStyledLabel(String text, int fontSize) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("微软雅黑", Font.BOLD, fontSize));
        label.setForeground(LABEL_FG);
        add(label);
    }

    private void setupStyledTextField(JTextField textField, String defaultText) {
        textField.setFont(new Font("微软雅黑", Font.PLAIN, 13));
        textField.setBackground(TEXTFIELD_BG);
        textField.setForeground(TEXTFIELD_FG);
        textField.setCaretColor(TEXTFIELD_FG);
        textField.setBorder(new LineBorder(new Color(180, 180, 180), 1, true));
        textField.setMargin(new Insets(3, 6, 3, 6));
        textField.setText(defaultText);
        add(textField);
    }

    private void setupStyledButton(JButton button) {
        button.setFont(new Font("微软雅黑", Font.BOLD, 12));
        button.setBackground(BUTTON_BG);
        button.setForeground(BUTTON_FG);
        button.setFocusPainted(false);
        button.setBorder(new LineBorder(new Color(101, 67, 33), 2, true));
        button.setContentAreaFilled(true);
        button.setOpaque(true);
        button.setMargin(new Insets(6, 12, 6, 12));
        button.setPreferredSize(new Dimension(button.getPreferredSize().width + 10, 32));

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                if (button.isEnabled()) {
                    button.setBackground(BUTTON_HOVER_BG);
                }
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG);
            }
        });
    }

    private void setupStatusLabel(JLabel label) {
        label.setFont(new Font("微软雅黑", Font.BOLD, 12));
        label.setForeground(LABEL_FG);
        label.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 0));
        add(label);
    }

    private void setConnectedState(boolean connected) {
        createRoomButton.setEnabled(!connected);
        joinRoomButton.setEnabled(!connected);
        disconnectButton.setEnabled(connected);
        nameTF.setEnabled(!connected);
        ipTF.setEnabled(!connected);
        portTF.setEnabled(!connected);
    }

    public static NetPanel getInstance() {
        return instance;
    }

    public void updateRoleDisplay() {
        int role = NetHelper.getInstance().getMyRole();
        String roleName = "";
        String icon = "";

        switch (role) {
            case ClientInfo.ROLE_PLAYER_BLACK:
                roleName = "黑棋玩家";
                icon = "⚫";
                break;
            case ClientInfo.ROLE_PLAYER_WHITE:
                roleName = "白棋玩家";
                icon = "⚪";
                break;
            case ClientInfo.ROLE_SPECTATOR:
                roleName = "观众";
                icon = "👁️";
                break;
            default:
                roleName = "未知";
                icon = "❓";
        }

        roleLabel.setText(icon + " 角色: " + roleName);
    }
}
