package five.edu.cn;

import java.util.LinkedList;

import javax.swing.JOptionPane;

public class Control {
    private static Control instance = new Control();
    
    private Control() {}
    
    public static Control getInstance() {
        return instance;
    }
    
    private int localColor = Model.Black;
    private boolean netMode = false;
    private boolean allowPutChess = true;
    private int otherColor = Model.white;
    
    public int getLocalColor() {
        return localColor;
    }
    
    public void setLocalColor(int localColor) {
        this.localColor = localColor;
    }
    
    public int getOtherColor() {
        return otherColor;
    }
    
    public void setOtherColor(int otherColor) {
        this.otherColor = otherColor;
    }
    
    public void setMode() {
        Object[] Mode = {"本地模式", "网络对战"};
        int x = JOptionPane.showOptionDialog(null, "请选择游戏模式", "游戏模式", 
            JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, Mode, Mode[0]);
        if (x == 0) {
            netMode = false;
        } else {
            netMode = true;
        }
    }
    
    public void setColor() {
        if (!netMode) {
            setLocalColor();
        } else {
            netmodesetcolor();
        }
    }
    
    private void setLocalColor() {
        Object[] Mode = {"黑棋", "白棋"};
        int x = JOptionPane.showOptionDialog(null, "请选择您的棋子颜色", "选择棋子颜色", 
            JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, Mode, Mode[0]);
        if (x == 0) {
            localColor = Model.Black;
        } else {
            localColor = Model.white;
        }
    }
    
    private void netmodesetcolor() {
        Object[] Mode = {"黑棋", "白棋"};
        int x = JOptionPane.showOptionDialog(null, "请与对手约定您的棋子颜色后再选择", 
            "选择棋子颜色", JOptionPane.OK_OPTION, JOptionPane.QUESTION_MESSAGE, null, Mode, Mode[0]);
        if (x == 0) {
            localColor = Model.Black;
        } else {
            localColor = Model.white;
        }
        otherColor = -localColor;
    }
    
    public boolean isAllowPutChess() {
        if (NetHelper.getInstance().isConnected() && NetHelper.getInstance().isSpectator()) {
            return false;
        }
        if (NetHelper.getInstance().isConnected()) {
            return allowPutChess;
        }
        return true;
    }
    
    public void setAllowPutChess(boolean allowPutChess) {
        this.allowPutChess = allowPutChess;
    }
    
    public boolean isNetMode() {
        return netMode;
    }
    
    public void setNetMode(boolean netMode) {
        this.netMode = netMode;
    }
    
    public void resetNetMode() {
        this.netMode = false;
        this.allowPutChess = true;
        this.localColor = Model.Black;
        this.otherColor = Model.white;
    }
    
    public void localPutChess(int row, int col) {
        if (NetHelper.getInstance().isConnected() && NetHelper.getInstance().isSpectator()) {
            JOptionPane.showMessageDialog(null, "您是观众，无法下棋！");
            return;
        }
        
        if (!netMode && !NetHelper.getInstance().isConnected()) {
            localModePutChess(row, col);
        } else {
            netModePutChess(row, col);
        }
    }
    
    public void localremoveChess() {
        if (NetHelper.getInstance().isConnected() && NetHelper.getInstance().isSpectator()) {
            JOptionPane.showMessageDialog(null, "您是观众，无法悔棋！");
            return;
        }
        
        if (!netMode && !NetHelper.getInstance().isConnected()) {
            Model.getInstance().back();
        } else {
            netModeremoveChess();
        }
    }
    
    public void netOtherPutChess(int row, int col, int color) {
        boolean success = Model.getInstance().putChess(row, col, color);
        
        if (success) {
            ChessPanel.getInstance().repaint();
            
            if (NetHelper.getInstance().isPlayer()) {
                if (color == otherColor) {
                    allowPutChess = true;
                } else {
                    allowPutChess = false;
                }
            }
            
            int winner = Model.getInstance().judge();
            if (winner == -1) {
                JOptionPane.showMessageDialog(null, "黑棋获胜");
            } else if (winner == 1) {
                JOptionPane.showMessageDialog(null, "白棋获胜");
            }
        }
    }
    
    private void netModePutChess(int row, int col) {
        if (!allowPutChess) return;
        
        boolean success = Model.getInstance().putChess(row, col, localColor);
        if (success) {
            ChessPanel.getInstance().repaint();
            NetHelper.getInstance().sentChess(row, col);
            allowPutChess = false;
            
            int winner = Model.getInstance().judge();
            if (winner == -1) {
                JOptionPane.showMessageDialog(null, "黑棋获胜");
            } else if (winner == 1) {
                JOptionPane.showMessageDialog(null, "白棋获胜");
            }
        }
    }
    
    private void localModePutChess(int row, int col) {
        boolean success = Model.getInstance().putChess(row, col, localColor);
        if (success) {
            ChessPanel.getInstance().repaint();
            localColor = -localColor;
            
            int winner = Model.getInstance().judge();
            if (winner == -1) {
                JOptionPane.showMessageDialog(null, "黑棋获胜");
            } else if (winner == 1) {
                JOptionPane.showMessageDialog(null, "白棋获胜");
            }
        }
    }
    
    @Deprecated
    public void beginlisten() {
        JOptionPane.showMessageDialog(null, "请使用新的'创建房间'功能！");
    }
    
    @Deprecated
    public void connect(String ip) {
        JOptionPane.showMessageDialog(null, "请使用新的'加入房间'功能！");
    }
    
    public void netModeremoveChess() {
        if (Model.getInstance().getList().isEmpty()) {
            JOptionPane.showMessageDialog(null, "没有棋子可悔！");
            return;
        }
        
        if (Model.getInstance().getList().getLast().color == otherColor) {
            Model.getInstance().back();
            ChessPanel.getInstance().repaint();
            NetHelper.getInstance().sentbackmsg();
        } else {
            JOptionPane.showMessageDialog(null, "请等待对手下棋后才能悔棋");
        }
    }
    
    public void netotherremoveChess() {
        if (!Model.getInstance().getList().isEmpty()) {
            Model.getInstance().back();
            allowPutChess = false;
            ChessPanel.getInstance().repaint();
        }
    }
    
    public void netOthershowmsg(String line) {
        Chatpanl.getInstance().readboard.append("对手说：" + line + "\n");
    }
    
    public void updateRoleDisplay() {
        int role = NetHelper.getInstance().getMyRole();
        String roleName = "";
        
        switch (role) {
            case ClientInfo.ROLE_PLAYER_BLACK:
                roleName = "黑棋玩家";
                netMode = true;
                break;
            case ClientInfo.ROLE_PLAYER_WHITE:
                roleName = "白棋玩家";
                netMode = true;
                break;
            case ClientInfo.ROLE_SPECTATOR:
                roleName = "观众";
                netMode = true;
                break;
        }
        
        NetPanel.getInstance().updateRoleDisplay();
        System.out.println("角色已更新为: " + roleName);
    }
}
