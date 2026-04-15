package five.edu.cn.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

import five.edu.cn.controller.Control;
import five.edu.cn.util.NetHelper;

public class NetPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(NetPanel.class.getName());

    private static volatile NetPanel instance;
    private static final Object LOCK = new Object();

    private final JButton listenButton;
    private final JButton connectButton;
    private final JTextField ipTextField;
    private final JLabel statusLabel;
    private final JPanel statusPanel;

    private NetPanel() {
        this.listenButton = new JButton("开始监听");
        this.connectButton = new JButton("连接服务器");
        this.ipTextField = new JTextField(20);
        this.statusLabel = new JLabel("状态: 未连接");
        this.statusPanel = new JPanel();

        initializeComponents();
    }

    public static NetPanel getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new NetPanel();
                }
            }
        }
        return instance;
    }

    private void initializeComponents() {
        setLayout(new BorderLayout(5, 5));
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        ipTextField.setText("localhost");
        ipTextField.setToolTipText("输入服务器IP地址，例如: 192.168.1.100 或 localhost");

        listenButton.setToolTipText("作为服务器，等待其他玩家连接");
        connectButton.setToolTipText("作为客户端，连接到已监听的服务器");

        controlPanel.add(new JLabel("服务器IP:"));
        controlPanel.add(ipTextField);
        controlPanel.add(listenButton);
        controlPanel.add(connectButton);

        statusPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        statusLabel.setForeground(Color.GRAY);
        statusPanel.add(statusLabel);

        add(controlPanel, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        listenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startListening();
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectToServer();
            }
        });
    }

    private void startListening() {
        if (NetHelper.getInstance().isListening()) {
            JOptionPane.showMessageDialog(this, 
                "已经在监听中，端口: " + NetHelper.DEFAULT_PORT, 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        updateStatus("正在启动监听...", Color.ORANGE);
        listenButton.setEnabled(false);

        Control.getInstance().startListening();
        
        new Thread(() -> {
            try {
                Thread.sleep(500);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            
            SwingUtilities.invokeLater(() -> {
                if (NetHelper.getInstance().isListening()) {
                    updateStatus("监听中，端口: " + NetHelper.DEFAULT_PORT + " - 等待客户端连接", Color.BLUE);
                    JOptionPane.showMessageDialog(this, 
                        "服务器已启动！\n" +
                        "端口: " + NetHelper.DEFAULT_PORT + "\n" +
                        "请等待其他玩家连接", 
                        "监听成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    updateStatus("监听启动失败", Color.RED);
                    listenButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this, 
                        "监听启动失败！\n" +
                        "可能原因：\n" +
                        "1. 端口 " + NetHelper.DEFAULT_PORT + " 被占用\n" +
                        "2. 防火墙阻止\n" +
                        "3. 权限不足", 
                        "错误", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        }, "NetPanel-ListenCheck").start();

        LOGGER.info("Started listening for connections");
    }

    private void connectToServer() {
        String ip = ipTextField.getText();
        
        if (ip == null || ip.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, 
                "请输入服务器IP地址！", 
                "输入错误", 
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        String trimmedIp = ip.trim();
        
        if (NetHelper.getInstance().isConnected()) {
            JOptionPane.showMessageDialog(this, 
                "已经连接到服务器: " + trimmedIp, 
                "提示", 
                JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        updateStatus("正在连接到 " + trimmedIp + "...", Color.ORANGE);
        connectButton.setEnabled(false);
        listenButton.setEnabled(false);

        Control.getInstance().connectToServer(trimmedIp);

        new Thread(() -> {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            
            SwingUtilities.invokeLater(() -> {
                if (NetHelper.getInstance().isConnected()) {
                    updateStatus("已连接到: " + trimmedIp, Color.GREEN);
                    JOptionPane.showMessageDialog(this, 
                        "连接成功！\n" +
                        "服务器: " + trimmedIp + "\n" +
                        "可以开始对战了！", 
                        "连接成功", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    updateStatus("连接失败", Color.RED);
                    connectButton.setEnabled(true);
                    listenButton.setEnabled(true);
                    JOptionPane.showMessageDialog(this, 
                        "连接失败！\n\n" +
                        "可能原因：\n" +
                        "1. 服务器未启动监听\n" +
                        "   → 请确保对方先点击了\"开始监听\"按钮\n\n" +
                        "2. IP地址错误\n" +
                        "   → 请检查输入的IP地址是否正确\n" +
                        "   → 本机测试请使用: localhost 或 127.0.0.1\n\n" +
                        "3. 防火墙阻止\n" +
                        "   → 请关闭防火墙或添加端口 " + NetHelper.DEFAULT_PORT + " 例外\n\n" +
                        "4. 网络不通\n" +
                        "   → 请检查网络连接\n" +
                        "   → 尝试ping对方IP地址", 
                        "连接失败", 
                        JOptionPane.ERROR_MESSAGE);
                }
            });
        }, "NetPanel-ConnectCheck").start();

        LOGGER.info("Attempting to connect to: " + trimmedIp);
    }

    private void updateStatus(final String text, final Color color) {
        SwingUtilities.invokeLater(() -> {
            statusLabel.setText("状态: " + text);
            statusLabel.setForeground(color);
        });
    }

    public void updateConnectionStatus(boolean isConnected, boolean isListening) {
        SwingUtilities.invokeLater(() -> {
            if (isConnected) {
                updateStatus("已连接", Color.GREEN);
                connectButton.setEnabled(false);
                listenButton.setEnabled(false);
            } else if (isListening) {
                updateStatus("监听中，端口: " + NetHelper.DEFAULT_PORT, Color.BLUE);
                listenButton.setEnabled(false);
                connectButton.setEnabled(true);
            } else {
                updateStatus("未连接", Color.GRAY);
                listenButton.setEnabled(true);
                connectButton.setEnabled(true);
            }
        });
    }

    public String getIpAddress() {
        return ipTextField.getText();
    }

    public void setIpAddress(String ip) {
        if (ip != null) {
            ipTextField.setText(ip);
        }
    }

    public void setListenButtonEnabled(boolean enabled) {
        listenButton.setEnabled(enabled);
    }

    public void setConnectButtonEnabled(boolean enabled) {
        connectButton.setEnabled(enabled);
    }
}
