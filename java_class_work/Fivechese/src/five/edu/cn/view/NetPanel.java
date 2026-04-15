package five.edu.cn.view;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.logging.Logger;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import five.edu.cn.controller.Control;

public class NetPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(NetPanel.class.getName());

    private static volatile NetPanel instance;
    private static final Object LOCK = new Object();

    private final JButton listenButton;
    private final JButton connectButton;
    private final JTextField ipTextField;

    private NetPanel() {
        this.listenButton = new JButton("开始监听");
        this.connectButton = new JButton("连接服务器");
        this.ipTextField = new JTextField(20);

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
        setLayout(new FlowLayout());

        add(listenButton);
        add(ipTextField);
        add(connectButton);

        ipTextField.setText("localhost");

        listenButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Control.getInstance().startListening();
                listenButton.setEnabled(false);
                LOGGER.info("Started listening for connections");
            }
        });

        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = ipTextField.getText();
                if (ip != null && !ip.trim().isEmpty()) {
                    Control.getInstance().connectToServer(ip.trim());
                    LOGGER.info("Attempting to connect to: " + ip.trim());
                }
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
