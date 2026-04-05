# Fivechese项目分析报告

## 1. 项目概述

Fivechese是一个基于Java Swing开发的五子棋游戏，支持本地对战和网络对战模式，包含游戏核心逻辑、界面交互、网络通信和聊天功能等模块。

## 2. 模块结构

### 2.1 核心模块 (Model.java)

核心模块负责游戏的基本逻辑和数据结构，采用单例模式实现。

**主要功能：**
- 棋盘状态管理：使用二维数组存储19x19棋盘的棋子状态
- 落子逻辑：验证落子位置的合法性并更新棋盘状态
- 胜负判断：检查横向、纵向、两个对角线是否有连续五个同色棋子
- 历史记录：使用链表存储棋子落子历史，支持悔棋和复盘功能

**数据结构：**
- `int[][] data`：二维数组，存储棋盘状态，0表示空格，1表示白棋，-1表示黑棋
- `LinkedList<Chess> list`：链表，存储棋子落子历史

**核心方法：**
- `putChess(int row, int col, int color)`：在指定位置放置棋子
- `judge()`：判断游戏是否结束，返回获胜方
- `clearchess()`：清空棋盘
- `back()`：悔棋，移除最后两步落子

### 2.2 界面模块 (ChessPanel.java)

界面模块负责游戏的可视化和用户交互，采用单例模式实现。

**主要功能：**
- 棋盘绘制：绘制19x19的棋盘网格
- 棋子绘制：根据Model中的数据绘制黑白棋子
- 鼠标交互：处理鼠标点击事件，将屏幕坐标转换为棋盘坐标
- 按钮控制：提供开始游戏、悔棋、结束游戏、复盘等按钮

**核心方法：**
- `paintComponent(Graphics g)`：绘制棋盘和棋子
- `drawPanel(Graphics g)`：绘制棋盘网格
- `drawchess(Graphics g)`：绘制棋子
- 鼠标监听器：处理鼠标点击事件，调用Control中的方法落子

### 2.3 控制模块 (Control.java)

控制模块负责游戏的流程控制和模式管理，采用单例模式实现。

**主要功能：**
- 游戏模式选择：本地模式或网络对战模式
- 棋子颜色选择：玩家选择黑白棋子
- 落子控制：根据游戏模式处理落子逻辑
- 网络对战控制：处理网络对战中的落子和悔棋

**核心方法：**
- `setMode()`：选择游戏模式
- `setColor()`：选择棋子颜色
- `localPutChess(int row, int col)`：本地模式下落子
- `netModePutChess(int row, int col)`：网络模式下落子
- `netOtherPutChess(int row, int col)`：处理对方落子

### 2.4 网络模块 (NetHelper.java)

网络模块负责游戏的网络通信，采用单例模式实现。

**主要功能：**
- 服务器监听：创建ServerSocket监听连接请求
- 客户端连接：连接到指定IP的服务器
- 数据传输：发送和接收游戏数据和聊天信息
- 协议解析：解析接收到的网络数据

**核心方法：**
- `beginListen()`：开始监听连接请求
- `connect(String ip)`：连接到指定IP的服务器
- `sentChess(int row, int col)`：发送落子信息
- `sentbackmsg()`：发送悔棋信息
- `setChat(String text)`：发送聊天信息
- `parseChess(String line)`：解析对方落子信息
- `parseChat(String line)`：解析对方聊天信息

### 2.5 网络面板 (NetPanel.java)

网络面板负责网络连接的界面交互，采用单例模式实现。

**主要功能：**
- 提供开始监听按钮：启动服务器监听
- 提供连接按钮：连接到指定IP的服务器
- IP输入框：输入服务器IP地址

### 2.6 聊天模块 (Chatpanl.java)

聊天模块负责游戏中的聊天功能，采用单例模式实现。

**主要功能：**
- 聊天信息显示：显示双方的聊天信息
- 聊天输入：输入聊天内容并发送

### 2.7 音乐模块 (BackGroundMusic.java)

音乐模块负责游戏的背景音乐播放。

## 3. 游戏逻辑

### 3.1 基本规则

- 棋盘为19x19网格
- 黑白棋子交替落子
- 先在一条直线（横向、纵向、对角线）上连成五个同色棋子者获胜

### 3.2 落子逻辑

1. 本地模式：
   - 玩家点击棋盘，系统将屏幕坐标转换为棋盘坐标
   - 验证落子位置是否合法（是否在棋盘范围内且为空）
   - 放置棋子并更新棋盘状态
   - 切换棋子颜色
   - 检查是否获胜

2. 网络模式：
   - 服务器端：启动监听，等待客户端连接
   - 客户端：连接到服务器
   - 玩家点击棋盘，系统验证落子位置并放置棋子
   - 发送落子信息到对方
   - 等待对方落子
   - 接收对方落子信息并更新棋盘
   - 检查是否获胜

### 3.3 胜负判断算法

```java
public int judge() {
    int m = data[lastrow][lastcol];
    int num = 1;
    // 横向检查
    for (int i = lastcol + 1; i < width; i++) {
        if (data[lastrow][i] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    for (int i = lastcol - 1; i >= 0; i--) {
        if (data[lastrow][i] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    // 纵向检查
    num = 1;
    for (int i = lastrow + 1; i < width; i++) {
        if (data[i][lastcol] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    for (int i = lastrow - 1; i > 0; i--) {
        if (data[i][lastcol] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    // 左对角线检查
    num = 1;
    for (int i = lastrow - 1, j = lastcol - 1; i >= 0 && j >= 0; i--, j--) {
        if (data[i][j] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    for (int i = lastrow + 1, j = lastcol + 1; i < width && j < width; i++, j++) {
        if (data[i][j] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    // 右对角线检查
    num = 1;
    for (int i = lastrow - 1, j = lastcol + 1; i >= 0 && j < width; i--, j++) {
        if (data[i][j] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    for (int i = lastrow + 1, j = lastcol - 1; i < width && j >= 0; i++, j--) {
        if (data[i][j] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }
    return Space; // 没有获胜
}
```

**算法分析：**
- 从最后落子的位置开始，向四个方向（横向、纵向、两个对角线）延伸检查
- 每个方向分别向两个相反的方向检查（如横向向左和向右）
- 计算连续同色棋子的数量，达到5个则返回获胜方
- 时间复杂度：O(1)，因为最多检查4个方向，每个方向最多检查4个棋子

### 3.4 悔棋逻辑

- 本地模式：直接移除最后两步落子（因为本地模式是双方交替落子）
- 网络模式：发送悔棋请求给对方，对方确认后移除最后两步落子

### 3.5 复盘功能

- 保存当前棋盘的所有落子历史
- 清空棋盘后，按照落子顺序重新显示每一步，实现复盘效果

## 4. 联网模块实现

### 4.1 网络协议

采用基于TCP的简单文本协议，端口号为8900。

**协议命令：**
- `PutChess:row,col`：落子命令，包含行和列坐标
- `chat message`：聊天命令，包含聊天内容
- `reback`：悔棋命令

### 4.2 服务器端实现

```java
public void beginListen() {
    new Thread() {
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(PORT);
                s = ss.accept();
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(), true);
                startReadThread();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }.start();
}
```

### 4.3 客户端实现

```java
public void connect(String ip) {
    try {
        s = new Socket(ip, PORT);
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        startReadThread();
    } catch (UnknownHostException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

### 4.4 数据传输

- 落子信息：使用`sentChess`方法发送落子坐标
- 聊天信息：使用`setChat`方法发送聊天内容
- 悔棋信息：使用`sentbackmsg`方法发送悔棋请求

### 4.5 数据接收与解析

```java
protected void startReadThread() {
    new Thread() {
        public void run() {
            while (true) {
                try {
                    String line;
                    line = reader.readLine();
                    if (line.startsWith("PutChess")) {
                        parseChess(line);
                    } else if (line.startsWith("chat")) {
                        parseChat(line);
                    } else if (line.startsWith("reback")) {
                        Control.getInstance().netotherremoveChess();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }.start();
}
```

## 5. 安全风险评估（STRIDE模型）

### 5.1 Spoofing（欺骗）

**风险：**
- 无身份验证机制，任何人都可以连接到服务器
- 可能被恶意用户冒充其他玩家

**缓解措施：**
- 实现用户身份验证机制
- 使用加密的连接建立过程

### 5.2 Tampering（篡改）

**风险：**
- 数据传输未加密，可能被中间人攻击篡改
- 落子信息和聊天内容可以被截获和修改

**缓解措施：**
- 使用SSL/TLS加密网络连接
- 实现数据完整性验证

### 5.3 Repudiation（否认）

**风险：**
- 无操作日志和身份验证，无法确认操作来源
- 玩家可能否认自己的操作

**缓解措施：**
- 实现操作日志记录
- 使用数字签名确认操作来源

### 5.4 Information Disclosure（信息泄露）

**风险：**
- 聊天信息和游戏数据明文传输
- 可能被网络嗅探工具截获

**缓解措施：**
- 使用加密传输
- 实现端到端加密

### 5.5 Denial of Service（拒绝服务）

**风险：**
- 无连接限制，可能被恶意用户发起大量连接请求导致服务器崩溃
- 无消息大小限制，可能被发送大消息导致缓冲区溢出

**缓解措施：**
- 实现连接限制和超时机制
- 对消息大小进行限制

### 5.6 Elevation of Privilege（权限提升）

**风险：**
- 无权限控制机制，任何连接的用户都可以执行所有操作
- 可能被恶意用户执行未授权操作

**缓解措施：**
- 实现权限控制机制
- 对关键操作进行权限验证

## 6. 技术总结

### 6.1 采用的技术

- Java Swing：用于构建游戏界面
- Socket编程：实现网络通信
- 单例模式：确保核心类只有一个实例
- 多线程：实现网络通信和背景音乐播放

### 6.2 数据结构

- 二维数组：存储棋盘状态
- 链表：存储棋子落子历史

### 6.3 算法

- 落子验证算法：检查位置是否合法
- 胜负判断算法：检查连续五个同色棋子
- 坐标转换算法：将屏幕坐标转换为棋盘坐标

### 6.4 网络协议

- 基于TCP的简单文本协议
- 命令格式：`命令:参数`

## 7. 改进建议

1. **安全性改进：**
   - 实现身份验证机制
   - 使用加密传输
   - 实现权限控制

2. **功能改进：**
   - 添加AI对手
   - 实现游戏存档和加载
   - 添加更多游戏模式（如不同棋盘大小）

3. **性能改进：**
   - 优化网络通信，减少延迟
   - 优化棋盘绘制，提高渲染速度

4. **代码质量改进：**
   - 增加异常处理
   - 提高代码可读性和可维护性
   - 添加单元测试

## 8. 结论

Fivechese是一个功能完整的五子棋游戏，支持本地对战和网络对战，包含游戏核心逻辑、界面交互、网络通信和聊天功能。项目采用了Java Swing和Socket编程技术，实现了基本的游戏功能。

然而，项目在安全性方面存在一些风险，如无身份验证、数据传输未加密等。同时，在功能和性能方面也有改进空间。通过实施建议的改进措施，可以提高游戏的安全性、稳定性和用户体验。