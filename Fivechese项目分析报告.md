# Fivechese五子棋项目分析报告

## 一、项目概述

Fivechese是一个基于Java Swing开发的五子棋游戏，支持本地双人对战和网络双人对战两种模式。项目采用MVC（Model-View-Controller）架构设计，具有良好的代码结构和功能划分。

### 主要功能
- 本地双人对战模式
- 网络双人对战模式（客户端-服务器架构）
- 实时聊天功能
- 悔棋功能
- 复盘功能
- 背景音乐播放
- 自适应棋盘大小

---

## 二、项目结构

### 2.1 核心文件及功能

| 文件名 | 功能描述 | 主要类 |
|--------|----------|--------|
| `Main.java` | 程序入口，初始化UI组件和背景音乐 | `Main` |
| `Model.java` | 游戏数据模型，处理游戏逻辑和状态管理 | `Model` |
| `Control.java` | 控制器，协调Model和View，处理用户输入 | `Control` |
| `ChessPanel.java` | 棋盘视图，负责绘制棋盘和棋子，处理鼠标事件 | `ChessPanel` |
| `Chess.java` | 棋子数据结构 | `Chess` |
| `NetHelper.java` | 网络通信核心，处理Socket连接和数据传输 | `NetHelper` |
| `NetPanel.java` | 网络设置面板，提供监听和连接功能 | `NetPanel` |
| `Chatpanl.java` | 聊天面板，处理聊天消息的显示和发送 | `Chatpanl` |
| `BackGroundMusic.java` | 背景音乐播放 | `BackGroundMusic` |

### 2.2 架构设计

项目采用经典的MVC架构：

```
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│      View       │       │   Controller    │       │      Model      │
│  (ChessPanel,   │◄─────►│   (Control)     │◄─────►│    (Model)      │
│   NetPanel,     │       │                 │       │                 │
│   Chatpanl)     │       │                 │       │                 │
└─────────────────┘       └─────────────────┘       └─────────────────┘
         │                        │                        │
         │                        │                        │
         ▼                        ▼                        ▼
┌─────────────────┐       ┌─────────────────┐       ┌─────────────────┐
│   用户交互      │       │   业务逻辑      │       │   数据存储      │
│   鼠标点击      │       │   游戏规则      │       │   棋盘状态      │
│   按钮操作      │       │   网络通信      │       │   棋子列表      │
└─────────────────┘       └─────────────────┘       └─────────────────┘
```

---

## 三、五子棋游戏核心实现逻辑

### 3.1 数据结构设计

#### 3.1.1 棋子类（Chess.java）

```java
public class Chess {
    int color;      // 棋子颜色：1=白棋，-1=黑棋，0=空
    int row;        // 行坐标
    int col;        // 列坐标
}
```

#### 3.1.2 游戏数据模型（Model.java）

```java
public class Model {
    public static final int white = 1;      // 白棋
    public static final int Black = -1;     // 黑棋
    public static final int Space = 0;      // 空格
    public static final int width = 19;     // 棋盘大小（19×19）
    
    private int[][] data = new int[width][width];           // 棋盘数据
    public static LinkedList<Chess> list = new LinkedList<>(); // 棋子历史记录
    
    private int lastrow;    // 最后落子行
    private int lastcol;    // 最后落子列
}
```

### 3.2 落子逻辑

落子功能由 `Model.putChess()` 方法实现：

```java
public boolean putChess(int row, int col, int color) {
    // 边界检查和空位检查
    if (row > width || row < 0 || col < 0 || col > width || data[row][col] != Space) {
        return false;
    } else {
        data[row][col] = color;              // 更新棋盘数据
        list.add(new Chess(row, col, color)); // 记录历史
        lastrow = row;
        lastcol = col;
        return true;
    }
}
```

### 3.3 胜负判定逻辑

胜负判定由 `Model.judge()` 方法实现，采用**四方向扫描算法**：

```java
public int judge() {
    int m = data[lastrow][lastcol];  // 最后落子的颜色
    int num = 1;
    
    // 1. 水平方向扫描（同一行）
    // 向右扫描
    for (int i = lastcol + 1; i < width; i++) {
        if (data[lastrow][i] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    // 向左扫描
    for (int i = lastcol - 1; i >= 0; i--) {
        if (data[lastrow][i] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    
    // 2. 垂直方向扫描（同一列）
    // 3. 左上到右下对角线扫描
    // 4. 右上到左下对角线扫描
    
    return Space; // 没有获胜
}
```

**算法特点：**
- 仅扫描最后落子位置的四个方向
- 每个方向最多扫描4个棋子（因为已有1个棋子，加上4个就成5个）
- 时间复杂度：O(1)，因为棋盘大小固定为19×19
- 空间复杂度：O(1)

### 3.4 悔棋功能

悔棋功能由 `Model.back()` 方法实现：

```java
public void back() {
    // 移除最后一步（对手的棋子）
    int row1 = list.getLast().row;
    int col1 = list.getLast().col;
    data[row1][col1] = Space;
    list.removeLast();
    
    // 移除倒数第二步（自己的棋子）
    int row = list.getLast().row;
    int col = list.getLast().col;
    data[row][col] = Space;
    list.removeLast();
}
```

**注意：** 每次悔棋会回退两步（双方各一步）

### 3.5 棋盘绘制

棋盘绘制由 `ChessPanel.paintComponent()` 方法实现：

```java
protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    g.drawImage(imageIcon, 0, 0, this.getWidth(), this.getHeight(), this); // 绘制背景
    drawPanel(g);    // 绘制棋盘网格
    drawchess(g);    // 绘制棋子
}
```

**自适应布局：**
- 通过 `ComponentListener` 监听窗口大小变化
- 动态计算棋盘格子大小（`unit`）
- 自动居中显示棋盘

---

## 四、联网模块实现逻辑

### 4.1 网络架构

采用**客户端-服务器（Client-Server）架构**，基于TCP Socket通信：

```
┌─────────────┐                       ┌─────────────┐
│   玩家A     │                       │   玩家B     │
│  (服务器)   │◄──────TCP Socket─────►│  (客户端)   │
│             │                       │             │
└─────────────┘                       └─────────────┘
       │                                     │
       │                                     │
       ▼                                     ▼
┌─────────────────────────────────────────────────┐
│              NetHelper（网络通信层）              │
└─────────────────────────────────────────────────┘
```

### 4.2 网络通信核心（NetHelper.java）

#### 4.2.1 服务器端监听

```java
public void beginListen() {
    new Thread() {
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(PORT); // PORT=8900
                s = ss.accept();  // 阻塞等待连接
                // 初始化输入输出流
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(), true);
                startReadThread();  // 启动读取线程
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }.start();
}
```

#### 4.2.2 客户端连接

```java
public void connect(String ip) {
    try {
        s = new Socket(ip, PORT);  // 连接到指定IP和端口
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        startReadThread();  // 启动读取线程
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

#### 4.2.3 消息读取线程

```java
protected void startReadThread() {
    new Thread() {
        public void run() {
            while (true) {
                try {
                    String line = reader.readLine();  // 阻塞读取消息
                    // 根据消息类型分发处理
                    if (line.startsWith("PutChess")) {
                        parseChess(line);      // 处理落子消息
                    } else if (line.startsWith("chat")) {
                        parseChat(line);       // 处理聊天消息
                    } else if (line.startsWith("reback")) {
                        Control.getInstance().netotherremoveChess();  // 处理悔棋消息
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }.start();
}
```

### 4.3 消息协议设计

采用**文本协议**，消息格式为：`类型:内容`

| 消息类型 | 格式示例 | 说明 |
|----------|----------|------|
| 落子消息 | `PutChess:行,列` | 发送棋子位置 |
| 聊天消息 | `chat消息内容` | 发送聊天信息 |
| 悔棋消息 | `reback` | 请求悔棋 |

#### 4.3.1 落子消息发送

```java
public void sentChess(final int row, final int col) {
    new Thread() {
        public void run() {
            out.println("PutChess:" + row + "," + col);
        }
    }.start();
}
```

#### 4.3.2 聊天消息发送

```java
public void setChat(final String text) {
    new Thread() {
        public void run() {
            out.println("chat" + text);
        }
    }.start();
}
```

### 4.4 网络模式下的游戏流程

1. **初始化阶段**
   - 玩家A选择"网络对战"模式，点击"开始监听"
   - 玩家B输入玩家A的IP地址，点击"连接服务器"
   - 双方选择棋子颜色（黑棋先手）

2. **游戏阶段**
   - 轮到本方时，点击棋盘落子
   - 本地更新棋盘，同时通过网络发送落子位置
   - 对方接收消息，更新棋盘
   - 轮到对方落子，重复上述过程

3. **胜负判定**
   - 每次落子后本地判定胜负
   - 如果获胜，弹出提示框

### 4.5 同步机制

网络模式下的同步由 `Control` 类管理：

```java
public class Control {
    private int localColor = Model.Black;     // 本方颜色
    private int otherColor = Model.white;     // 对方颜色
    private boolean allowPutChess = true;     // 是否允许落子（轮次控制）
    private boolean netMode = false;          // 是否为网络模式
    
    // 网络模式下本方落子
    public void netModePutChess(int row, int col) {
        if (!allowPutChess) return;  // 不是本方轮次，拒绝落子
        boolean success = Model.getInstance().putChess(row, col, localColor);
        if (success) {
            ChessPanel.getInstance().repaint();
            NetHelper.getInstance().sentChess(row, col);  // 发送落子消息
            allowPutChess = false;  // 禁止本方继续落子，等待对方
            int winner = Model.getInstance().judge();
            // 判定胜负...
        }
    }
    
    // 网络模式下对方落子
    public void netOtherPutChess(int row, int col) {
        boolean success = Model.getInstance().putChess(row, col, otherColor);
        if (success) {
            ChessPanel.getInstance().repaint();
            allowPutChess = true;  // 允许本方落子
            int winner = Model.getInstance().judge();
            // 判定胜负...
        }
    }
}
```

---

## 五、安全风险分析

### 5.1 严重安全风险

#### 5.1.1 输入验证缺失（高风险）

**问题描述：**
- `NetHelper.parseChess()` 方法直接解析网络数据，没有进行输入验证
- 恶意攻击者可以发送特制的消息导致崩溃或异常

```java
protected void parseChess(String line) {
    line = line.substring(9);
    String[] array = line.split(",");
    // 风险：未验证array长度，可能导致ArrayIndexOutOfBoundsException
    int row = Integer.parseInt(array[0]);  // 未捕获NumberFormatException
    int col = Integer.parseInt(array[1]);
    Control.getInstance().netOtherPutChess(row, col);
}
```

**攻击场景：**
- 发送 `PutChess:abc,def` → 触发 `NumberFormatException`
- 发送 `PutChess:100` → 触发 `ArrayIndexOutOfBoundsException`
- 发送 `PutChess:-1,100` → 超出棋盘边界

#### 5.1.2 网络数据未加密（高风险）

**问题描述：**
- 所有网络通信都是明文传输（TCP Socket）
- 聊天消息、落子位置等数据在网络中可被窃听和篡改
- 无身份验证机制，任何人都可以连接

**风险：**
- 聊天隐私泄露
- 游戏数据被篡改（中间人攻击）
- 未授权访问

#### 5.1.3 无限循环读取（高风险）

**问题描述：**
- `startReadThread()` 使用 `while(true)` 无限循环
- 当Socket关闭时，`reader.readLine()` 返回null，导致CPU占用100%
- 没有退出条件

```java
protected void startReadThread() {
    new Thread() {
        public void run() {
            while (true) {  // 无限循环
                try {
                    String line = reader.readLine();
                    // 风险：当line为null时，继续循环导致CPU占用
                    if (line.startsWith("PutChess")) {  // NullPointerException
                        parseChess(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();  // 仅打印异常，不退出
                }
            }
        }
    }.start();
}
```

#### 5.1.4 资源泄漏风险（中高风险）

**问题描述：**
- Socket、流等资源未正确关闭
- 线程没有正确的中断机制
- 多次连接可能导致资源耗尽

```java
public void connect(String ip) {
    try {
        s = new Socket(ip, PORT);
        // 风险：如果之前已有连接，旧的Socket没有关闭
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        // 风险：可能创建多个读取线程
        startReadThread();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### 5.2 中等安全风险

#### 5.2.1 聊天消息注入风险（中风险）

**问题描述：**
- 聊天消息直接追加到 `JTextArea`
- 虽然Swing不会执行HTML，但特殊字符可能导致显示问题
- 没有消息长度限制，可能导致内存溢出

```java
public void netOthershowmsg(String line) {
    // 风险：未过滤特殊字符，未限制长度
    Chatpanl.getInstance().readboard.append("对方说：" + line + "\n");
}
```

#### 5.2.2 悔棋逻辑不完善（中风险）

**问题描述：**
- 悔棋没有确认机制
- 网络模式下可以强制悔棋，对方无法拒绝
- 没有步数限制

```java
public void netModeremoveChess() {
    if (Model.getInstance().getList().getLast().color == otherColor) {
        Model.getInstance().back();
        ChessPanel.getInstance().repaint();
        NetHelper.getInstance().sentbackmsg();  // 直接发送悔棋消息
    } else {
        JOptionPane.showMessageDialog(null, "等待对方落子后才能悔棋");
    }
}
```

#### 5.2.3 线程安全问题（中风险）

**问题描述：**
- 多个线程访问共享数据没有同步
- Swing UI操作应该在EDT线程执行
- 可能导致竞态条件

```java
// 网络线程直接更新UI（违反Swing线程规则）
public void netOtherPutChess(int row, int col) {
    boolean success = Model.getInstance().putChess(row, col, otherColor);
    if (success) {
        ChessPanel.getInstance().repaint();  // 应该在EDT线程执行
        allowPutChess = true;
        // ...
    }
}
```

### 5.3 低风险问题

#### 5.3.1 硬编码端口号

**问题：** 端口号8900硬编码，可能与其他应用冲突

#### 5.3.2 异常处理不完善

**问题：** 大多数异常仅打印堆栈跟踪，没有优雅处理

#### 5.3.3 缺少超时机制

**问题：** Socket连接和读取没有超时设置

---

## 六、代码优化建议

### 6.1 输入验证优化

```java
protected void parseChess(String line) {
    try {
        line = line.substring(9);
        String[] array = line.split(",");
        if (array.length != 2) {
            throw new IllegalArgumentException("Invalid format");
        }
        int row = Integer.parseInt(array[0]);
        int col = Integer.parseInt(array[1]);
        
        // 边界验证
        if (row < 0 || row >= Model.width || col < 0 || col >= Model.width) {
            throw new IllegalArgumentException("Position out of bounds");
        }
        
        Control.getInstance().netOtherPutChess(row, col);
    } catch (Exception e) {
        // 记录日志，不崩溃
        System.err.println("Invalid chess message: " + line);
    }
}
```

### 6.2 读取线程优化

```java
protected void startReadThread() {
    new Thread() {
        public void run() {
            try {
                String line;
                while ((line = reader.readLine()) != null) {  // 正确处理null
                    if (line.startsWith("PutChess")) {
                        parseChess(line);
                    } else if (line.startsWith("chat")) {
                        parseChat(line);
                    } else if (line.startsWith("reback")) {
                        Control.getInstance().netotherremoveChess();
                    }
                }
            } catch (IOException e) {
                // 连接断开，正常退出
                System.out.println("Connection closed: " + e.getMessage());
            } finally {
                // 清理资源
                closeResources();
            }
        }
    }.start();
}
```

### 6.3 Swing线程安全优化

```java
public void netOtherPutChess(final int row, final int col) {
    boolean success = Model.getInstance().putChess(row, col, otherColor);
    if (success) {
        // 在EDT线程更新UI
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                ChessPanel.getInstance().repaint();
            }
        });
        allowPutChess = true;
        // ...
    }
}
```

---

## 七、总结

### 7.1 项目优点

1. **架构清晰**：采用MVC模式，职责分离明确
2. **功能完整**：包含本地对战、网络对战、聊天、悔棋、复盘等功能
3. **用户体验**：自适应棋盘大小，操作直观
4. **代码结构**：使用单例模式，代码组织良好

### 7.2 主要问题

1. **安全风险**：缺少输入验证、数据加密、身份验证
2. **线程安全**：违反Swing线程规则，存在竞态条件
3. **异常处理**：异常处理不完善，资源可能泄漏
4. **网络协议**：协议简单，缺少错误处理和重传机制

### 7.3 改进方向

1. **安全性**：添加输入验证、数据加密、身份验证
2. **稳定性**：完善异常处理，修复资源泄漏
3. **线程安全**：遵循Swing线程规则，添加同步机制
4. **协议完善**：添加消息校验、错误处理、重传机制

### 7.4 适用场景

该项目适合作为**教学演示**和**学习参考**，但不建议直接用于生产环境，需要进行安全加固和稳定性改进。
