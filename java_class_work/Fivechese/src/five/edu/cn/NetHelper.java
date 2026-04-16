package five.edu.cn;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JOptionPane;

public class NetHelper {
    private Socket s;
    private BufferedReader reader;
    private PrintWriter out;
    private String userName;
    private int myRole = ClientInfo.ROLE_SPECTATOR;
    private int currentPort = 8900;
    private boolean isConnected = false;
    private boolean isRoomOwner = false;
    
    private static NetHelper instance = new NetHelper();
    
    private NetHelper() {}
    
    public static NetHelper getInstance() {
        return instance;
    }
    
    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public int getMyRole() {
        return myRole;
    }
    
    public boolean isPlayer() {
        return myRole == ClientInfo.ROLE_PLAYER_BLACK || myRole == ClientInfo.ROLE_PLAYER_WHITE;
    }
    
    public boolean isSpectator() {
        return myRole == ClientInfo.ROLE_SPECTATOR;
    }
    
    public boolean isConnected() {
        return isConnected;
    }
    
    public boolean isRoomOwner() {
        return isRoomOwner;
    }
    
    public void createRoom(String userName, int port) {
        this.userName = userName;
        this.currentPort = port;
        this.isRoomOwner = true;
        RoomServer.getInstance().start(port);
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        connectToServer("localhost", port, userName);
    }
    
    public void joinRoom(String ip, int port, String userName) {
        this.userName = userName;
        this.currentPort = port;
        this.isRoomOwner = false;
        connectToServer(ip, port, userName);
    }
    
    private void connectToServer(String ip, int port, String userName) {
        try {
            s = new Socket(ip, port);
            reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
            isConnected = true;
            startReadThread();
            
            out.println("LOGIN:" + userName);
            
            Chatpanl.getInstance().readboard.append("=== 已连接到房间(" + ip + ":" + port + ") ===\n");
            
        } catch (UnknownHostException e) {
            JOptionPane.showMessageDialog(null, "无法连接到服务器：" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "连接错误：" + e.getMessage());
            e.printStackTrace();
        }
    }
    
    protected void startReadThread() {
        new Thread() {
            public void run() {
                while (isConnected) {
                    try {
                        String line = reader.readLine();
                        if (line == null) break;
                        
                        if (line.startsWith("ROLE:")) {
                            parseRole(line);
                        } else if (line.startsWith("PutChess")) {
                            parseChess(line);
                        } else if (line.startsWith("CHAT:")) {
                            parseChatWithName(line);
                        } else if (line.startsWith("chat")) {
                            parseChat(line);
                        } else if (line.startsWith("reback")) {
                            Control.getInstance().netotherremoveChess();
                        } else if (line.startsWith("USERLIST:")) {
                            parseUserList(line);
                        } else if (line.startsWith("SYNC:")) {
                            parseSync(line);
                        } else if (line.startsWith("CLEAR")) {
                            Model.getInstance().clearchess();
                            ChessPanel.getInstance().repaint();
                            Chatpanl.getInstance().readboard.append("=== 棋盘已清空 ===\n");
                        }
                        
                    } catch (IOException e) {
                        if (isConnected) {
                            e.printStackTrace();
                        }
                        break;
                    }
                }
                isConnected = false;
            }
        }.start();
    }
    
    private void parseRole(String line) {
        String roleStr = line.substring(5);
        if (roleStr.equals("BLACK")) {
            myRole = ClientInfo.ROLE_PLAYER_BLACK;
            Control.getInstance().setLocalColor(Model.Black);
            Control.getInstance().setOtherColor(Model.white);
            Control.getInstance().setAllowPutChess(true);
            Chatpanl.getInstance().readboard.append("=== 您的角色：黑棋玩家 ===\n");
        } else if (roleStr.equals("WHITE")) {
            myRole = ClientInfo.ROLE_PLAYER_WHITE;
            Control.getInstance().setLocalColor(Model.white);
            Control.getInstance().setOtherColor(Model.Black);
            Control.getInstance().setAllowPutChess(false);
            Chatpanl.getInstance().readboard.append("=== 您的角色：白棋玩家 ===\n");
        } else if (roleStr.equals("SPECTATOR")) {
            myRole = ClientInfo.ROLE_SPECTATOR;
            Control.getInstance().setAllowPutChess(false);
            Chatpanl.getInstance().readboard.append("=== 您的角色：观众 ===\n");
            Chatpanl.getInstance().readboard.append("=== 作为观众，您可以观看对战并参与聊天 ===\n");
        }
        Control.getInstance().updateRoleDisplay();
    }
    
    private void parseUserList(String line) {
        String listStr = line.substring(9);
        StringBuilder sb = new StringBuilder();
        sb.append("=== 当前房间用户 ===\n");
        
        if (listStr.contains("BLACK=")) {
            int start = listStr.indexOf("BLACK=") + 6;
            int end = listStr.indexOf(";", start);
            if (end == -1) end = listStr.length();
            sb.append("黑棋玩家: ").append(listStr.substring(start, end)).append("\n");
        }
        if (listStr.contains("WHITE=")) {
            int start = listStr.indexOf("WHITE=") + 6;
            int end = listStr.indexOf(";", start);
            if (end == -1) end = listStr.length();
            sb.append("白棋玩家: ").append(listStr.substring(start, end)).append("\n");
        }
        
        int spectatorCount = 0;
        int idx = 0;
        while ((idx = listStr.indexOf("SPECTATOR=", idx)) != -1) {
            spectatorCount++;
            idx += 10;
        }
        if (spectatorCount > 0) {
            sb.append("观众人数: ").append(spectatorCount).append("人\n");
        }
        
        sb.append("===================\n");
        Chatpanl.getInstance().readboard.append(sb.toString());
    }
    
    private void parseSync(String line) {
        String history = line.substring(5);
        if (history.isEmpty()) return;
        
        String[] moves = history.split("\\|");
        Model.getInstance().clearchess();
        
        for (String move : moves) {
            if (move.startsWith("PutChess:")) {
                String data = move.substring(9);
                String[] array = data.split(",");
                int row = Integer.parseInt(array[0]);
                int col = Integer.parseInt(array[1]);
                int color;
                if (array.length >= 3) {
                    color = Integer.parseInt(array[2]);
                } else {
                    color = (Model.list.size() % 2 == 0) ? Model.Black : Model.white;
                }
                Model.getInstance().putChess(row, col, color);
            }
        }
        ChessPanel.getInstance().repaint();
        Chatpanl.getInstance().readboard.append("=== 已同步历史棋局，共" + moves.length + "步 ===\n");
    }
    
    protected void parseChess(String line) {
        line = line.substring(9);
        String[] array = line.split(",");
        int row = Integer.parseInt(array[0]);
        int col = Integer.parseInt(array[1]);
        int color;
        if (array.length >= 3) {
            color = Integer.parseInt(array[2]);
        } else {
            color = (Model.list.size() % 2 == 0) ? Model.Black : Model.white;
        }
        Control.getInstance().netOtherPutChess(row, col, color);
    }
    
    protected void parseChatWithName(String line) {
        int firstColon = line.indexOf(":", 5);
        if (firstColon > 0) {
            String sender = line.substring(5, firstColon);
            String msg = line.substring(firstColon + 1);
            Chatpanl.getInstance().readboard.append("[" + sender + "]: " + msg + "\n");
        }
    }
    
    protected void parseChat(String line) {
        line = line.substring(4);
        Control.getInstance().netOthershowmsg(line);
    }
    
    public void sentChess(final int row, final int col) {
        if (out != null && isConnected) {
            new Thread() {
                public void run() {
                    out.println("PutChess:" + row + "," + col + "," + Control.getInstance().getLocalColor());
                }
            }.start();
        }
    }
    
    public void sentbackmsg() {
        if (out != null && isConnected) {
            new Thread() {
                public void run() {
                    out.println("reback");
                }
            }.start();
        }
    }
    
    public void setChat(final String text) {
        if (out != null && isConnected) {
            new Thread() {
                public void run() {
                    out.println("chat" + text);
                }
            }.start();
            Chatpanl.getInstance().readboard.append("[我]: " + text + "\n");
        }
    }
    
    public void sendClear() {
        if (out != null && isConnected) {
            new Thread() {
                public void run() {
                    out.println("CLEAR");
                }
            }.start();
        }
    }
    
    public void disconnect() {
        isConnected = false;
        myRole = ClientInfo.ROLE_SPECTATOR;
        try {
            if (s != null) {
                s.close();
            }
            if (isRoomOwner) {
                RoomServer.getInstance().stop();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
