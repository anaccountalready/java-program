package five.edu.cn;

import java.util.ArrayList;
import java.util.List;

public class TestRunner {
    
    private static int testsRun = 0;
    private static int testsPassed = 0;
    private static int testsFailed = 0;
    private static List<String> failures = new ArrayList<String>();
    
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println("  FiveChess Test Suite");
        System.out.println("========================================");
        System.out.println();
        
        runChessTests();
        runModelTests();
        runControlTests();
        runLocalGameTests();
        runNetGameTests();
        runUndoTests();
        runReplayTests();
        runChatTests();
        
        printSummary();
    }
    
    private static void runChessTests() {
        System.out.println("--- Chess Unit Tests ---");
        
        try {
            test("Chess constructor", new TestCase() {
                public void run() {
                    Chess c = new Chess(3, 4, Model.white);
                    assertEqual(3, c.row);
                    assertEqual(4, c.col);
                    assertEqual(Model.white, c.color);
                }
            });
            
            test("Chess default constructor", new TestCase() {
                public void run() {
                    Chess c = new Chess();
                    assertNotNull(c);
                }
            });
            
            test("Chess toString", new TestCase() {
                public void run() {
                    Chess c = new Chess(5, 6, Model.Black);
                    String expected = "Chess [color=-1, row=5, col=6]";
                    assertEqual(expected, c.toString());
                }
            });
            
            test("Chess properties", new TestCase() {
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
    
    private static void runModelTests() {
        System.out.println("--- Model Unit Tests ---");
        
        try {
            Model.getInstance().clearchess();
            Model.list.clear();
            
            test("Model singleton", new TestCase() {
                public void run() {
                    Model m1 = Model.getInstance();
                    Model m2 = Model.getInstance();
                    assertSame(m1, m2);
                }
            });
            
            test("Model initial board is empty", new TestCase() {
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
            
            test("Model putChess valid position", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    boolean result = m.putChess(5, 5, Model.Black);
                    assertTrue(result);
                    assertEqual(Model.Black, m.getChess(5, 5));
                }
            });
            
            test("Model putChess invalid position - occupied", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    m.putChess(5, 5, Model.Black);
                    boolean result = m.putChess(5, 5, Model.white);
                    assertFalse(result);
                }
            });
            
            test("Model putChess invalid position - out of bounds", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    assertFalse(m.putChess(-1, 0, Model.Black));
                    assertFalse(m.putChess(0, -1, Model.Black));
                    assertFalse(m.putChess(19, 0, Model.Black));
                    assertFalse(m.putChess(0, 19, Model.Black));
                }
            });
            
            test("Model getChess out of bounds returns Space", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    assertEqual(Model.Space, m.getChess(-1, 0));
                    assertEqual(Model.Space, m.getChess(0, -1));
                    assertEqual(Model.Space, m.getChess(19, 0));
                    assertEqual(Model.Space, m.getChess(0, 19));
                }
            });
            
            test("Model list tracking", new TestCase() {
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
    
    private static void runControlTests() {
        System.out.println("--- Control Unit Tests ---");
        
        try {
            test("Control singleton", new TestCase() {
                public void run() {
                    Control c1 = Control.getInstance();
                    Control c2 = Control.getInstance();
                    assertSame(c1, c2);
                }
            });
            
            test("Control initial settings", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    assertNotNull(c);
                }
            });
            
            test("Control set and get localColor", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setLocalColor(Model.Black);
                    assertEqual(Model.Black, c.getLocalColor());
                    
                    c.setLocalColor(Model.white);
                    assertEqual(Model.white, c.getLocalColor());
                }
            });
            
            test("Control set and get otherColor", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setOtherColor(Model.white);
                    assertEqual(Model.white, c.getOtherColor());
                    
                    c.setOtherColor(Model.Black);
                    assertEqual(Model.Black, c.getOtherColor());
                }
            });
            
            test("Control set and get netMode", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setNetMode(false);
                    assertFalse(c.isNetMode());
                    
                    c.setNetMode(true);
                    assertTrue(c.isNetMode());
                }
            });
            
            test("Control set and get allowPutChess", new TestCase() {
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
    
    private static void runLocalGameTests() {
        System.out.println("--- Local Game Integration Tests ---");
        
        try {
            Model.getInstance().clearchess();
            Model.list.clear();
            Control.getInstance().setNetMode(false);
            Control.getInstance().setLocalColor(Model.Black);
            
            test("Local game - alternate turns", new TestCase() {
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
            
            test("Local game - Black wins horizontal", new TestCase() {
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
            
            test("Local game - White wins vertical", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    for (int i = 0; i < 5; i++) {
                        m.putChess(i, 10, Model.white);
                    }
                    
                    int winner = m.judge();
                    assertEqual(Model.white, winner);
                }
            });
            
            test("Local game - Black wins diagonal", new TestCase() {
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
            
            test("Local game - no winner", new TestCase() {
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
            
            test("Local game - cannot place on occupied", new TestCase() {
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
            
            test("Local game - board bounds", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    m.clearchess();
                    Model.list.clear();
                    
                    assertFalse(m.putChess(-1, 0, Model.Black));
                    assertFalse(m.putChess(0, -1, Model.Black));
                    assertFalse(m.putChess(19, 0, Model.Black));
                    assertFalse(m.putChess(0, 19, Model.Black));
                    
                    assertTrue(m.putChess(0, 0, Model.Black));
                    assertTrue(m.putChess(18, 18, Model.white));
                }
            });
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println();
    }
    
    private static void runNetGameTests() {
        System.out.println("--- Network Game Integration Tests ---");
        
        try {
            Model.getInstance().clearchess();
            Model.list.clear();
            Control.getInstance().setNetMode(true);
            Control.getInstance().setLocalColor(Model.Black);
            Control.getInstance().setOtherColor(Model.white);
            Control.getInstance().setAllowPutChess(true);
            
            test("Network mode - initialization", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    assertTrue(c.isNetMode());
                    assertEqual(Model.Black, c.getLocalColor());
                    assertEqual(Model.white, c.getOtherColor());
                    assertTrue(c.isAllowPutChess());
                }
            });
            
            test("Network mode - color switch", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setLocalColor(Model.white);
                    c.setOtherColor(Model.Black);
                    assertEqual(Model.white, c.getLocalColor());
                    assertEqual(Model.Black, c.getOtherColor());
                }
            });
            
            test("Network mode - allowPutChess toggle", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    c.setAllowPutChess(false);
                    assertFalse(c.isAllowPutChess());
                    
                    c.setAllowPutChess(true);
                    assertTrue(c.isAllowPutChess());
                }
            });
            
            test("Network mode - other player puts chess", new TestCase() {
                public void run() {
                    Model m = Model.getInstance();
                    Control c = Control.getInstance();
                    
                    c.setOtherColor(Model.white);
                    c.setAllowPutChess(false);
                    
                    m.putChess(5, 5, Model.white);
                    
                    assertEqual(Model.white, m.getChess(5, 5));
                    assertEqual(1, Model.list.size());
                }
            });
            
            test("Network mode - player colors are opposite", new TestCase() {
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
    
    private static void runUndoTests() {
        System.out.println("--- Undo Integration Tests ---");
        
        try {
            test("Undo - removes last two moves in local mode", new TestCase() {
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
            
            test("Undo - chess list is maintained correctly", new TestCase() {
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
            
            test("Undo - board data is cleared", new TestCase() {
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
    
    private static void runReplayTests() {
        System.out.println("--- Replay Integration Tests ---");
        
        try {
            test("Replay - clearchess clears board and list", new TestCase() {
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
            
            test("Replay - game history preservation", new TestCase() {
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
            
            test("Replay - new game after replay", new TestCase() {
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
    
    private static void runChatTests() {
        System.out.println("--- Chat Integration Tests ---");
        
        try {
            test("Chat - Chatpanl singleton", new TestCase() {
                public void run() {
                    Chatpanl c1 = Chatpanl.getInstance();
                    Chatpanl c2 = Chatpanl.getInstance();
                    assertSame(c1, c2);
                }
            });
            
            test("Chat - readboard initial state", new TestCase() {
                public void run() {
                    Chatpanl chat = Chatpanl.getInstance();
                    assertNotNull(chat.readboard);
                }
            });
            
            test("Chat - netOthershowmsg appends message", new TestCase() {
                public void run() {
                    Control c = Control.getInstance();
                    Chatpanl chat = Chatpanl.getInstance();
                    
                    String initialText = chat.readboard.getText();
                    
                    c.netOthershowmsg("Hello");
                    
                    String newText = chat.readboard.getText();
                    assertTrue(newText.contains("Hello"));
                }
            });
            
            test("Chat - multiple messages", new TestCase() {
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
        System.out.println("  Tests run: " + testsRun);
        System.out.println("  Passed:    " + testsPassed);
        System.out.println("  Failed:    " + testsFailed);
        System.out.println();
        
        if (!failures.isEmpty()) {
            System.out.println("  Failed Tests:");
            for (String failure : failures) {
                System.out.println("    - " + failure);
            }
        }
        
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
