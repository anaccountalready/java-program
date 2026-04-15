package five.edu.cn.controller;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JOptionPane;

import five.edu.cn.model.Chess;
import five.edu.cn.model.Model;
import five.edu.cn.util.NetHelper;
import five.edu.cn.view.ChessPanel;
import five.edu.cn.view.ChatPanel;

public class Control {
    private static final Logger LOGGER = Logger.getLogger(Control.class.getName());

    private static volatile Control instance;
    private static final Object LOCK = new Object();

    private final Model model;
    private final AtomicBoolean netMode;
    private final AtomicBoolean allowPutChess;
    private int localColor;
    private int otherColor;

    private Control() {
        this.model = Model.getInstance();
        this.netMode = new AtomicBoolean(false);
        this.allowPutChess = new AtomicBoolean(true);
        this.localColor = Model.BLACK;
        this.otherColor = Model.WHITE;
    }

    public static Control getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new Control();
                }
            }
        }
        return instance;
    }

    public void selectGameMode() {
        Object[] options = {"本地模式", "网络对战"};
        int choice = JOptionPane.showOptionDialog(
            null, 
            "请选择游戏模式", 
            "游戏模式", 
            JOptionPane.OK_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]
        );
        
        if (choice == 0) {
            netMode.set(false);
            LOGGER.info("Game mode set to local mode");
        } else if (choice == 1) {
            netMode.set(true);
            LOGGER.info("Game mode set to network mode");
        }
    }

    public void selectPlayerColor() {
        if (!netMode.get()) {
            selectLocalColor();
        } else {
            selectNetworkColor();
        }
    }

    private void selectLocalColor() {
        Object[] options = {"黑棋", "白棋"};
        int choice = JOptionPane.showOptionDialog(
            null, 
            "请选择你的棋子颜色", 
            "选择棋子颜色", 
            JOptionPane.OK_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]
        );
        
        if (choice == 0) {
            localColor = Model.BLACK;
        } else if (choice == 1) {
            localColor = Model.WHITE;
        }
        LOGGER.info("Local player color set to: " + (localColor == Model.BLACK ? "BLACK" : "WHITE"));
    }

    private void selectNetworkColor() {
        Object[] options = {"黑棋", "白棋"};
        int choice = JOptionPane.showOptionDialog(
            null, 
            "黑棋先手，请与对手协商选择颜色", 
            "选择棋子颜色", 
            JOptionPane.OK_OPTION, 
            JOptionPane.QUESTION_MESSAGE, 
            null, 
            options, 
            options[0]
        );
        
        if (choice == 0) {
            localColor = Model.BLACK;
            otherColor = Model.WHITE;
        } else if (choice == 1) {
            localColor = Model.WHITE;
            otherColor = Model.BLACK;
        }
        LOGGER.info("Network player color set to: " + (localColor == Model.BLACK ? "BLACK" : "WHITE"));
    }

    public void handleLocalMove(int row, int col) {
        if (!netMode.get()) {
            handleLocalModeMove(row, col);
        } else {
            handleNetworkModeMove(row, col);
        }
    }

    private void handleLocalModeMove(int row, int col) {
        boolean success = model.putChess(row, col, localColor);
        if (success) {
            ChessPanel.getInstance().repaint();
            localColor = -localColor;
            checkWinner();
        }
    }

    private void handleNetworkModeMove(int row, int col) {
        if (!allowPutChess.get()) {
            LOGGER.info("Not allowed to put chess now");
            return;
        }

        boolean success = model.putChess(row, col, localColor);
        if (success) {
            ChessPanel.getInstance().repaint();
            NetHelper.getInstance().sendChessMove(row, col);
            allowPutChess.set(false);
            checkWinner();
        }
    }

    public void handleRemoteMove(int row, int col) {
        boolean success = model.putChess(row, col, otherColor);
        if (success) {
            ChessPanel.getInstance().repaint();
            allowPutChess.set(true);
            checkWinner();
        }
    }

    public void handleUndo() {
        if (!netMode.get()) {
            handleLocalUndo();
        } else {
            handleNetworkUndo();
        }
    }

    private void handleLocalUndo() {
        if (model.getHistorySize() < 2) {
            JOptionPane.showMessageDialog(null, "没有足够的棋子可以悔棋");
            return;
        }
        
        if (model.undo()) {
            ChessPanel.getInstance().repaint();
            LOGGER.info("Local undo successful");
        }
    }

    private void handleNetworkUndo() {
        Chess lastChess = model.getLastChess();
        if (lastChess == null) {
            JOptionPane.showMessageDialog(null, "没有棋子可以悔棋");
            return;
        }

        if (lastChess.getColor() == otherColor) {
            JOptionPane.showMessageDialog(null, "请等待对手落子后才能悔棋");
            return;
        }

        if (model.undo()) {
            ChessPanel.getInstance().repaint();
            NetHelper.getInstance().sendUndoRequest();
            LOGGER.info("Network undo request sent");
        }
    }

    public void handleRemoteUndo() {
        if (model.undo()) {
            allowPutChess.set(false);
            ChessPanel.getInstance().repaint();
            LOGGER.info("Remote undo processed");
        }
    }

    public void handleChatSend(String text) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        
        if (netMode.get() && NetHelper.getInstance().isConnected()) {
            NetHelper.getInstance().sendChatMessage(text);
            ChatPanel.getInstance().appendMessage("我: " + text);
        } else {
            ChatPanel.getInstance().appendMessage("系统: 网络未连接，无法发送消息");
        }
    }

    public void handleRemoteChat(String text) {
        if (text != null && !text.isEmpty()) {
            ChatPanel.getInstance().appendMessage("对手: " + text);
        }
    }

    private void checkWinner() {
        int winner = model.judge();
        if (winner == Model.BLACK) {
            JOptionPane.showMessageDialog(null, "黑棋获胜！");
            LOGGER.info("Black wins!");
        } else if (winner == Model.WHITE) {
            JOptionPane.showMessageDialog(null, "白棋获胜！");
            LOGGER.info("White wins!");
        }
    }

    public void startListening() {
        NetHelper.getInstance().beginListen();
    }

    public void connectToServer(String ip) {
        if (ip == null || ip.trim().isEmpty()) {
            JOptionPane.showMessageDialog(null, "请输入有效的IP地址");
            return;
        }
        allowPutChess.set(false);
        NetHelper.getInstance().connect(ip.trim());
    }

    public void resetGame() {
        model.clearChess();
        ChessPanel.getInstance().repaint();
        allowPutChess.set(true);
        localColor = Model.BLACK;
        otherColor = Model.WHITE;
        LOGGER.info("Game reset");
    }

    public List<Chess> getGameHistory() {
        return model.getChessHistory();
    }

    public int getLocalColor() {
        return localColor;
    }

    public void setLocalColor(int localColor) {
        if (localColor == Model.BLACK || localColor == Model.WHITE) {
            this.localColor = localColor;
            this.otherColor = -localColor;
        }
    }

    public int getOtherColor() {
        return otherColor;
    }

    public boolean isNetMode() {
        return netMode.get();
    }

    public void setNetMode(boolean netMode) {
        this.netMode.set(netMode);
    }

    public boolean isAllowPutChess() {
        return allowPutChess.get();
    }

    public void setAllowPutChess(boolean allowPutChess) {
        this.allowPutChess.set(allowPutChess);
    }
}
