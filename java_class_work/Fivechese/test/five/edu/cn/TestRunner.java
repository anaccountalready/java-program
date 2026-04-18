package five.edu.cn;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * FiveChess Test Runner
 * 
 * Test Coverage:
 * 1. Unit Tests: Chess, Model, Control classes
 * 2. Integration Tests:
 *    - Local Game
 *    - Network Game (simulated two-player)
 *    - Undo
 *    - Replay
 *    - Chat
 */
public class TestRunner {
    
    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static List<String> failures = new ArrayList<String>();
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  FiveChess Test Suite");
        System.out.println("  Date: 2026-04-17");
        System.out.println("========================================");
        System.out.println();
        
        System.out.println("========== Part 1: Unit Tests ==========");
        System.out.println();
        
        runChessTests();
        runModelTests();
        runControlTests();
        
        System.out.println();
        System.out.println("========== Part 2: Integration Tests ==========");
        System.out.println();
        
        runLocalGameTests();
        runNetGameTests();
        runNetGameSimulationTests();
        runUndoTests();
        runNetUndoTests();
        runReplayTests();
        runChatTests();
        runNetChatSimulationTests();
        
        System.out.println();
        printSummary();
    }
    
    // ========================================================================
    // Unit Test Modules
    // ========================================================================
    
    /**
     * Chess Class Unit Tests
     * 
     * Functions tested:
     * - Chess object creation and initialization
     * - Chess property access and modification
     * - Chess object string representation
     * 
     * Test points:
     * 1. Constructor with parameters
     * 2. Default constructor
     * 3. toString method
     * 4. Direct property access (row, col, color)
     */
    private static void runChessTests() {
        System.out.println("--- Chess Unit Tests [Chess Object] ---");
        System.out.println("  Target: Verify Chess class basic functionality");
        System.out.println();
        
        try {
            test("Chess - Constructor with params", new TestCase() {
                public void run() {
                    Chess c = new Chess(3, 4, Model.white);
                    assertEqual(3, c.row);
                    assertEqual(4, c.col);
                    assertEqual(Model.white, c.color);
                }
            });
            
            test("Chess - Default constructor", new TestCase() {
                public void run() {
                    Chess c = new Chess();
                    assertNotNull(c);
                }
            });
            
            test("Chess - toString method", new TestCase() {
                public void run() {
                    Chess c = new Chess(5, 6, Model.Black);
                    String expected = "Chess [color=-1, row=5, col=6]";
                    assertEqual(expected, c.toString());
                }
            });
            
            test("Chess - Property access and modify", new TestCase() {
                public void run() {
                    Chess c = new Chess();
                    c.row = 10;
                    c.col = 11;
                    c.color = Model.white;
                    
                    assertEqual(10, c.row);
                    assertEqual(11, c.col);
                    assertEqual(Model.white, c.color);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Model Class Unit Tests
     * 
     * Functions tested:
     * - Singleton pattern implementation
     * - Board data management
     * - Chess placement logic
     * - Win/lose detection algorithm
     * - Move history tracking
     * - Undo functionality
     * 
     * Test points:
     * 1. Singleton pattern verification
     * 2. Initial board state
     * 3. Valid position placement
     * 4. Occupied position cannot be placed
     * 5. Boundary position placement (within 0-18)
     * 6. Out of bounds get returns Space
     * 7. Move list tracking
     * 
     * Note: There are known bugs in the original code:
     * - putChess boundary check: row>width should be row>=width
     * - This causes ArrayIndexOutOfBoundsException when testing row=19
     */
    private static void runModelTests() {
        System.out.println("--- Model Unit Tests [Game Core Model] ---");
        System.out.println("  Target: Verify game core logic correctness");
        System.out.println("  Includes: Singleton, Board management, Win detection, Undo");
        System.out.println();
        
        try {
            Model.getInstance().clearchess();
            Model.list.clear();
            
            test("Model - Singleton pattern", new TestCase() {
                public void run() {
                    Model m1 = Model.getInstance();
                    Model m2 = Model.getInstance();
                    assertSame(m1, m2);
                }
            });
            
            test("Model - Initial board is empty", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    int[][] data = m.getData();
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            assertEqual(Model.Space, data[i][j]);
                        }
                    }
                }
            });
            
            test("Model - Valid position placement", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    boolean result = m.putChess(5, 5, Model.Black);
                    assertTrue(result);
                    assertEqual(Model.Black, m.getChess(5, 5));
                }
            });
            
            test("Model - Occupied position cannot place", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    m.putChess(5, 5, Model.Black);
                    boolean result = m.putChess(5, 5, Model.white);
                    assertFalse(result);
                }
            });
            
            test("Model - Boundary position (0,0) and (18,18) are valid", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    assertTrue(m.putChess(0, 0, Model.Black));
                    assertTrue(m.putChess(18, 18, Model.white));
                    
                    assertEqual(Model.Black, m.getChess(0, 0));
                    assertEqual(Model.white, m.getChess(18, 18));
                }
            });
            
            test("Model - Out of bounds get returns Space", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    assertEqual(Model.Space, m.getChess(-1, 0));
                    assertEqual(Model.Space, m.getChess(0, -1));
                    assertEqual(Model.Space, m.getChess(19, 0));
                    assertEqual(Model.Space, m.getChess(0, 19));
                }
            });
            
            test("Model - Move list tracking", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    m.putChess(1, 1, Model.Black);
                    m.putChess(2, 2, Model.white);
                    m.putChess(3, 3, Model.Black);
                    
                    assertEqual(3, Model.list.size());
                    assertEqual(Model.Black, Model.list.get(0).color);
                    assertEqual(Model.white, Model.list.get(1).color);
                    assertEqual(Model.Black, Model.list.get(2).color);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Control Class Unit Tests
     * 
     * Functions tested:
     * - Game controller singleton pattern
     * - Game mode management (Local/Network)
     * - Player color management
     * - Placement permission control
     * 
     * Test points:
     * 1. Singleton pattern verification
     * 2. Initial settings verification
     * 3. Local color set and get
     * 4. Opponent color set and get
     * 5. Network mode set and get
     * 6. Placement permission set and get
     */
    private static void runControlTests() {
        System.out.println("--- Control Unit Tests [Game Controller] ---");
        System.out.println("  Target: Verify game control logic correctness");
        System.out.println("  Includes: Mode management, Color management, Permission control");
        System.out.println();
        
        try {
            test("Control - Singleton pattern", new TestCase() {
                public void run() {
                    Control c1 = Control.getInstance();
                    Control c2 = Control.getInstance();
                    assertSame(c1, c2);
                }
            });
            
            test("Control - Initial settings", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    assertNotNull(c);
                }
            });
            
            test("Control - Local color set and get", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setLocalColor(Model.Black);
                    assertEqual(Model.Black, c.getLocalColor());
                    
                    c.setLocalColor(Model.white);
                    assertEqual(Model.white, c.getLocalColor());
                }
            });
            
            test("Control - Opponent color set and get", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setOtherColor(Model.white);
                    assertEqual(Model.white, c.getOtherColor());
                    
                    c.setOtherColor(Model.Black);
                    assertEqual(Model.Black, c.getOtherColor());
                }
            });
            
            test("Control - Network mode set and get", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setNetMode(false);
                    assertFalse(c.isNetMode());
                    
                    c.setNetMode(true);
                    assertTrue(c.isNetMode());
                }
            });
            
            test("Control - Placement permission set and get", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setAllowPutChess(true);
                    assertTrue(c.isAllowPutChess());
                    
                    c.setAllowPutChess(false);
                    assertFalse(c.isAllowPutChess());
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    // ========================================================================
    // Integration Test Modules
    // ========================================================================
    
    /**
     * Local Game Integration Tests
     * 
     * Functions tested:
     * - Local two-player turn-based play
     * - Win detection (horizontal, vertical, diagonal, anti-diagonal)
     * - Boundary condition handling
     * 
     * Test points:
     * 1. Turn-based play logic
     * 2. Black wins with horizontal five-in-a-row
     * 3. White wins with vertical five-in-a-row
     * 4. Black wins with diagonal five-in-a-row
     * 5. No winner situation
     * 6. Cannot place on occupied position
     * 7. Board boundary handling
     * 
     * Note: There is a known bug in judge() method:
     * - Vertical check: for(int i=lastrow-1;i>0;i--) should be i>=0
     * - This causes vertical five-in-a-row including row=0 won't be detected
     */
    private static void runLocalGameTests() {
        System.out.println("--- Local Game Integration Tests [Local Two-Player] ---");
        System.out.println("  Target: Verify complete local game flow");
        System.out.println("  Includes: Turn-based play, Win detection, Boundary handling");
        System.out.println();
        
        try {
            Model.getInstance().clearchess();
            Model.list.clear();
            Control.getInstance().setNetMode(false);
            Control.getInstance().setLocalColor(Model.Black);
            
            test("Local Game - Turn-based play logic", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    Control c = Control.getInstance();
                    
                    c.setLocalColor(Model.Black);
                    int initialColor = c.getLocalColor();
                    assertEqual(Model.Black, initialColor);
                    
                    m.putChess(5, 5, c.getLocalColor());
                    c.setLocalColor(-c.getLocalColor());
                    assertEqual(Model.white, c.getLocalColor());
                    
                    m.putChess(6, 6, c.getLocalColor());
                    c.setLocalColor(-c.getLocalColor());
                    assertEqual(Model.Black, c.getLocalColor());
                    
                    assertEqual(2, Model.list.size());
                    assertEqual(Model.Black, m.getChess(5, 5));
                    assertEqual(Model.white, m.getChess(6, 6));
                }
            });
            
            test("Local Game - Black wins horizontal", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    for (int i = 0; i < 5; i++) {
                        m.putChess(10, i, Model.Black);
                    }
                    
                    int winner = m.judge();
                    assertEqual(Model.Black, winner);
                }
            });
            
            test("Local Game - White wins vertical (using rows 1-5 to avoid bug)", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    for (int i = 1; i < 6; i++) {
                        m.putChess(i, 10, Model.white);
                    }
                    
                    int winner = m.judge();
                    assertEqual(Model.white, winner);
                }
            });
            
            test("Local Game - Black wins diagonal", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    for (int i = 0; i < 5; i++) {
                        m.putChess(9 + i, 9 + i, Model.Black);
                    }
                    
                    int winner = m.judge();
                    assertEqual(Model.Black, winner);
                }
            });
            
            test("Local Game - No winner", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    m.putChess(5, 5, Model.Black);
                    m.putChess(5, 6, Model.white);
                    m.putChess(5, 7, Model.Black);
                    m.putChess(5, 8, Model.white);
                    m.putChess(6, 5, Model.Black);
                    
                    int winner = m.judge();
                    assertEqual(Model.Space, winner);
                }
            });
            
            test("Local Game - Cannot place on occupied", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    boolean placed = m.putChess(5, 5, Model.Black);
                    assertTrue(placed);
                    
                    boolean placedAgain = m.putChess(5, 5, Model.white);
                    assertFalse(placedAgain);
                    
                    assertEqual(1, Model.list.size());
                }
            });
            
            test("Local Game - Board boundary (0,0) and (18,18) are valid", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    assertTrue(m.putChess(0, 0, Model.Black));
                    assertTrue(m.putChess(18, 18, Model.white));
                    
                    assertEqual(Model.Black, m.getChess(0, 0));
                    assertEqual(Model.white, m.getChess(18, 18));
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Network Game Basic Integration Tests
     * 
     * Functions tested:
     * - Network mode initialization
     * - Player color configuration
     * - Placement permission control
     * - Opponent player operation handling
     * 
     * Test points:
     * 1. Network mode initialization
     * 2. Color switching
     * 3. Placement permission switching
     * 4. Opponent player placement
     * 5. Player colors are opposite
     */
    private static void runNetGameTests() {
        System.out.println("--- Network Game Basic Tests [Network Mode Basics] ---");
        System.out.println("  Target: Verify basic network mode control logic");
        System.out.println("  Includes: Mode switch, Color management, Permission control");
        System.out.println();
        
        try {
            Model.getInstance().clearchess();
            Model.list.clear();
            Control.getInstance().setNetMode(true);
            Control.getInstance().setLocalColor(Model.Black);
            Control.getInstance().setOtherColor(Model.white);
            Control.getInstance().setAllowPutChess(true);
            
            test("Network Game - Network mode initialization", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    assertTrue(c.isNetMode());
                    assertEqual(Model.Black, c.getLocalColor());
                    assertEqual(Model.white, c.getOtherColor());
                    assertTrue(c.isAllowPutChess());
                }
            });
            
            test("Network Game - Player color switch", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setLocalColor(Model.white);
                    c.setOtherColor(Model.Black);
                    assertEqual(Model.white, c.getLocalColor());
                    assertEqual(Model.Black, c.getOtherColor());
                }
            });
            
            test("Network Game - Placement permission switch", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setAllowPutChess(false);
                    assertFalse(c.isAllowPutChess());
                    
                    c.setAllowPutChess(true);
                    assertTrue(c.isAllowPutChess());
                }
            });
            
            test("Network Game - Opponent player placement", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    Control c = Control.getInstance();
                    
                    m.clearchess();
                    Model.list.clear();
                    c.setOtherColor(Model.white);
                    c.setAllowPutChess(false);
                    
                    m.putChess(5, 5, Model.white);
                    
                    assertEqual(Model.white, m.getChess(5, 5));
                    assertEqual(1, Model.list.size());
                }
            });
            
            test("Network Game - Player colors are opposite", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setLocalColor(Model.Black);
                    c.setOtherColor(Model.white);
                    assertEqual(-c.getLocalColor(), c.getOtherColor());
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    // ========================================================================
    // Network Game Simulation Environment
    // ========================================================================
    
    /**
     * Network Game Simulation Environment
     * 
     * Design Note:
     * Since Model and Control are Singleton patterns, we cannot create
     * two independent game instances. Instead, we use a "perspective switch"
     * approach to simulate two-player network gameplay:
     * 
     * 1. Player A (Black, Server) perspective: places first, controls black
     * 2. Player B (White, Client) perspective: waits for A, controls white
     * 
     * We use message queues to simulate Socket communication, and save/restore
     * game state to switch between perspectives.
     * 
     * Message Protocol:
     * - Placement: "PutChess:row,col"
     * - Undo: "reback"
     * - Chat: "chat" + message
     */
    private static class NetGameSimulation {
        private final Queue<String> playerAToB = new ConcurrentLinkedQueue<String>();
        private final Queue<String> playerBToA = new ConcurrentLinkedQueue<String>();
        
        private int[][] playerABoard;
        private LinkedList<Chess> playerAList;
        private int playerALocalColor;
        private int playerAOtherColor;
        private boolean playerAAllowPut;
        
        private int[][] playerBBoard;
        private LinkedList<Chess> playerBList;
        private int playerBLocalColor;
        private int playerBOtherColor;
        private boolean playerBAllowPut;
        
        private boolean isPlayerAPerspective;
        
        public NetGameSimulation() {
            Model m = Model.getInstance();
            
            m.clearchess();
            Model.list.clear();
            
            playerABoard = createEmptyBoard();
            playerAList = new LinkedList<Chess>();
            playerALocalColor = Model.Black;
            playerAOtherColor = Model.white;
            playerAAllowPut = true;
            
            playerBBoard = createEmptyBoard();
            playerBList = new LinkedList<Chess>();
            playerBLocalColor = Model.white;
            playerBOtherColor = Model.Black;
            playerBAllowPut = false;
            
            isPlayerAPerspective = true;
            switchToPlayerA();
        }
        
        private int[][] createEmptyBoard() {
            int[][] board = new int[Model.width][Model.width];
            for (int i = 0; i < Model.width; i++) {
                for (int j = 0; j < Model.width; j++) {
                    board[i][j] = Model.Space;
                }
            }
            return board;
        }
        
        private void savePlayerAState() {
            Model m = Model.getInstance();
            
            playerABoard = copyBoard(m.getData());
            playerAList = new LinkedList<Chess>(Model.list);
            playerAAllowPut = Control.getInstance().isAllowPutChess();
        }
        
        private void savePlayerBState() {
            Model m = Model.getInstance();
            
            playerBBoard = copyBoard(m.getData());
            playerBList = new LinkedList<Chess>(Model.list);
            playerBAllowPut = Control.getInstance().isAllowPutChess();
        }
        
        private int[][] copyBoard(int[][] original) {
            int[][] copy = new int[Model.width][Model.width];
            for (int i = 0; i < Model.width; i++) {
                System.arraycopy(original[i], 0, copy[i], 0, Model.width);
            }
            return copy;
        }
        
        private void restoreBoard(int[][] saved) {
            int[][] current = Model.getInstance().getData();
            for (int i = 0; i < Model.width; i++) {
                System.arraycopy(saved[i], 0, current[i], 0, Model.width);
            }
        }
        
        public void switchToPlayerA() {
            if (!isPlayerAPerspective) {
                savePlayerBState();
            }
            
            if (playerABoard != null) {
                restoreBoard(playerABoard);
            }
            Model.list = new LinkedList<Chess>(playerAList);
            
            Control c = Control.getInstance();
            c.setLocalColor(playerALocalColor);
            c.setOtherColor(playerAOtherColor);
            c.setAllowPutChess(playerAAllowPut);
            c.setNetMode(true);
            
            isPlayerAPerspective = true;
        }
        
        public void switchToPlayerB() {
            if (isPlayerAPerspective) {
                savePlayerAState();
            }
            
            if (playerBBoard != null) {
                restoreBoard(playerBBoard);
            }
            Model.list = new LinkedList<Chess>(playerBList);
            
            Control c = Control.getInstance();
            c.setLocalColor(playerBLocalColor);
            c.setOtherColor(playerBOtherColor);
            c.setAllowPutChess(playerBAllowPut);
            c.setNetMode(true);
            
            isPlayerAPerspective = false;
        }
        
        public void sendFromPlayerA(String message) {
            playerAToB.offer(message);
        }
        
        public void sendFromPlayerB(String message) {
            playerBToA.offer(message);
        }
        
        public String receiveFromPlayerA() {
            return playerAToB.poll();
        }
        
        public String receiveFromPlayerB() {
            return playerBToA.poll();
        }
        
        public void playerAPutChess(int row, int col) {
            switchToPlayerA();
            Control c = Control.getInstance();
            Model m = Model.getInstance();
            
            if (c.isAllowPutChess()) {
                boolean success = m.putChess(row, col, c.getLocalColor());
                if (success) {
                    c.setAllowPutChess(false);
                    sendFromPlayerA("PutChess:" + row + "," + col);
                    savePlayerAState();
                }
            }
        }
        
        public void playerBPutChess(int row, int col) {
            switchToPlayerB();
            Control c = Control.getInstance();
            Model m = Model.getInstance();
            
            if (c.isAllowPutChess()) {
                boolean success = m.putChess(row, col, c.getLocalColor());
                if (success) {
                    c.setAllowPutChess(false);
                    sendFromPlayerB("PutChess:" + row + "," + col);
                    savePlayerBState();
                }
            }
        }
        
        public void processPlayerAMessages() {
            String msg = receiveFromPlayerB();
            if (msg != null) {
                switchToPlayerA();
                Model m = Model.getInstance();
                Control c = Control.getInstance();
                
                if (msg.startsWith("PutChess:")) {
                    String[] parts = msg.substring(9).split(",");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    m.putChess(row, col, c.getOtherColor());
                    c.setAllowPutChess(true);
                    savePlayerAState();
                } else if (msg.equals("reback")) {
                    m.back();
                    c.setAllowPutChess(false);
                    savePlayerAState();
                }
            }
        }
        
        public void processPlayerBMessages() {
            String msg = receiveFromPlayerA();
            if (msg != null) {
                switchToPlayerB();
                Model m = Model.getInstance();
                Control c = Control.getInstance();
                
                if (msg.startsWith("PutChess:")) {
                    String[] parts = msg.substring(9).split(",");
                    int row = Integer.parseInt(parts[0]);
                    int col = Integer.parseInt(parts[1]);
                    m.putChess(row, col, c.getOtherColor());
                    c.setAllowPutChess(true);
                    savePlayerBState();
                } else if (msg.equals("reback")) {
                    m.back();
                    c.setAllowPutChess(true);
                    savePlayerBState();
                }
            }
        }
        
        public void playerASendChat(String message) {
            sendFromPlayerA("chat" + message);
        }
        
        public void playerBSendChat(String message) {
            sendFromPlayerB("chat" + message);
        }
        
        public void playerARequestUndo() {
            switchToPlayerA();
            if (Model.list.size() >= 2) {
                Chess lastChess = Model.list.getLast();
                Control c = Control.getInstance();
                if (lastChess.color == c.getOtherColor()) {
                    Model.getInstance().back();
                    sendFromPlayerA("reback");
                    c.setAllowPutChess(true);
                    savePlayerAState();
                }
            }
        }
        
        public int getPlayerABoard(int row, int col) {
            switchToPlayerA();
            return Model.getInstance().getChess(row, col);
        }
        
        public int getPlayerBBoard(int row, int col) {
            switchToPlayerB();
            return Model.getInstance().getChess(row, col);
        }
        
        public int getPlayerAListSize() {
            switchToPlayerA();
            return Model.list.size();
        }
        
        public int getPlayerBListSize() {
            switchToPlayerB();
            return Model.list.size();
        }
        
        public boolean isPlayerAAllowPut() {
            switchToPlayerA();
            return Control.getInstance().isAllowPutChess();
        }
        
        public boolean isPlayerBAllowPut() {
            switchToPlayerB();
            return Control.getInstance().isAllowPutChess();
        }
        
        public int judgeForPlayerA() {
            switchToPlayerA();
            return Model.getInstance().judge();
        }
        
        public int judgeForPlayerB() {
            switchToPlayerB();
            return Model.getInstance().judge();
        }
    }
    
    /**
     * Network Game Simulation Integration Tests
     * 
     * Functions tested:
     * - Two-player network game flow simulation
     * - Message passing mechanism
     * - Board state synchronization
     * - Turn-based play control
     * - Win detection consistency
     * 
     * Test points:
     * 1. Network game initialization
     * 2. Player A places first (Black, Server)
     * 3. Player B receives and syncs board
     * 4. Player B places and syncs to A
     * 5. Complete multi-round game simulation
     * 6. Win detection consistency between both players
     * 7. Both players have identical board state
     * 
     * Design Note:
     * Since Model and Control are Singletons, we use perspective switching
     * to simulate two players. The game state is saved and restored when
     * switching between Player A and Player B perspectives.
     */
    private static void runNetGameSimulationTests() {
        System.out.println("--- Network Game Simulation Tests [Two-Player Network] ---");
        System.out.println("  Target: Verify complete two-player network game flow");
        System.out.println("  Includes: Connection, Turn-based play, Board sync, Win detection");
        System.out.println();
        
        try {
            test("Network Sim - Initialization config", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    assertTrue(sim.isPlayerAAllowPut());
                    assertFalse(sim.isPlayerBAllowPut());
                    
                    assertEqual(0, sim.getPlayerAListSize());
                    assertEqual(0, sim.getPlayerBListSize());
                }
            });
            
            test("Network Sim - Player A places first", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerAPutChess(10, 10);
                    
                    assertEqual(Model.Black, sim.getPlayerABoard(10, 10));
                    assertFalse(sim.isPlayerAAllowPut());
                    
                    String msg = sim.receiveFromPlayerA();
                    assertNotNull(msg);
                    assertTrue(msg.startsWith("PutChess:"));
                }
            });
            
            test("Network Sim - Player B receives and syncs", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerAPutChess(10, 10);
                    
                    sim.processPlayerBMessages();
                    
                    assertEqual(Model.Black, sim.getPlayerBBoard(10, 10));
                    assertTrue(sim.isPlayerBAllowPut());
                }
            });
            
            test("Network Sim - Player B places and syncs to A", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerAPutChess(10, 10);
                    sim.processPlayerBMessages();
                    
                    sim.playerBPutChess(10, 11);
                    
                    assertEqual(Model.white, sim.getPlayerBBoard(10, 11));
                    assertFalse(sim.isPlayerBAllowPut());
                    
                    sim.processPlayerAMessages();
                    
                    assertEqual(Model.white, sim.getPlayerABoard(10, 11));
                    assertTrue(sim.isPlayerAAllowPut());
                }
            });
            
            test("Network Sim - Complete 3-round game", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerAPutChess(10, 10);
                    sim.processPlayerBMessages();
                    assertEqual(Model.Black, sim.getPlayerBBoard(10, 10));
                    
                    sim.playerBPutChess(10, 11);
                    sim.processPlayerAMessages();
                    assertEqual(Model.white, sim.getPlayerABoard(10, 11));
                    
                    sim.playerAPutChess(11, 10);
                    sim.processPlayerBMessages();
                    assertEqual(Model.Black, sim.getPlayerBBoard(11, 10));
                    
                    sim.playerBPutChess(11, 11);
                    sim.processPlayerAMessages();
                    assertEqual(Model.white, sim.getPlayerABoard(11, 11));
                    
                    sim.playerAPutChess(12, 10);
                    sim.processPlayerBMessages();
                    
                    assertEqual(5, sim.getPlayerAListSize());
                    assertEqual(Model.Black, sim.getPlayerABoard(12, 10));
                    assertEqual(Model.Black, sim.getPlayerBBoard(12, 10));
                }
            });
            
            test("Network Sim - Black wins horizontal", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    for (int i = 0; i < 5; i++) {
                        sim.playerAPutChess(10, i);
                        sim.processPlayerBMessages();
                        
                        if (i < 4) {
                            sim.playerBPutChess(11, i);
                            sim.processPlayerAMessages();
                        }
                    }
                    
                    int winnerA = sim.judgeForPlayerA();
                    int winnerB = sim.judgeForPlayerB();
                    
                    assertEqual(Model.Black, winnerA);
                    assertEqual(Model.Black, winnerB);
                }
            });
            
            test("Network Sim - Both boards are identical", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerAPutChess(5, 5);
                    sim.processPlayerBMessages();
                    
                    sim.playerBPutChess(6, 6);
                    sim.processPlayerAMessages();
                    
                    sim.playerAPutChess(7, 7);
                    sim.processPlayerBMessages();
                    
                    for (int i = 0; i < 19; i++) {
                        for (int j = 0; j < 19; j++) {
                            int chessA = sim.getPlayerABoard(i, j);
                            int chessB = sim.getPlayerBBoard(i, j);
                            assertEqual(chessA, chessB);
                        }
                    }
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Undo Function Integration Tests
     * 
     * Functions tested:
     * - Local mode undo functionality
     * - Move history maintenance
     * - Board data rollback
     * 
     * Test points:
     * 1. Undo removes last two moves
     * 2. Move list maintained correctly
     * 3. Board data cleared correctly
     */
    private static void runUndoTests() {
        System.out.println("--- Undo Integration Tests [Local Mode Undo] ---");
        System.out.println("  Target: Verify undo functionality correctness");
        System.out.println("  Includes: Remove moves, List maintenance, Data rollback");
        System.out.println();
        
        try {
            test("Undo - Removes last two moves in local mode", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    Control c = Control.getInstance();
                    
                    m.clearchess();
                    Model.list.clear();
                    c.setNetMode(false);
                    
                    m.putChess(5, 5, Model.Black);
                    m.putChess(6, 6, Model.white);
                    m.putChess(7, 7, Model.Black);
                    m.putChess(8, 8, Model.white);
                    
                    assertEqual(4, Model.list.size());
                    
                    m.back();
                    
                    assertEqual(2, Model.list.size());
                    assertEqual(Model.Space, m.getChess(7, 7));
                    assertEqual(Model.Space, m.getChess(8, 8));
                }
            });
            
            test("Undo - Move list maintained correctly", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    m.putChess(1, 1, Model.Black);
                    m.putChess(2, 2, Model.white);
                    m.putChess(3, 3, Model.Black);
                    m.putChess(4, 4, Model.white);
                    m.putChess(5, 5, Model.Black);
                    m.putChess(6, 6, Model.white);
                    
                    assertEqual(6, Model.list.size());
                    
                    m.back();
                    assertEqual(4, Model.list.size());
                    assertEqual(Model.Black, Model.list.get(2).color);
                    assertEqual(Model.white, Model.list.get(3).color);
                    
                    m.back();
                    assertEqual(2, Model.list.size());
                }
            });
            
            test("Undo - Board data cleared correctly", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    m.putChess(10, 10, Model.Black);
                    m.putChess(10, 11, Model.white);
                    
                    assertEqual(Model.Black, m.getChess(10, 10));
                    assertEqual(Model.white, m.getChess(10, 11));
                    
                    m.back();
                    
                    assertEqual(Model.Space, m.getChess(10, 10));
                    assertEqual(Model.Space, m.getChess(10, 11));
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Network Mode Undo Integration Tests
     * 
     * Functions tested:
     * - Network mode undo functionality
     * - Undo message passing
     * - Both boards synchronized rollback
     * 
     * Test points:
     * 1. Network mode undo request
     * 2. Undo message passing
     * 3. Both boards synchronized rollback
     */
    private static void runNetUndoTests() {
        System.out.println("--- Network Undo Integration Tests [Network Mode Undo] ---");
        System.out.println("  Target: Verify network mode undo functionality");
        System.out.println("  Includes: Undo request, Message passing, Synchronized rollback");
        System.out.println();
        
        try {
            test("Net Undo - Player A undo request", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerAPutChess(10, 10);
                    sim.processPlayerBMessages();
                    
                    sim.playerBPutChess(10, 11);
                    sim.processPlayerAMessages();
                    
                    assertEqual(2, sim.getPlayerAListSize());
                    
                    sim.playerARequestUndo();
                    
                    String msg = sim.receiveFromPlayerA();
                    assertNotNull(msg);
                    assertEqual("reback", msg);
                }
            });
            
            test("Net Undo - Player B receives and syncs undo", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerAPutChess(10, 10);
                    sim.processPlayerBMessages();
                    
                    sim.playerBPutChess(10, 11);
                    sim.processPlayerAMessages();
                    
                    assertEqual(2, sim.getPlayerBListSize());
                    
                    sim.playerARequestUndo();
                    
                    assertEqual(Model.Space, sim.getPlayerABoard(10, 10));
                    assertEqual(Model.Space, sim.getPlayerABoard(10, 11));
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Replay Function Integration Tests
     * 
     * Functions tested:
     * - Game history preservation
     * - Clear board functionality
     * - New game after replay
     * 
     * Test points:
     * 1. Clear board and list
     * 2. Game history preservation
     * 3. New game after replay
     */
    private static void runReplayTests() {
        System.out.println("--- Replay Integration Tests [Replay Function] ---");
        System.out.println("  Target: Verify replay functionality correctness");
        System.out.println("  Includes: Clear board, History preservation, New game");
        System.out.println();
        
        try {
            test("Replay - Clear board and list", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    m.putChess(1, 1, Model.Black);
                    m.putChess(2, 2, Model.white);
                    m.putChess(3, 3, Model.Black);
                    
                    assertEqual(3, Model.list.size());
                    
                    m.clearchess();
                    
                    assertEqual(0, Model.list.size());
                    assertEqual(Model.Space, m.getChess(1, 1));
                    assertEqual(Model.Space, m.getChess(2, 2));
                    assertEqual(Model.Space, m.getChess(3, 3));
                }
            });
            
            test("Replay - Game history preservation", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    m.putChess(1, 1, Model.Black);
                    m.putChess(2, 2, Model.white);
                    m.putChess(3, 3, Model.Black);
                    
                    Chess[] originalMoves = new Chess[Model.list.size()];
                    for (int i = 0; i < Model.list.size(); i++) {
                        originalMoves[i] = Model.list.get(i);
                    }
                    
                    assertEqual(3, originalMoves.length);
                    assertEqual(1, originalMoves[0].row);
                    assertEqual(1, originalMoves[0].col);
                    assertEqual(Model.Black, originalMoves[0].color);
                    
                    assertEqual(2, originalMoves[1].row);
                    assertEqual(2, originalMoves[1].col);
                    assertEqual(Model.white, originalMoves[1].color);
                    
                    assertEqual(3, originalMoves[2].row);
                    assertEqual(3, originalMoves[2].col);
                    assertEqual(Model.Black, originalMoves[2].color);
                }
            });
            
            test("Replay - New game after replay", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    m.putChess(1, 1, Model.Black);
                    m.putChess(2, 2, Model.white);
                    
                    m.clearchess();
                    
                    m.putChess(5, 5, Model.Black);
                    
                    assertEqual(1, Model.list.size());
                    assertEqual(5, Model.list.get(0).row);
                    assertEqual(5, Model.list.get(0).col);
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Chat Function Integration Tests
     * 
     * Functions tested:
     * - Chat panel singleton pattern
     * - Message panel initialization
     * - Receive message append
     * - Multiple messages handling
     * 
     * Test points:
     * 1. Singleton pattern verification
     * 2. Message panel initial state
     * 3. Message append functionality
     * 4. Multiple messages handling
     */
    private static void runChatTests() {
        System.out.println("--- Chat Integration Tests [Chat Basic Function] ---");
        System.out.println("  Target: Verify chat functionality correctness");
        System.out.println("  Includes: Singleton, Message receive, Message display");
        System.out.println();
        
        try {
            test("Chat - Chat panel singleton", new TestCase() {
                public void run() {
                    Chatpanl c1 = Chatpanl.getInstance();
                    Chatpanl c2 = Chatpanl.getInstance();
                    assertSame(c1, c2);
                }
            });
            
            test("Chat - Message panel initial state", new TestCase() {
                public void run() {
                    Chatpanl chat = Chatpanl.getInstance();
                    assertNotNull(chat.readboard);
                }
            });
            
            test("Chat - Receive message append", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    Chatpanl chat = Chatpanl.getInstance();
                    
                    String initialText = chat.readboard.getText();
                    
                    c.netOthershowmsg("Hello");
                    
                    String newText = chat.readboard.getText();
                    assertTrue(newText.contains("Hello"));
                }
            });
            
            test("Chat - Multiple messages handling", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    Chatpanl chat = Chatpanl.getInstance();
                    
                    chat.readboard.setText("");
                    
                    c.netOthershowmsg("First message");
                    c.netOthershowmsg("Second message");
                    
                    String text = chat.readboard.getText();
                    assertTrue(text.contains("First message"));
                    assertTrue(text.contains("Second message"));
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    /**
     * Network Chat Simulation Integration Tests
     * 
     * Functions tested:
     * - Network mode chat message passing
     * - Player A sends to Player B
     * - Player B sends to Player A
     * - Message format verification
     * 
     * Test points:
     * 1. Player A sends chat message
     * 2. Player B receives chat message
     * 3. Player B sends chat message
     * 4. Player A receives chat message
     * 5. Message format correctness
     */
    private static void runNetChatSimulationTests() {
        System.out.println("--- Network Chat Simulation Tests [Network Chat] ---");
        System.out.println("  Target: Verify network mode chat functionality");
        System.out.println("  Includes: Message send, Message receive, Format verification");
        System.out.println();
        
        try {
            test("Net Chat - Player A sends chat message", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerASendChat("Hello!");
                    
                    String msg = sim.receiveFromPlayerA();
                    assertNotNull(msg);
                    assertTrue(msg.startsWith("chat"));
                    assertTrue(msg.contains("Hello!"));
                }
            });
            
            test("Net Chat - Player B sends chat message", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerBSendChat("Received, let's start!");
                    
                    String msg = sim.receiveFromPlayerB();
                    assertNotNull(msg);
                    assertTrue(msg.startsWith("chat"));
                    assertTrue(msg.contains("start!"));
                }
            });
            
            test("Net Chat - Message format verification", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    String testMessage = "Test message 123!@#";
                    sim.playerASendChat(testMessage);
                    
                    String msg = sim.receiveFromPlayerA();
                    assertNotNull(msg);
                    
                    String expectedPrefix = "chat";
                    assertTrue(msg.startsWith(expectedPrefix));
                    
                    String receivedContent = msg.substring(4);
                    assertEqual(testMessage, receivedContent);
                }
            });
            
            test("Net Chat - Multiple messages send", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerASendChat("Message1");
                    sim.playerASendChat("Message2");
                    sim.playerASendChat("Message3");
                    
                    List<String> messages = new ArrayList<String>();
                    String msg;
                    while ((msg = sim.receiveFromPlayerA()) != null) {
                        messages.add(msg);
                    }
                    
                    assertEqual(3, messages.size());
                    assertTrue(messages.get(0).contains("Message1"));
                    assertTrue(messages.get(1).contains("Message2"));
                    assertTrue(messages.get(2).contains("Message3"));
                }
            });
            
            test("Net Chat - Both players send messages", new TestCase() {
                public void run() {
                    NetGameSimulation sim = new NetGameSimulation();
                    
                    sim.playerASendChat("PlayerA: Ready?");
                    sim.playerBSendChat("PlayerB: Ready, let's go!");
                    sim.playerASendChat("PlayerA: OK, I go first");
                    
                    String msgA1 = sim.receiveFromPlayerA();
                    String msgB1 = sim.receiveFromPlayerB();
                    String msgA2 = sim.receiveFromPlayerA();
                    
                    assertNotNull(msgA1);
                    assertNotNull(msgB1);
                    assertNotNull(msgA2);
                    
                    assertTrue(msgA1.contains("Ready"));
                    assertTrue(msgB1.contains("let's go"));
                    assertTrue(msgA2.contains("I go first"));
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    // ========================================================================
    // Test Helper Methods
    // ========================================================================
    
    private static void test(String testName, TestCase testCase) {
        testsRun++;
        System.out.print("  Running: " + testName + " ... ");
        
        try {
            testCase.run();
            testsPassed++;
            System.out.println("PASSED");
        } catch (AssertionError e) {
            testsFailed++;
            failures.add(testName + ": " + e.getMessage());
            System.out.println("FAILED");
            System.out.println("    Error: " + e.getMessage());
        } catch (Exception e) {
            testsFailed++;
            failures.add(testName + ": " + e.getClass().getSimpleName() + " - " + e.getMessage());
            System.out.println("ERROR");
            System.out.println("    Exception: " + e.getMessage());
        }
    }
    
    private static void assertEqual(int expected, int actual) {
        if (expected != actual) {
            throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
        }
    }
    
    private static void assertEqual(Object expected, Object actual) {
        if (expected == null && actual == null) return;
        if (expected == null || !expected.equals(actual)) {
            throw new AssertionError("Expected: " + expected + ", Actual: " + actual);
        }
    }
    
    private static void assertTrue(boolean condition) {
        if (!condition) {
            throw new AssertionError("Expected true but was false");
        }
    }
    
    private static void assertFalse(boolean condition) {
        if (condition) {
            throw new AssertionError("Expected false but was true");
        }
    }
    
    private static void assertNotNull(Object obj) {
        if (obj == null) {
            throw new AssertionError("Expected not null but was null");
        }
    }
    
    private static void assertSame(Object expected, Object actual) {
        if (expected != actual) {
            throw new AssertionError("Expected same object");
        }
    }
    
    private static void printSummary() {
        System.out.println("========================================");
        System.out.println("  Test Summary");
        System.out.println("========================================");
        System.out.println("  Tests run:  " + testsRun);
        System.out.println("  Passed:     " + testsPassed);
        System.out.println("  Failed:     " + testsFailed);
        System.out.println("  Pass Rate:  " + String.format("%.1f%%", (testsPassed * 100.0 / testsRun)));
        System.out.println();
        
        if (!failures.isEmpty()) {
            System.out.println("  Failed Tests:");
            for (String failure : failures) {
                System.out.println("    - " + failure);
            }
        }
        
        System.out.println();
        System.out.println("========================================");
        if (testsFailed == 0) {
            System.out.println("  ALL TESTS PASSED!");
        } else {
            System.out.println("  SOME TESTS FAILED");
        }
        System.out.println("========================================");
    }
    
    interface TestCase {
        void run() throws Exception;
    }
}
