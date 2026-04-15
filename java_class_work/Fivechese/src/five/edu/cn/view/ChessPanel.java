package five.edu.cn.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import five.edu.cn.controller.Control;
import five.edu.cn.model.Chess;
import five.edu.cn.model.Model;

public class ChessPanel extends JPanel {
    private static final Logger LOGGER = Logger.getLogger(ChessPanel.class.getName());
    private static final String DEFAULT_BOARD_IMAGE = "painting/view.jpg";

    private static volatile ChessPanel instance;
    private static final Object LOCK = new Object();

    private int gap = 50;
    private int unit = 10;
    private int boardOffsetX = 10;
    private int boardOffsetY = 10;
    private Image boardImage;

    private ChessPanel() {
        initializeComponents();
        loadBoardImage();
    }

    public static ChessPanel getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new ChessPanel();
                }
            }
        }
        return instance;
    }

    private void initializeComponents() {
        JButton undoButton = new JButton("悔棋");
        add(undoButton);
        undoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Control.getInstance().handleUndo();
                repaint();
            }
        });

        JButton restartButton = new JButton("重新开始游戏");
        add(restartButton);
        restartButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    null, 
                    "确定要重新开始游戏吗？", 
                    "重新开始游戏", 
                    JOptionPane.OK_CANCEL_OPTION
                );
                if (choice == JOptionPane.OK_OPTION) {
                    Control.getInstance().resetGame();
                    Control.getInstance().selectGameMode();
                    Control.getInstance().selectPlayerColor();
                }
            }
        });

        JButton startButton = new JButton("开始游戏");
        add(startButton);
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Control.getInstance().resetGame();
                Control.getInstance().selectGameMode();
                Control.getInstance().selectPlayerColor();
            }
        });

        JButton exitButton = new JButton("退出游戏");
        add(exitButton);
        exitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int choice = JOptionPane.showConfirmDialog(
                    null, 
                    "确定要退出游戏吗？", 
                    "退出游戏", 
                    JOptionPane.OK_CANCEL_OPTION
                );
                if (choice == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });

        JButton replayButton = new JButton("复盘");
        add(replayButton);
        replayButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startReplay();
            }
        });

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                updateBoardLayout();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                handleMouseClick(e.getX(), e.getY());
            }
        });
    }

    private void loadBoardImage() {
        try {
            File imageFile = new File(DEFAULT_BOARD_IMAGE);
            if (imageFile.exists()) {
                boardImage = new ImageIcon(DEFAULT_BOARD_IMAGE).getImage();
            } else {
                URL resourceUrl = getClass().getClassLoader().getResource(DEFAULT_BOARD_IMAGE);
                if (resourceUrl != null) {
                    boardImage = new ImageIcon(resourceUrl).getImage();
                } else {
                    LOGGER.warning("Board image not found: " + DEFAULT_BOARD_IMAGE);
                }
            }
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load board image", e);
        }
    }

    private void updateBoardLayout() {
        int width = getWidth();
        int height = getHeight();
        int min = Math.min(width, height);
        unit = (min - 2 * gap) / (Model.BOARD_SIZE - 1);
        boardOffsetX = (width - (Model.BOARD_SIZE - 1) * unit) / 2;
        boardOffsetY = (height - (Model.BOARD_SIZE - 1) * unit) / 2;
        repaint();
    }

    private void handleMouseClick(int x, int y) {
        int col = calculateGridPosition(x, boardOffsetX);
        int row = calculateGridPosition(y, boardOffsetY);
        
        if (row >= 0 && row < Model.BOARD_SIZE && col >= 0 && col < Model.BOARD_SIZE) {
            Control.getInstance().handleLocalMove(row, col);
        }
    }

    private int calculateGridPosition(int coordinate, int offset) {
        int relativePos = coordinate - offset;
        if (relativePos < 0) {
            return -1;
        }
        int mod = relativePos % unit;
        if (mod > unit / 2) {
            return (relativePos / unit) + 1;
        } else {
            return relativePos / unit;
        }
    }

    private void startReplay() {
        List<Chess> history = Control.getInstance().getGameHistory();
        if (history.isEmpty()) {
            JOptionPane.showMessageDialog(null, "没有棋谱可以复盘");
            return;
        }

        final LinkedList<Chess> replayHistory = new LinkedList<>(history);
        Control.getInstance().resetGame();

        new Thread(() -> {
            Model model = Model.getInstance();
            for (Chess chess : replayHistory) {
                model.putChess(chess.getRow(), chess.getCol(), chess.getColor());
                repaint();
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    LOGGER.log(Level.INFO, "Replay interrupted", e);
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "Chess-Replay-Thread").start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (boardImage != null) {
            g.drawImage(boardImage, 0, 0, getWidth(), getHeight(), this);
        }
        
        drawBoardGrid(g);
        drawChessPieces(g);
    }

    private void drawBoardGrid(Graphics g) {
        for (int i = 0; i < Model.BOARD_SIZE; i++) {
            g.drawLine(
                boardOffsetX, 
                boardOffsetY + i * unit, 
                boardOffsetX + unit * (Model.BOARD_SIZE - 1), 
                boardOffsetY + i * unit
            );
            g.drawLine(
                boardOffsetX + i * unit, 
                boardOffsetY, 
                boardOffsetX + i * unit, 
                boardOffsetY + unit * (Model.BOARD_SIZE - 1)
            );
        }
    }

    private void drawChessPieces(Graphics g) {
        Model model = Model.getInstance();
        List<Chess> history = model.getChessHistory();
        
        for (Chess chess : history) {
            if (chess.getColor() == Model.BLACK) {
                g.setColor(Color.BLACK);
            } else if (chess.getColor() == Model.WHITE) {
                g.setColor(Color.WHITE);
            } else {
                continue;
            }
            
            int x = boardOffsetX + chess.getCol() * unit - unit / 2;
            int y = boardOffsetY + chess.getRow() * unit - unit / 2;
            g.fillOval(x, y, unit, unit);
        }
    }
}
