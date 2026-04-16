package five.edu.cn;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class RoomServer {
    private int port = 8900;
    private ServerSocket serverSocket;
    private List<ClientHandler> clients = new ArrayList<>();
    private ClientHandler blackPlayer = null;
    private ClientHandler whitePlayer = null;
    private List<ClientHandler> spectators = new ArrayList<>();
    private String chessHistory = "";
    private boolean isRunning = false;
    
    private static RoomServer instance;
    
    private RoomServer() {}
    
    public static RoomServer getInstance() {
        if (instance == null) {
            instance = new RoomServer();
        }
        return instance;
    }
    
    public void start(int port) {
        if (isRunning) {
            System.out.println("服务器已在运行中");
            return;
        }
        
        this.port = port;
        this.isRunning = true;
        this.clients.clear();
        this.blackPlayer = null;
        this.whitePlayer = null;
        this.spectators.clear();
        this.chessHistory = "";
        
        new Thread() {
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println("房间服务器启动，端口: " + port);
                    while (isRunning) {
                        Socket socket = serverSocket.accept();
                        ClientHandler handler = new ClientHandler(socket);
                        clients.add(handler);
                        new Thread(handler).start();
                    }
                } catch (Exception e) {
                    if (isRunning) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
    
    public void stop() {
        isRunning = false;
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            for (ClientHandler handler : clients) {
                try {
                    handler.socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            clients.clear();
            blackPlayer = null;
            whitePlayer = null;
            spectators.clear();
            chessHistory = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private synchronized void assignRole(ClientHandler handler, String userName) {
        handler.setUserName(userName);
        
        if (blackPlayer == null) {
            blackPlayer = handler;
            handler.setRole(ClientInfo.ROLE_PLAYER_BLACK);
            handler.sendMsg("ROLE:BLACK");
            System.out.println(userName + " 加入房间，角色：黑棋玩家");
        } else if (whitePlayer == null) {
            whitePlayer = handler;
            handler.setRole(ClientInfo.ROLE_PLAYER_WHITE);
            handler.sendMsg("ROLE:WHITE");
            System.out.println(userName + " 加入房间，角色：白棋玩家");
        } else {
            spectators.add(handler);
            handler.setRole(ClientInfo.ROLE_SPECTATOR);
            handler.sendMsg("ROLE:SPECTATOR");
            System.out.println(userName + " 加入房间，角色：观众");
            if (!chessHistory.isEmpty()) {
                handler.sendMsg("SYNC:" + chessHistory);
            }
        }
        broadcastUserList();
    }
    
    private synchronized void handleLeave(ClientHandler handler) {
        clients.remove(handler);
        
        if (handler == blackPlayer) {
            blackPlayer = null;
            System.out.println(handler.getUserName() + " 离开，黑棋位置空出");
        } else if (handler == whitePlayer) {
            whitePlayer = null;
            System.out.println(handler.getUserName() + " 离开，白棋位置空出");
        } else {
            spectators.remove(handler);
            System.out.println(handler.getUserName() + " 离开（观众）");
        }
        broadcastUserList();
    }
    
    private void broadcastUserList() {
        StringBuilder sb = new StringBuilder("USERLIST:");
        if (blackPlayer != null) {
            sb.append("BLACK=").append(blackPlayer.getUserName()).append(";");
        }
        if (whitePlayer != null) {
            sb.append("WHITE=").append(whitePlayer.getUserName()).append(";");
        }
        for (ClientHandler s : spectators) {
            sb.append("SPECTATOR=").append(s.getUserName()).append(";");
        }
        broadcast(sb.toString());
    }
    
    private void broadcast(String msg) {
        for (ClientHandler handler : clients) {
            handler.sendMsg(msg);
        }
    }
    
    private void broadcastToPlayers(String msg) {
        if (blackPlayer != null) blackPlayer.sendMsg(msg);
        if (whitePlayer != null) whitePlayer.sendMsg(msg);
    }
    
    class ClientHandler implements Runnable {
        private Socket socket;
        private BufferedReader reader;
        private PrintWriter writer;
        private String userName;
        private int role;
        
        public ClientHandler(Socket socket) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public int getRole() {
            return role;
        }
        
        public void setRole(int role) {
            this.role = role;
        }
        
        public void sendMsg(String msg) {
            try {
                writer.println(msg);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
        @Override
        public void run() {
            try {
                String line;
                while (isRunning && (line = reader.readLine()) != null) {
                    processMessage(line);
                }
            } catch (Exception e) {
                System.out.println("连接断开: " + userName);
            } finally {
                handleLeave(this);
                try {
                    socket.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        
        private void processMessage(String line) {
            if (line.startsWith("LOGIN:")) {
                String name = line.substring(6);
                assignRole(this, name);
            } else if (line.startsWith("PutChess:")) {
                if (chessHistory.isEmpty()) {
                    chessHistory = line;
                } else {
                    chessHistory += "|" + line;
                }
                broadcast(line);
            } else if (line.startsWith("chat")) {
                String chatMsg = line.substring(4);
                String fullMsg = "CHAT:" + userName + ":" + chatMsg;
                broadcast(fullMsg);
            } else if (line.startsWith("reback")) {
                broadcast(line);
                if (!chessHistory.isEmpty()) {
                    int lastIndex = chessHistory.lastIndexOf("|");
                    if (lastIndex > 0) {
                        chessHistory = chessHistory.substring(0, lastIndex);
                    } else {
                        chessHistory = "";
                    }
                }
            } else if (line.startsWith("CLEAR")) {
                chessHistory = "";
                broadcast(line);
            }
        }
    }
}
