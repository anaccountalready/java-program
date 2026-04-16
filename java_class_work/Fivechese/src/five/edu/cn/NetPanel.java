package five.edu.cn;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;

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
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));
        
        add(new JLabel("用户名:"));
        add(nameTF);
        nameTF.setText("玩家" + (int)(Math.random() * 1000));
        
        add(new JLabel("服务器IP:"));
        add(ipTF);
        ipTF.setText("localhost");
        
        add(new JLabel("端口:"));
        add(portTF);
        portTF.setText("8900");
        
        add(createRoomButton);
        add(joinRoomButton);
        add(disconnectButton);
        
        add(roleLabel);
        add(statusLabel);
        
        disconnectButton.setEnabled(false);
        
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
                
                createRoomButton.setEnabled(false);
                joinRoomButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                nameTF.setEnabled(false);
                ipTF.setEnabled(false);
                portTF.setEnabled(false);
                
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
                
                createRoomButton.setEnabled(false);
                joinRoomButton.setEnabled(false);
                disconnectButton.setEnabled(true);
                nameTF.setEnabled(false);
                ipTF.setEnabled(false);
                portTF.setEnabled(false);
                
                NetHelper.getInstance().joinRoom(ip, port, userName);
                statusLabel.setText("状态: 已连接到 " + ip + ":" + port);
            }
        });
        
        disconnectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                NetHelper.getInstance().disconnect();
                Control.getInstance().resetNetMode();
                
                createRoomButton.setEnabled(true);
                joinRoomButton.setEnabled(true);
                disconnectButton.setEnabled(false);
                nameTF.setEnabled(true);
                ipTF.setEnabled(true);
                portTF.setEnabled(true);
                
                roleLabel.setText("角色: 未连接");
                statusLabel.setText("状态: 未连接");
                
                Chatpanl.getInstance().readboard.append("=== 已断开连接，已切换到本地模式 ===\n");
            }
        });
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
