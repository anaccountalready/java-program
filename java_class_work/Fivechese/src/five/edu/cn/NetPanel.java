package five.edu.cn;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.LineBorder;

public class NetPanel extends JPanel {
    private JButton createRoomButton = new JButton("创建房间");
    private JButton joinRoomButton = new JButton("加入房间");
    private JButton disconnectButton = new JButton("断开连接");
    private JTextField ipTF = new JTextField(12);
    private JTextField portTF = new JTextField(6);
    private JTextField nameTF = new JTextField(10);
    private JLabel roleLabel = new JLabel("角色: 未连接");
    private JLabel statusLabel = new JLabel("状态: 未连接");

    private static NetPanel instance = new NetPanel();

    private NetPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 10));
        setBorder(BorderFactory.createTitledBorder("网络对战"));
        setPreferredSize(new Dimension(1200, 80));
        setMinimumSize(new Dimension(1000, 80));

        add(new JLabel("用户名:"));
        nameTF.setText("玩家" + (int) (Math.random() * 1000));
        add(nameTF);

        add(new JLabel("服务器IP:"));
        ipTF.setText("localhost");
        add(ipTF);

        add(new JLabel("端口:"));
        portTF.setText("8900");
        add(portTF);

        add(createRoomButton);
        add(joinRoomButton);
        add(disconnectButton);
        disconnectButton.setEnabled(false);

        add(roleLabel);
        add(statusLabel);

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

        switch (role) {
            case ClientInfo.ROLE_PLAYER_BLACK:
                roleName = "黑棋玩家";
                break;
            case ClientInfo.ROLE_PLAYER_WHITE:
                roleName = "白棋玩家";
                break;
            case ClientInfo.ROLE_SPECTATOR:
                roleName = "观众";
                break;
            default:
                roleName = "未知";
        }

        roleLabel.setText("角色: " + roleName);
    }
}
