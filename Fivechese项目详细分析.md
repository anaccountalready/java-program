# Fivechese五子棋项目详细分析

## 一、项目结构概览

```
five/edu/cn/
├── Main.java          # 程序入口，创建主窗口
├── Model.java         # 数据模型，管理棋盘状态和棋子
├── Chess.java         # 棋子实体类
├── ChessPanel.java    # 棋盘视图，处理用户输入和绘制
├── Control.java       # 控制层，协调模型和视图
├── NetPanel.java      # 网络面板，提供连接控制UI
├── NetHelper.java     # 网络辅助类，处理TCP通信
├── Chatpanl.java      # 聊天面板
└── BackGroundMusic.java # 背景音乐
```

---

## 二、核心模块详细分析

### 1. 数据模型层 (Model.java)
**文件位置**：`java_class_work/Fivechese/src/five/edu/cn/Model.java`

#### 单例模式实现
```java
// 单例模式实现 - 线程安全的懒汉式
private static Model instance = new Model();
private Model() {} // 私有构造函数
public static Model getInstance() {
    return instance;
}
```

**代码解释**：
- 采用饿汉式单例模式，在类加载时就创建实例
- 私有构造函数防止外部实例化
- 提供全局访问点`getInstance()`
- 优点：线程安全，实现简单
- 缺点：无法延迟加载，在类加载时就占用资源

#### 棋盘数据存储
```java
public static final int white = 1;   // 白棋常量
public static final int Black = -1;  // 黑棋常量
public static final int Space = 0;   // 空位置常量
public static final int width = 19;  // 棋盘大小(19x19)
private int[][] data = new int[width][width]; // 二维数组存储棋盘
private static LinkedList<Chess> list = new LinkedList<Chess>(); // 棋子列表
```

**代码解释**：
- 使用常量定义棋子状态，提高代码可读性
- 19x19的二维数组存储棋盘，每个位置可以是0(空)、1(白)、-1(黑)
- LinkedList存储所有已放置的棋子，支持快速访问和修改

#### 棋子放置逻辑
```java
public boolean putChess(int row, int col, int color) {
    // 输入验证：检查坐标是否在棋盘范围内，且位置为空
    if (row < 0 || row >= width || col < 0 || col >= width || data[row][col] != Space) {
        return false;
    } else {
        data[row][col] = color;
        list.add(new Chess(row, col, color));
        lastrow = row;
        lastcol = col;
        return true;
    }
}
```

**代码解释**：
- 首先进行边界检查，确保坐标在0-18范围内
- 检查目标位置是否为空
- 如果验证通过，更新棋盘数组和棋子列表
- 记录最后放置的棋子位置，用于胜负判断

#### 胜负判断逻辑
```java
public int judge() {
    int m = data[lastrow][lastcol]; // 最后放置的棋子颜色
    int num = 1; // 连续棋子计数

    // 水平方向检查
    for (int i = lastcol + 1; i < width; i++) {
        if (data[lastrow][i] == m) {
            num++;
            if (num == 5) return m; // 五子连珠，返回获胜方
        } else break;
    }
    for (int i = lastcol - 1; i >= 0; i--) {
        if (data[lastrow][i] == m) {
            num++;
            if (num == 5) return m;
        } else break;
    }

    // 垂直方向检查(类似水平方向)
    // 左对角线检查(从左上到右下)
    // 右对角线检查(从右上到左下)

    return Space; // 无人获胜
}
```

**代码解释**：
- 从最后放置的棋子位置开始，向四个方向检查
- 水平方向：向左和向右统计连续相同颜色的棋子
- 垂直方向：向上和向下统计连续相同颜色的棋子
- 对角线方向：两条对角线方向统计
- 如果任何方向达到5个连续棋子，返回获胜方(1或-1)

---

### 2. 视图层 (ChessPanel.java)
**文件位置**：`java_class_work/Fivechese/src/five/edu/cn/ChessPanel.java`

#### 棋盘绘制
```java
private void drawPanel(Graphics g) {
    // 绘制19x19的棋盘网格
    for (int i = 0; i < Model.width; i++) {
        // 水平线
        g.drawLine(lx, ly + i * unit, lx + unit * (Model.width - 1), ly + i * unit);
        // 垂直线
        g.drawLine(lx + i * unit, ly, lx + i * unit, ly + unit * (Model.width - 1));
    }
}
```

**代码解释**：
- 使用Graphics对象绘制棋盘网格
- `lx`和`ly`是棋盘左上角坐标，`unit`是格子大小
- 循环绘制19条水平线和19条垂直线
- 形成19x19的棋盘结构

#### 棋子绘制
```java
private void drawchess(Graphics g) {
    Model m = Model.getInstance();
    for (Chess n : m.getList()) {
        if (n.color == -1) { // 黑棋
            g.setColor(Color.BLACK);
            g.fillOval(lx + n.col * unit - unit / 2, 
                       ly + n.row * unit - unit / 2, 
                       unit, unit);
        } else if (n.color == 1) { // 白棋
            g.setColor(Color.WHITE);
            g.fillOval(lx + n.col * unit - unit / 2, 
                       ly + n.row * unit - unit / 2, 
                       unit, unit);
        }
    }
}
```

**代码解释**：
- 遍历棋子列表，根据颜色绘制黑白棋子
- 使用fillOval方法绘制圆形棋子
- 计算棋子位置：`lx + n.col * unit - unit / 2`
- 确保棋子居中于网格交叉点

#### 鼠标点击处理
```java
addMouseListener(new MouseAdapter() {
    @Override
    public void mousePressed(MouseEvent e) {
        int col; int row;
        // 计算点击位置对应的列
        int colmod = (e.getX() - lx) % unit;
        if (colmod > unit / 2) {
            col = ((e.getX() - lx) / unit) + 1;
        } else {
            col = (e.getX() - lx) / unit;
        }
        
        // 计算点击位置对应的行
        int rowmod = (e.getY() - ly) % unit;
        if (rowmod > unit / 2) {
            row = ((e.getY() - ly) / unit) + 1;
        } else {
            row = (e.getY() - ly) / unit;
        }
        
        Control.getInstance().localPutChess(row, col);
    }
});
```

**代码解释**：
- 监听鼠标点击事件，获取点击坐标
- 将鼠标坐标转换为棋盘的行和列
- 使用模运算判断点击位置靠近哪个交叉点
- 调用Control类处理棋子放置

---

### 3. 控制层 (Control.java)
**文件位置**：`java_class_work/Fivechese/src/five/edu/cn/Control.java`

#### 本地模式游戏逻辑
```java
private void localModePutChess(int row, int col) {
    // 尝试放置棋子
    boolean success = Model.getInstance().putChess(row, col, localColor);
    if (success) {
        ChessPanel.getInstance().repaint(); // 重绘棋盘
        localColor = -localColor; // 切换玩家
        
        // 判断是否获胜
        int winner = Model.getInstance().judge();
        if (winner == -1) {
            JOptionPane.showMessageDialog(null, "黑方获胜");
        } else if (winner == 1) {
            JOptionPane.showMessageDialog(null, "白方获胜");
        }
    }
}
```

**代码解释**：
- 调用Model的putChess方法尝试放置棋子
- 如果成功，重绘棋盘并切换玩家颜色
- 立即进行胜负判断
- 使用JOptionPane显示胜负结果

#### 网络模式游戏逻辑
```java
private void netModePutChess(int row, int col) {
    if (!allowPutChess) return; // 检查是否允许下棋
    
    boolean success = Model.getInstance().putChess(row, col, localColor);
    if (success) {
        ChessPanel.getInstance().repaint();
        NetHelper.getInstance().sentChess(row, col); // 发送棋子位置
        allowPutChess = false; // 禁止本方再次下棋
        
        int winner = Model.getInstance().judge();
        if (winner == -1) {
            JOptionPane.showMessageDialog(null, "黑方获胜");
        } else if (winner == 1) {
            JOptionPane.showMessageDialog(null, "白方获胜");
        }
    }
}
```

**代码解释**：
- 首先检查是否允许本方下棋（在网络模式中轮流）
- 放置棋子后，通过网络发送位置信息
- 设置allowPutChess为false，等待对方下棋
- 胜负判断逻辑与本地模式相同

---

### 4. 联网模块 (NetHelper.java)
**文件位置**：`java_class_work/Fivechese/src/five/edu/cn/NetHelper.java`

#### 服务器端监听
```java
public void beginListen() {
    new Thread() {
        public void run() {
            try {
                ServerSocket ss = new ServerSocket(PORT); // 创建服务器套接字
                s = ss.accept(); // 等待客户端连接
                reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
                out = new PrintWriter(s.getOutputStream(), true);
                startReadThread(); // 启动消息监听线程
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }.start();
}
```

**代码解释**：
- 在新线程中执行网络监听，避免阻塞UI线程
- 创建ServerSocket在PORT(8900)端口监听
- accept()方法阻塞等待客户端连接
- 建立连接后创建输入输出流
- 启动消息监听线程处理网络消息

#### 客户端连接
```java
public void connect(String ip) {
    try {
        s = new Socket(ip, PORT); // 连接到指定IP和端口
        reader = new BufferedReader(new InputStreamReader(s.getInputStream()));
        out = new PrintWriter(s.getOutputStream(), true);
        startReadThread(); // 启动消息监听线程
    } catch (UnknownHostException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
}
```

**代码解释**：
- 创建Socket连接到指定IP地址和端口
- 建立连接后创建输入输出流
- 启动消息监听线程处理网络消息

#### 消息监听与处理
```java
protected void startReadThread() {
    new Thread() {
        public void run() {
            while (true) { // 无限循环监听消息
                try {
                    String line = reader.readLine(); // 读取一行消息
                    if (line.startsWith("PutChess")) {
                        parseChess(line); // 解析棋子位置
                    } else if (line.startsWith("chat")) {
                        parseChat(line); // 解析聊天消息
                    } else if (line.startsWith("reback")) {
                        Control.getInstance().netotherremoveChess(); // 处理悔棋
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }.start();
}
```

**代码解释**：
- 创建新线程循环监听网络消息
- readLine()方法阻塞等待消息到达
- 根据消息前缀调用不同的处理方法
- 支持三种消息类型：棋子放置、聊天、悔棋

#### 棋子消息解析
```java
protected void parseChess(String line) {
    line = line.substring(9); // 去掉"PutChess:"前缀
    String[] array = line.split(","); // 分割行列信息
    int row = Integer.parseInt(array[0]);
    int col = Integer.parseInt(array[1]);
    Control.getInstance().netOtherPutChess(row, col); // 调用控制层处理
}
```

**代码解释**：
- 从消息中提取行列信息
- 使用split方法分割字符串
- 转换为整数后调用Control类处理
- netOtherPutChess方法会放置对方的棋子并切换回合

---

## 三、代码问题与改进建议

### 1. 安全性问题
```java
// 问题：坐标未进行严格验证，可能导致数组越界
public boolean putChess(int row, int col, int color) {
    if (row > width || row < 0 || col < 0 || col > width || data[row][col] != Space)
        return false;
    // ...
}
```

**问题分析**：
- 条件判断中使用`row > width`而不是`row >= width`
- 这意味着行号可以是19，导致数组越界（数组索引0-18）

**改进建议**：
```java
// 改进：严格的边界检查
if (row < 0 || row >= width || col < 0 || col >= width || data[row][col] != Space)
    return false;
```

### 2. 线程安全性问题
```java
// 问题：多线程访问共享数据没有同步
public static LinkedList<Chess> list = new LinkedList<Chess>();
```

**问题分析**：
- list被声明为public static，任何线程都可以修改
- 网络线程和UI线程可能同时访问，导致并发问题

**改进建议**：
```java
// 改进：使用线程安全的集合或同步访问
private static List<Chess> list = Collections.synchronizedList(new LinkedList<Chess>());
```

### 3. 资源泄漏问题
```java
// 问题：Socket流没有正确关闭
public void beginListen() {
    try {
        ServerSocket ss = new ServerSocket(PORT);
        s = ss.accept();
        // ... 流创建代码
    } catch (IOException e) {
        e.printStackTrace();
    }
    // 没有finally块关闭资源
}
```

**问题分析**：
- 网络异常时，Socket流可能没有正确关闭
- 可能导致资源泄漏和程序不稳定

**改进建议**：
- 使用try-with-resources语句
- 在finally块中关闭流和套接字

---

## 四、安全风险总结

| 风险类型 | 具体问题 | 代码位置 |
|---------|---------|---------|
| 数据传输安全 | 明文传输所有消息 | NetHelper.java所有网络通信 |
| 输入验证 | 坐标验证不严格 | Model.java:66 |
| 线程安全 | 共享数据无同步 | Model.java:17 |
| 资源管理 | 网络资源未正确关闭 | NetHelper.java所有网络方法 |
| 异常处理 | 异常栈轨迹直接暴露 | NetHelper.java所有catch块 |

## 五、总结

Fivechese五子棋项目实现了完整的游戏功能，包括：
- 19x19标准棋盘
- 本地双人对战
- 网络对战（TCP Socket）
- 实时聊天
- 悔棋和复盘功能

项目采用了经典的MVC架构，代码结构清晰，但在安全性和健壮性方面还有改进空间。
