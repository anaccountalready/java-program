# Fivechese 项目 - Bug修复文档

## 一、问题概述

### 1.1 已修复的问题清单

| 问题编号 | 问题类型 | 问题描述 | 修复状态 |
|---------|---------|---------|---------|
| Bug-001 | 编码问题 | 混合编码导致编译失败 | 已修复 |
| Bug-002 | 下棋逻辑 | 双方各下一子后无法继续下棋 | 已修复 |
| Bug-003 | 端口固定 | 端口号固定为8900，无法选择房间 | 已修复 |
| Bug-004 | 状态重置 | 观众离开房间后仍然不能本地下棋 | 已修复 |
| Bug-005 | 下棋约束 | 进入房间前本地棋子混入对战棋局 | 已修复 |

---

## 二、Bug-001：编码问题修复

### 2.1 问题现象
项目无法通过编译，报错信息显示为"编码 UTF-8 的不可映射字符"或"编码 GBK 的不可映射字符"。

### 2.2 错误信息示例
```
five\edu\cn\Main.java:15: 错误: 编码 UTF-8 的不可映射字符 (0xCE)
        JFrame f=new JFrame("??????");
                             ^
```

### 2.3 问题根因分析

| 文件类型 | 原编码格式 | 新文件编码 | 问题 |
|---------|-----------|-----------|------|
| 原始项目文件（Main.java, Chatpanl.java, ChessPanel.java） | GBK | - | 包含中文字符串和注释 |
| 新增文件（ClientInfo.java, RoomServer.java等） | - | UTF-8 | 包含中文字符串 |

**问题产生的原因**：
1. **原始项目编码**：原始Fivechese项目是在Eclipse等旧IDE中开发，使用**GBK编码**保存Java源文件
2. **新文件编码**：新增的观战功能相关文件使用**UTF-8编码**保存
3. **编译冲突**：两种编码格式的文件混在一起，导致无论使用哪种编码参数都无法编译通过

### 2.4 具体受影响的文件

| 文件名 | 问题位置 | 原乱码内容 | 正确内容 |
|--------|---------|-----------|---------|
| Main.java | 第15行 | `"??????"` | `"五子棋"` |
| Chatpanl.java | 第27行 | `"????"` | `"发送"` |
| Chatpanl.java | 第62行 | `"???????"` | `"消息显示区"` |
| Chatpanl.java | 第65行 | `"?????"` | `"请输入信息"` |
| ChessPanel.java | 第35行 | `"????"` | `"悔棋"` |
| ChessPanel.java | 第46行 | `"???\u00bf\u00b9???\u00bfs"` | `"重新开始游戏"` |
| ChessPanel.java | 第53行 | `"\u00bf\u00c7\u00b7??????\u00bf\u00b9???\u00bfs"` | `"是否重新开始游戏"` |
| ChessPanel.java | 第54行 | `"\u00bf\u00b9???\u00bfs"` | `"重新开始游戏"` |
| ChessPanel.java | 第62行 | `"??????\u00bfs"` | `"开始游戏"` |
| ChessPanel.java | 第76行 | `"\u00bf\u00b5???\u00bfs"` | `"退出游戏"` |
| ChessPanel.java | 第83行 | `"\u00bf\u00c7\u00b7??\u00bf\u00b5???\u00bfs"` | `"是否退出游戏"` |
| ChessPanel.java | 第84行 | `"\u00bf\u00b5???\u00bfs"` | `"退出游戏"` |
| ChessPanel.java | 第90行 | `"????"` | `"复盘"` |
| ChessPanel.java | 第29行 | 注释乱码 | `//棋盘左上角的坐标` |
| ChessPanel.java | 第130行 | 注释乱码 | `//自动调用paintcomponent方法` |

### 2.5 修复方案

**解决方案**：将所有Java源文件统一转换为**UTF-8编码**，并在编译时指定`-encoding UTF-8`参数。

**修复的文件**：
- `Main.java` - 窗口标题修复
- `Chatpanl.java` - 按钮和标签文本修复
- `ChessPanel.java` - 所有按钮文本、对话框消息、移除乱码注释

---

## 三、Bug-002：下棋逻辑问题修复

### 3.1 问题现象
聊天功能正常，但下棋的时候双方在下完一个棋子后就都不能再下了。

**具体表现**：
1. 黑棋玩家点击棋盘 → 成功下第一步 ✓
2. 白棋玩家点击棋盘 → 成功下第二步 ✓
3. 黑棋玩家再次点击棋盘 → 无响应 ✗
4. 白棋玩家再次点击棋盘 → 无响应 ✗

### 3.2 问题根因分析

#### 3.2.1 消息格式缺陷

**原消息格式**：`PutChess:row,col`

**问题**：消息只包含行和列信息，**不包含颜色信息**。

#### 3.2.2 颜色判断逻辑错误

**原代码（Control.java - netOtherPutChess方法）**：
```java
public void netOtherPutChess(int row, int col) {
    // 问题：根据本地list.size()猜测颜色！
    int color = (Model.list.size() % 2 == 0) ? Model.Black : Model.white;
    boolean success = Model.getInstance().putChess(row, col, color);
    
    if (success) {
        if (NetHelper.getInstance().isPlayer()) {
            if (color == otherColor) {
                allowPutChess = true;
            } else {
                allowPutChess = false;
            }
        }
    }
}
```

### 3.3 修复方案

#### 3.3.1 修改消息格式

**旧格式**：`PutChess:row,col`

**新格式**：`PutChess:row,col,color`

其中：
- `color = -1` 表示黑棋（Black）
- `color = 1` 表示白棋（white）

#### 3.3.2 修改的文件

| 文件名 | 修改内容 |
|--------|---------|
| `NetHelper.java` | 1. sentChess() - 发送时包含颜色信息<br>2. parseChess() - 解析时从消息中获取颜色<br>3. parseSync() - 同步历史时也从消息中获取颜色 |
| `Control.java` | netOtherPutChess() - 添加颜色参数，不再猜测 |

---

## 四、Bug-003：端口号固定问题修复

### 4.1 问题现象

端口号固定为8900，导致：
1. 同一个IP只能创建一个房间
2. 观众加入时无法选择要加入的房间
3. 无法同时运行多个房间

**具体场景**：
- 用户A创建房间（端口8900）
- 用户B想要创建另一个房间 → 无法创建（端口冲突）
- 用户C想要加入房间 → 只能加入8900端口的房间，无法选择

### 4.2 问题根因分析

#### 4.2.1 RoomServer端口固定

**原代码（RoomServer.java）**：
```java
public class RoomServer {
    public static final int PORT = 8900;  // 端口号硬编码！
    
    public void start() {
        // 总是使用固定端口8900
        serverSocket = new ServerSocket(PORT);
    }
}
```

#### 4.2.2 NetHelper连接时使用固定端口

**原代码（NetHelper.java）**：
```java
public static final int PORT = 8900;  // 端口号硬编码！

private void connectToServer(String ip, String userName) {
    s = new Socket(ip, PORT);  // 总是连接到8900端口
}
```

#### 4.2.3 NetPanel没有端口输入框

**原UI布局**：
```
[用户名:] [输入框] [服务器IP:] [输入框] [创建房间] [加入房间]
```

没有端口号输入框，用户无法指定端口。

### 4.3 修复方案

#### 4.3.1 修改RoomServer支持动态端口

**修改前（RoomServer.java）**：
```java
public class RoomServer {
    public static final int PORT = 8900;
    private ServerSocket serverSocket;
    
    public void start() {
        new Thread() {
            public void run() {
                serverSocket = new ServerSocket(PORT);  // 固定端口
                // ...
            }
        }.start();
    }
}
```

**修改后（RoomServer.java）**：
```java
public class RoomServer {
    private int port = 8900;  // 改为实例变量
    private boolean isRunning = false;  // 添加运行状态
    
    public void start(int port) {  // 添加端口参数
        if (isRunning) {
            System.out.println("服务器已在运行中");
            return;
        }
        
        this.port = port;
        this.isRunning = true;
        
        // 清空之前的状态
        this.clients.clear();
        this.blackPlayer = null;
        this.whitePlayer = null;
        this.spectators.clear();
        this.chessHistory = "";
        
        new Thread() {
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);  // 使用动态端口
                    System.out.println("房间服务器启动，端口: " + port);
                    while (isRunning) {  // 使用状态变量控制循环
                        // ...
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
        isRunning = false;  // 设置状态为停止
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
            // 关闭所有客户端连接
            for (ClientHandler handler : clients) {
                handler.socket.close();
            }
            // 清空状态
            clients.clear();
            blackPlayer = null;
            whitePlayer = null;
            spectators.clear();
            chessHistory = "";
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

#### 4.3.2 修改NetHelper支持动态端口

**修改前（NetHelper.java）**：
```java
public class NetHelper {
    public static final int PORT = 8900;  // 静态常量
    
    public void createRoom(String userName) {
        this.userName = userName;
        this.isRoomOwner = true;
        RoomServer.getInstance().start();  // 无参数
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        connectToServer("localhost", userName);
    }
    
    public void joinRoom(String ip, String userName) {
        this.userName = userName;
        this.isRoomOwner = false;
        connectToServer(ip, userName);
    }
    
    private void connectToServer(String ip, String userName) {
        try {
            s = new Socket(ip, PORT);  // 使用固定端口
            // ...
        } catch (Exception e) {
            // ...
        }
    }
}
```

**修改后（NetHelper.java）**：
```java
public class NetHelper {
    private int currentPort = 8900;  // 改为实例变量
    
    public void createRoom(String userName, int port) {  // 添加端口参数
        this.userName = userName;
        this.currentPort = port;
        this.isRoomOwner = true;
        RoomServer.getInstance().start(port);  // 传递端口
        
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
        connectToServer("localhost", port, userName);
    }
    
    public void joinRoom(String ip, int port, String userName) {  // 添加端口参数
        this.userName = userName;
        this.currentPort = port;
        this.isRoomOwner = false;
        connectToServer(ip, port, userName);
    }
    
    private void connectToServer(String ip, int port, String userName) {  // 添加端口参数
        try {
            s = new Socket(ip, port);  // 使用动态端口
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
}
```

#### 4.3.3 修改NetPanel添加端口输入框

**修改前（NetPanel.java）**：
```java
public class NetPanel extends JPanel {
    private JTextField ipTF = new JTextField(15);
    private JTextField nameTF = new JTextField(10);
    // 没有端口输入框
    
    private NetPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 10, 5));
        
        add(new JLabel("用户名:"));
        add(nameTF);
        nameTF.setText("玩家" + (int)(Math.random() * 1000));
        
        add(new JLabel("服务器IP:"));
        add(ipTF);
        ipTF.setText("localhost");
        
        add(createRoomButton);
        add(joinRoomButton);
        add(disconnectButton);
        // ...
    }
}
```

**修改后（NetPanel.java）**：
```java
public class NetPanel extends JPanel {
    private JTextField ipTF = new JTextField(12);
    private JTextField portTF = new JTextField(6);  // 新增端口输入框
    private JTextField nameTF = new JTextField(10);
    
    private NetPanel() {
        setLayout(new FlowLayout(FlowLayout.LEFT, 8, 5));
        
        add(new JLabel("用户名:"));
        add(nameTF);
        nameTF.setText("玩家" + (int)(Math.random() * 1000));
        
        add(new JLabel("服务器IP:"));
        add(ipTF);
        ipTF.setText("localhost");
        
        add(new JLabel("端口:"));  // 新增端口标签
        add(portTF);
        portTF.setText("8900");  // 默认端口8900
        
        add(createRoomButton);
        add(joinRoomButton);
        add(disconnectButton);
        
        add(roleLabel);
        add(statusLabel);
        
        disconnectButton.setEnabled(false);
        
        // 创建房间按钮事件
        createRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String userName = nameTF.getText().trim();
                String portStr = portTF.getText().trim();  // 获取端口输入
                
                if (userName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入用户名！");
                    return;
                }
                
                // 验证端口号
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
                portTF.setEnabled(false);  // 禁用端口输入框
                
                NetHelper.getInstance().createRoom(userName, port);  // 传递端口
                statusLabel.setText("状态: 已创建房间(端口:" + port + ")");
            }
        });
        
        // 加入房间按钮事件
        joinRoomButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                String userName = nameTF.getText().trim();
                String ip = ipTF.getText().trim();
                String portStr = portTF.getText().trim();  // 获取端口输入
                
                if (userName.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入用户名！");
                    return;
                }
                if (ip.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "请输入服务器IP！");
                    return;
                }
                
                // 验证端口号
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
                portTF.setEnabled(false);  // 禁用端口输入框
                
                NetHelper.getInstance().joinRoom(ip, port, userName);  // 传递端口
                statusLabel.setText("状态: 已连接到 " + ip + ":" + port);
            }
        });
        
        // 断开连接按钮事件
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
                portTF.setEnabled(true);  // 启用端口输入框
                
                roleLabel.setText("角色: 未连接");
                statusLabel.setText("状态: 未连接");
                
                Chatpanl.getInstance().readboard.append("=== 已断开连接，已切换到本地模式 ===\n");
            }
        });
    }
}
```

### 4.4 修复后的功能

**多房间支持**：
- 用户A可以在端口8900创建房间
- 用户B可以在端口8901创建另一个房间
- 用户C可以选择加入8900或8901端口的房间

**UI布局**：
```
[用户名:] [输入框] [服务器IP:] [输入框] [端口:] [输入框] [创建房间] [加入房间] [断开连接]
```

**端口号验证**：
- 端口号必须在1024-65535之间
- 必须是有效的数字

---

## 五、Bug-004：观众离开后不能下棋问题修复

### 5.1 问题现象

观众离开房间后仍然不能本地下棋：

**具体流程**：
1. 用户以观众身份加入房间 ✓
2. 观众点击棋盘 → 显示"您是观众，无法下棋！" ✓
3. 观众点击"断开连接"按钮 ✓
4. 观众再次点击棋盘 → 仍然显示"您是观众，无法下棋！" ✗

**问题**：断开连接后，用户应该可以恢复本地下棋功能。

### 5.2 问题根因分析

#### 5.2.1 isAllowPutChess()逻辑缺陷

**原代码（Control.java）**：
```java
public boolean isAllowPutChess() {
    if (NetHelper.getInstance().isSpectator()) {
        return false;  // 观众不能下棋
    }
    return allowPutChess;
}
```

**问题**：
- `isSpectator()` 检查的是 `myRole` 变量
- 断开连接后 `myRole` 没有被重置
- 即使断开连接，仍然返回 `false`

#### 5.2.2 状态变量没有重置

**原代码（NetHelper.java）**：
```java
public class NetHelper {
    private int myRole = ClientInfo.ROLE_SPECTATOR;
    private boolean isConnected = false;
    
    public void disconnect() {
        isConnected = false;  // 只重置了isConnected
        // myRole 没有重置！
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
    
    public boolean isSpectator() {
        return myRole == ClientInfo.ROLE_SPECTATOR;  // 只检查myRole
    }
}
```

**问题**：
- `disconnect()` 只设置了 `isConnected = false`
- `myRole` 保持为 `ROLE_SPECTATOR`（3）
- `isSpectator()` 只检查 `myRole`，不检查 `isConnected`

#### 5.2.3 本地下棋逻辑缺陷

**原代码（Control.java）**：
```java
public void localPutChess(int row, int col) {
    if (NetHelper.getInstance().isSpectator()) {  // 只检查是否是观众
        JOptionPane.showMessageDialog(null, "您是观众，无法下棋！");
        return;
    }
    
    if (!netMode) {
        localModePutChess(row, col);  // 本地模式
    } else {
        netModePutChess(row, col);  // 网络模式
    }
}
```

**问题**：
- 即使断开连接，`isSpectator()` 仍然返回 `true`
- 直接返回，无法进入本地下棋逻辑

### 5.3 修复方案

#### 5.3.1 修改NetHelper断开连接时重置角色

**修改前（NetHelper.java）**：
```java
public void disconnect() {
    isConnected = false;
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
```

**修改后（NetHelper.java）**：
```java
public void disconnect() {
    isConnected = false;
    myRole = ClientInfo.ROLE_SPECTATOR;  // 重置角色为默认值
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
```

#### 5.3.2 修改Control的isAllowPutChess逻辑

**修改前（Control.java）**：
```java
public boolean isAllowPutChess() {
    if (NetHelper.getInstance().isSpectator()) {
        return false;
    }
    return allowPutChess;
}
```

**修改后（Control.java）**：
```java
public boolean isAllowPutChess() {
    // 只有在连接状态下，观众才不能下棋
    if (NetHelper.getInstance().isConnected() && NetHelper.getInstance().isSpectator()) {
        return false;
    }
    // 连接状态下返回allowPutChess，未连接状态下始终返回true（本地模式）
    if (NetHelper.getInstance().isConnected()) {
        return allowPutChess;
    }
    return true;  // 未连接时，总是可以本地下棋
}
```

#### 5.3.3 修改localPutChess逻辑

**修改前（Control.java）**：
```java
public void localPutChess(int row, int col) {
    if (NetHelper.getInstance().isSpectator()) {
        JOptionPane.showMessageDialog(null, "您是观众，无法下棋！");
        return;
    }
    
    if (!netMode) {
        localModePutChess(row, col);
    } else {
        netModePutChess(row, col);
    }
}
```

**修改后（Control.java）**：
```java
public void localPutChess(int row, int col) {
    // 只有在连接状态下，观众才不能下棋
    if (NetHelper.getInstance().isConnected() && NetHelper.getInstance().isSpectator()) {
        JOptionPane.showMessageDialog(null, "您是观众，无法下棋！");
        return;
    }
    
    // 未连接时，使用本地模式；连接时使用网络模式
    if (!netMode && !NetHelper.getInstance().isConnected()) {
        localModePutChess(row, col);
    } else {
        netModePutChess(row, col);
    }
}
```

#### 5.3.4 修改localremoveChess逻辑

**修改前（Control.java）**：
```java
public void localremoveChess() {
    if (NetHelper.getInstance().isSpectator()) {
        JOptionPane.showMessageDialog(null, "您是观众，无法悔棋！");
        return;
    }
    
    if (!netMode) {
        Model.getInstance().back();
    } else {
        netModeremoveChess();
    }
}
```

**修改后（Control.java）**：
```java
public void localremoveChess() {
    // 只有在连接状态下，观众才不能悔棋
    if (NetHelper.getInstance().isConnected() && NetHelper.getInstance().isSpectator()) {
        JOptionPane.showMessageDialog(null, "您是观众，无法悔棋！");
        return;
    }
    
    // 未连接时，使用本地模式；连接时使用网络模式
    if (!netMode && !NetHelper.getInstance().isConnected()) {
        Model.getInstance().back();
    } else {
        netModeremoveChess();
    }
}
```

#### 5.3.5 添加resetNetMode方法

**新增方法（Control.java）**：
```java
public void resetNetMode() {
    this.netMode = false;           // 重置为非网络模式
    this.allowPutChess = true;      // 允许下棋
    this.localColor = Model.Black;  // 重置颜色为黑棋
    this.otherColor = Model.white;  // 重置对方颜色
}
```

**调用位置（NetPanel.java）**：
```java
disconnectButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent arg0) {
        NetHelper.getInstance().disconnect();
        Control.getInstance().resetNetMode();  // 调用重置方法
        // ...
    }
});
```

### 5.4 修复后的逻辑流程

**观众离开房间后：**

1. 用户点击"断开连接"按钮
2. `NetHelper.disconnect()` 被调用：
   - `isConnected = false`
   - `myRole = ROLE_SPECTATOR`（重置为默认值）
   - 关闭Socket连接
   - 如果是房间主人，停止服务器

3. `Control.resetNetMode()` 被调用：
   - `netMode = false`
   - `allowPutChess = true`
   - `localColor = Black`
   - `otherColor = white`

4. 用户点击棋盘：
   - `isAllowPutChess()` 检查：
     - `isConnected()` 返回 `false`
     - 不检查 `isSpectator()`
     - 返回 `true`
   - `localPutChess()` 检查：
     - `isConnected()` 返回 `false`
     - 不显示"您是观众，无法下棋！"提示
     - 进入本地模式下棋逻辑
   - 成功下棋！

### 5.5 状态对比

| 状态 | isConnected | myRole | netMode | allowPutChess | 能否下棋 |
|------|-------------|--------|---------|---------------|---------|
| 连接前（默认） | false | SPECTATOR | false | true | ✓ |
| 作为观众连接 | true | SPECTATOR | true | false | ✗ |
| 作为玩家连接 | true | PLAYER | true | 交替 | ✓ |
| 断开连接后 | false | SPECTATOR | false | true | ✓ |

---

## 六、Bug-005：进入房间前本地棋子混入问题修复

### 6.1 问题现象

用户在进入房间之前，在本地模式下已经下了一些棋。当用户创建房间或加入房间后，这些本地棋子会和对战双方的棋混合在一起，导致棋局混乱。

**具体场景**：

1. **场景A：观众提前下棋后加入房间**
   - 用户打开游戏，在本地模式下下了几步棋
   - 用户点击"加入房间"，以观众身份加入
   - 服务器同步历史棋局（空或已有棋子）
   - 问题：本地棋子和服务器同步的棋子混在一起 ✓

2. **场景B：玩家创建房间前下棋**
   - 用户打开游戏，在本地模式下下了几步棋
   - 用户点击"创建房间"，成为黑棋玩家
   - 问题：本地棋子留在棋盘上，和对战棋子混在一起 ✓

3. **场景C：玩家加入房间前下棋**
   - 用户打开游戏，在本地模式下下了几步棋
   - 用户点击"加入房间"，成为白棋玩家
   - 问题：本地棋子留在棋盘上，和对战棋子混在一起 ✓

### 6.2 问题根因分析

#### 6.2.1 进入房间时未清空棋盘

**原代码（NetHelper.java）**：

```java
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
```

**问题分析**：

1. **`createRoom()`方法**：
   - 直接启动服务器并连接
   - **没有清空本地棋盘**
   - 如果用户之前在本地模式下下过棋，这些棋子会保留

2. **`joinRoom()`方法**：
   - 直接连接到服务器
   - **没有清空本地棋盘**
   - 虽然服务器会发送SYNC消息同步历史棋局，但：
     - SYNC消息中会调用`Model.getInstance().clearchess()`
     - 但如果是新房间（没有历史棋局），SYNC消息可能为空
     - 或者在收到SYNC消息之前，本地棋子已经显示

#### 6.2.2 棋局同步的时序问题

**问题场景**：

```
时间线：
T1: 用户在本地模式下下棋 → 棋盘上有棋子
T2: 用户点击"加入房间"
T3: 连接到服务器
T4: 发送LOGIN消息
T5: 服务器分配角色，发送ROLE消息
T6: 客户端收到ROLE消息，显示角色
T7: 服务器检查是否有历史棋局
T8: 如果有历史棋局，发送SYNC消息
T9: 客户端收到SYNC消息，清空棋盘并同步

问题：
- 在T7-T9之间，本地棋子仍然显示
- 如果是新房间（没有历史棋局），T8不会发送SYNC消息
- 本地棋子永远保留在棋盘上
```

### 6.3 修复方案

#### 6.3.1 修改`createRoom()`方法

**修改前**：
```java
public void createRoom(String userName, int port) {
    this.userName = userName;
    this.currentPort = port;
    this.isRoomOwner = true;
    RoomServer.getInstance().start(port);
    // ...
}
```

**修改后**：
```java
public void createRoom(String userName, int port) {
    Model.getInstance().clearchess();  // 清空本地棋盘
    ChessPanel.getInstance().repaint(); // 重绘棋盘
    
    this.userName = userName;
    this.currentPort = port;
    this.isRoomOwner = true;
    RoomServer.getInstance().start(port);
    // ...
}
```

#### 6.3.2 修改`joinRoom()`方法

**修改前**：
```java
public void joinRoom(String ip, int port, String userName) {
    this.userName = userName;
    this.currentPort = port;
    this.isRoomOwner = false;
    connectToServer(ip, port, userName);
}
```

**修改后**：
```java
public void joinRoom(String ip, int port, String userName) {
    Model.getInstance().clearchess();  // 清空本地棋盘
    ChessPanel.getInstance().repaint(); // 重绘棋盘
    
    this.userName = userName;
    this.currentPort = port;
    this.isRoomOwner = false;
    connectToServer(ip, port, userName);
}
```

### 6.4 修复后的逻辑流程

**用户创建房间时**：

1. 用户点击"创建房间"按钮
2. `NetPanel.createRoomButton`的ActionListener被触发
3. 验证用户名和端口号
4. 调用`NetHelper.getInstance().createRoom(userName, port)`
5. **关键步骤**：`Model.getInstance().clearchess()`清空本地棋盘
6. **关键步骤**：`ChessPanel.getInstance().repaint()`重绘棋盘
7. 启动`RoomServer`
8. 连接到本地服务器

**用户加入房间时**：

1. 用户点击"加入房间"按钮
2. `NetPanel.joinRoomButton`的ActionListener被触发
3. 验证用户名、IP和端口号
4. 调用`NetHelper.getInstance().joinRoom(ip, port, userName)`
5. **关键步骤**：`Model.getInstance().clearchess()`清空本地棋盘
6. **关键步骤**：`ChessPanel.getInstance().repaint()`重绘棋盘
7. 连接到服务器

### 6.5 状态对比

| 场景 | 修复前 | 修复后 |
|------|--------|--------|
| 本地下棋后创建房间 | 本地棋子保留 ✓ | 棋盘被清空 ✗ |
| 本地下棋后加入房间 | 本地棋子保留 ✓ | 棋盘被清空 ✗ |
| 新房间无历史棋局 | 本地棋子保留 ✓ | 棋盘被清空 ✗ |
| 有历史棋局的房间 | 先显示本地棋，再同步 | 直接同步服务器棋局 |

### 6.6 为什么在进入房间时清空

**设计考虑**：

1. **对战房间应该是独立的**：
   - 每个房间的棋局应该是独立的
   - 不应该混入本地模式的棋子

2. **用户意图清晰**：
   - 用户点击"创建房间" → 想要开始新的对战
   - 用户点击"加入房间" → 想要加入已有的对战
   - 两种情况都应该以干净的棋盘开始

3. **与SYNC消息的配合**：
   - 清空后，服务器的SYNC消息可以正确同步
   - 即使没有SYNC消息，棋盘也是干净的

4. **用户体验**：
   - 避免棋局混乱
   - 对战双方看到的棋盘一致
   - 观众看到的棋盘与对战双方一致

---

## 七、修复的文件清单汇总

| 文件名 | Bug-001 | Bug-002 | Bug-003 | Bug-004 | Bug-005 |
|--------|---------|---------|---------|---------|---------|
| `Main.java` | ✓ | - | - | - | - |
| `Chatpanl.java` | ✓ | - | - | - | - |
| `ChessPanel.java` | ✓ | - | - | - | - |
| `NetPanel.java` | - | - | ✓（添加端口输入框） | ✓（调用resetNetMode） | - |
| `NetHelper.java` | - | ✓（消息格式） | ✓（动态端口） | ✓（重置myRole） | ✓（进入房间清空棋盘） |
| `RoomServer.java` | - | - | ✓（动态端口） | - | - |
| `Control.java` | - | ✓（颜色参数） | - | ✓（逻辑修复、resetNetMode） | - |

---

## 七、编译验证

### 7.1 编译命令
```bash
cd "c:\Users\Ha ha\tare0328\Ajava-program\java_class_work\Fivechese\src"
javac -encoding UTF-8 -d "../bin" five/edu/cn/*.java
```

### 7.2 编译结果
```
编译成功，无错误输出
退出码: 0
```

---

## 八、测试验证

### 8.1 功能测试要点

#### 8.1.1 多房间测试（Bug-003）

1. **创建多个房间**：
   - 实例A：用户名"玩家1"，端口"8900" → 点击"创建房间" ✓
   - 实例B：用户名"玩家2"，端口"8901" → 点击"创建房间" ✓
   - 验证：两个实例都显示"角色：黑棋玩家"

2. **加入指定房间**：
   - 实例C：用户名"观众1"，IP"localhost"，端口"8900" → 点击"加入房间"
   - 验证：显示"角色：观众"，加入的是8900端口的房间 ✓

3. **跨房间聊天隔离**：
   - 实例A（8900端口）发送消息"你好，我在8900房间"
   - 实例B（8901端口）不应该收到这条消息 ✓

#### 8.1.2 观众离开后下棋测试（Bug-004）

1. **作为观众加入**：
   - 输入用户名，端口，点击"加入房间"
   - 验证：显示"角色：观众" ✓

2. **观众不能下棋**：
   - 点击棋盘 → 显示"您是观众，无法下棋！"提示 ✓

3. **断开连接**：
   - 点击"断开连接"按钮
   - 验证：显示"角色：未连接"，状态：未连接 ✓

4. **恢复本地下棋**：
   - 点击棋盘 → 成功下棋 ✓
   - 交替下黑白棋 ✓
   - 点击"悔棋"按钮 → 成功悔棋 ✓

#### 8.1.3 端口号验证测试

1. **有效端口号**：
   - 输入端口"8900" → 成功创建/加入房间 ✓

2. **无效端口号（<1024）**：
   - 输入端口"80" → 显示"端口号必须在1024-65535之间！" ✓

3. **无效端口号（>65535）**：
   - 输入端口"70000" → 显示"端口号必须在1024-65535之间！" ✓

4. **非数字端口号**：
   - 输入端口"abc" → 显示"请输入有效的端口号！" ✓

#### 8.1.4 进入房间前清空棋盘测试（Bug-005）

1. **创建房间前清空测试**：
   - 打开游戏，在本地模式下下3步棋
   - 验证：棋盘上有3个棋子 ✓
   - 输入用户名，端口，点击"创建房间"
   - 验证：棋盘被清空，显示"角色：黑棋玩家" ✓

2. **加入房间前清空测试**：
   - 实例A创建房间（端口8900）
   - 实例B打开游戏，在本地模式下下3步棋
   - 验证：实例B的棋盘上有3个棋子 ✓
   - 实例B输入用户名，IP，端口，点击"加入房间"
   - 验证：实例B的棋盘被清空 ✓

3. **观众加入房间前清空测试**：
   - 实例A创建房间，下几步棋
   - 实例B打开游戏，在本地模式下下3步棋
   - 实例B点击"加入房间"（成为观众）
   - 验证：实例B的棋盘先清空，再同步服务器棋局 ✓

4. **对战棋局隔离验证**：
   - 实例A创建房间，成为黑棋玩家
   - 实例B加入房间，成为白棋玩家
   - 实例A下第1步棋
   - 验证：实例B的棋盘同步显示黑棋，没有混入本地棋 ✓

---

## 九、附录：新增UI布局

### 9.1 新的网络面板布局

```
+------------------------------------------------------------------+
| 用户名: [玩家123  ]  服务器IP: [localhost   ]  端口: [8900 ]  |
| [创建房间] [加入房间] [断开连接]  角色: 未连接  状态: 未连接  |
+------------------------------------------------------------------+
```

### 9.2 组件说明

| 组件 | 类型 | 默认值 | 说明 |
|------|------|--------|------|
| 用户名 | JTextField | 随机生成如"玩家123" | 用户在房间中的昵称 |
| 服务器IP | JTextField | "localhost" | 要连接的服务器地址 |
| 端口 | JTextField | "8900" | 房间端口号（1024-65535） |
| 创建房间 | JButton | - | 创建新房间并成为黑棋玩家 |
| 加入房间 | JButton | - | 加入指定IP和端口的房间 |
| 断开连接 | JButton | - | 断开连接并恢复本地模式 |
| 角色 | JLabel | "角色: 未连接" | 显示当前角色 |
| 状态 | JLabel | "状态: 未连接" | 显示连接状态 |

---

## 十、根本原因总结

| Bug编号 | 根本原因 | 修复策略 |
|---------|---------|---------|
| Bug-001 | GBK和UTF-8混合编码 | 统一转换为UTF-8 |
| Bug-002 | 消息格式不包含颜色信息 | 消息格式改为`PutChess:row,col,color` |
| Bug-003 | 端口号硬编码为8900 | 添加端口输入框，支持动态端口 |
| Bug-004 | 断开连接后状态未重置 | 添加`resetNetMode()`，修改权限检查逻辑 |
| Bug-005 | 进入房间时未清空本地棋盘 | 在`createRoom()`和`joinRoom()`中添加清空棋盘逻辑 |

---

**文档版本**: v4.0  
**修复日期**: 2026-04-16  
**修复状态**: 已完成并验证通过  
**修复的问题**: 编码问题（Bug-001）、下棋逻辑问题（Bug-002）、端口固定问题（Bug-003）、状态重置问题（Bug-004）、下棋约束问题（Bug-005）
