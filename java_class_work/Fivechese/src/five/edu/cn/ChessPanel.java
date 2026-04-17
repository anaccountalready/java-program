package five.edu.cn;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ChessPanel extends JPanel {
    private int gap = 40;
    private int unit = 25;
    private int lx = 30;
    private int ly = 60;
    private int boardMargin = 20;
    Image imageIcon = new ImageIcon("painting/view.jpg").getImage();
    
    private static final Color BOARD_COLOR = new Color(222, 184, 135);
    private static final Color BOARD_LINE_COLOR = new Color(139, 105, 30);
    private static final Color BOARD_BORDER_COLOR = new Color(101, 67, 33);
    
    private static final Color BUTTON_BG = new Color(139, 69, 19);
    private static final Color BUTTON_FG = new Color(255, 248, 220);
    private static final Color BUTTON_HOVER_BG = new Color(160, 82, 45);

    private static ChessPanel instance = new ChessPanel();

    private ChessPanel() {
        setOpaque(false);
        setLayout(null);
        
        setupButtons();
        
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent arg0) {
                resizeComponents();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                int col;
                int row;
                int colmod = (e.getX() - lx) % unit;
                int rowmod = (e.getY() - ly) % unit;
                if (colmod > unit / 2) {
                    col = ((e.getX() - lx) / unit) + 1;
                } else
                    col = (e.getX() - lx) / unit;
                if (rowmod > unit / 2) {
                    row = ((e.getY() - ly) / unit) + 1;
                } else
                    row = (e.getY() - ly) / unit;
                Control.getInstance().localPutChess(row, col);
            }
        });
    }

    private void setupButtons() {
        createStyledButton("悔棋", 10, 10, 80, 35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Control.getInstance().localremoveChess();
                repaint();
            }
        });

        createStyledButton("重新开始", 100, 10, 90, 35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int choice = JOptionPane.showConfirmDialog(null, "是否重新开始游戏", "重新开始游戏",
                        JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    Model.getInstance().clearchess();
                    repaint();
                    Control.getInstance().setMode();
                    Control.getInstance().setColor();
                }
            }
        });

        createStyledButton("开始游戏", 200, 10, 90, 35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                Model.getInstance().clearchess();
                repaint();
                Control.getInstance().setMode();
                Control.getInstance().setColor();
            }
        });

        createStyledButton("复盘", 300, 10, 70, 35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                final LinkedList<Chess> fupan = new LinkedList<Chess>();
                for (Chess chess : Model.list) {
                    fupan.add(chess);
                }
                Model.getInstance().clearchess();
                repaint();
                new Thread() {
                    public void run() {
                        for (int i = 0; i < fupan.size(); i++) {
                            Model.list.add(fupan.get(i));
                            repaint();
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }.start();
            }
        });

        createStyledButton("退出游戏", 380, 10, 90, 35, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                int choice = JOptionPane.showConfirmDialog(null, "是否退出游戏", "退出游戏",
                        JOptionPane.OK_CANCEL_OPTION);
                if (choice == JOptionPane.OK_OPTION) {
                    System.exit(0);
                }
            }
        });
    }

    private JButton createStyledButton(String text, int x, int y, int width, int height, ActionListener listener) {
        JButton button = new JButton(text);
        button.setBounds(x, y, width, height);
        button.setFont(new Font("微软雅黑", Font.BOLD, 13));
        button.setForeground(BUTTON_FG);
        button.setBackground(BUTTON_BG);
        button.setBorder(new LineBorder(BOARD_BORDER_COLOR, 2, true));
        button.setFocusPainted(false);
        button.setOpaque(true);
        button.setContentAreaFilled(true);
        button.setHorizontalAlignment(SwingConstants.CENTER);
        
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_BG);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_BG);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                button.setBackground(BOARD_BORDER_COLOR);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                button.setBackground(BUTTON_HOVER_BG);
            }
        });
        
        button.addActionListener(listener);
        add(button);
        return button;
    }

    private void resizeComponents() {
        int width = getWidth();
        int height = getHeight();
        int min = width < height ? width : height;
        unit = (min - 2 * gap - 2 * boardMargin) / (Model.width - 1);
        if (unit < 20)
            unit = 20;
        
        int boardWidth = unit * (Model.width - 1);
        lx = (width - boardWidth) / 2;
        ly = 55 + boardMargin;
        
        repaint();
    }

    public static ChessPanel getInstance() {
        return instance;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);

        if (imageIcon != null) {
            g2d.drawImage(imageIcon, 0, 0, this.getWidth(), this.getHeight(), this);
        }

        drawBoardBackground(g2d);
        drawPanel(g2d);
        drawchess(g2d);
    }

    private void drawBoardBackground(Graphics2D g2d) {
        int boardWidth = unit * (Model.width - 1) + 2 * boardMargin;
        int boardHeight = boardWidth;
        int bx = lx - boardMargin;
        int by = ly - boardMargin;

        GradientPaint outerGradient = new GradientPaint(
                bx, by, new Color(101, 67, 33),
                bx + boardWidth, by + boardHeight, new Color(139, 90, 43));
        g2d.setPaint(outerGradient);
        g2d.fillRoundRect(bx - 3, by - 3, boardWidth + 6, boardHeight + 6, 15, 15);

        GradientPaint innerGradient = new GradientPaint(
                bx, by, new Color(222, 184, 135),
                bx + boardWidth, by + boardHeight, new Color(210, 170, 120));
        g2d.setPaint(innerGradient);
        g2d.fillRect(bx, by, boardWidth, boardHeight);

        g2d.setColor(BOARD_BORDER_COLOR);
        g2d.setStroke(new BasicStroke(3));
        g2d.drawRect(bx, by, boardWidth, boardHeight);

        g2d.setStroke(new BasicStroke(1));
        g2d.drawRect(bx + 3, by + 3, boardWidth - 6, boardHeight - 6);
    }

    private void drawchess(Graphics2D g2d) {
        Model m = Model.getInstance();
        LinkedList<Chess> list = m.getList();
        
        for (int i = 0; i < list.size(); i++) {
            Chess n = list.get(i);
            if (n.color == -1) {
                drawBlackChess(g2d, lx + n.col * unit, ly + n.row * unit, 
                        i == list.size() - 1);
            } else if (n.color == 1) {
                drawWhiteChess(g2d, lx + n.col * unit, ly + n.row * unit, 
                        i == list.size() - 1);
            }
        }
    }

    private void drawBlackChess(Graphics2D g2d, int x, int y, boolean isLast) {
        int r = (int) (unit * 0.45);
        int rx = x - r;
        int ry = y - r;
        int diameter = r * 2;

        g2d.setColor(new Color(0, 0, 0, 50));
        g2d.fillOval(rx + 3, ry + 3, diameter, diameter);

        GradientPaint gradient = new GradientPaint(
                rx - r / 3, ry - r / 3, new Color(80, 80, 80),
                rx + diameter, ry + diameter, new Color(10, 10, 10));
        g2d.setPaint(gradient);
        g2d.fillOval(rx, ry, diameter, diameter);

        g2d.setColor(new Color(255, 255, 255, 40));
        g2d.fillOval(rx + diameter / 6, ry + diameter / 6, diameter / 3, diameter / 3);

        g2d.setColor(new Color(60, 60, 60));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(rx, ry, diameter, diameter);

        if (isLast) {
            g2d.setColor(new Color(255, 50, 50, 180));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(rx - 2, ry - 2, diameter + 4, diameter + 4);
        }
    }

    private void drawWhiteChess(Graphics2D g2d, int x, int y, boolean isLast) {
        int r = (int) (unit * 0.45);
        int rx = x - r;
        int ry = y - r;
        int diameter = r * 2;

        g2d.setColor(new Color(0, 0, 0, 30));
        g2d.fillOval(rx + 3, ry + 3, diameter, diameter);

        GradientPaint gradient = new GradientPaint(
                rx - r / 3, ry - r / 3, new Color(255, 255, 255),
                rx + diameter, ry + diameter, new Color(200, 200, 200));
        g2d.setPaint(gradient);
        g2d.fillOval(rx, ry, diameter, diameter);

        g2d.setColor(new Color(255, 255, 255, 200));
        g2d.fillOval(rx + diameter / 6, ry + diameter / 6, diameter / 3, diameter / 3);

        g2d.setColor(new Color(150, 150, 150));
        g2d.setStroke(new BasicStroke(1));
        g2d.drawOval(rx, ry, diameter, diameter);

        if (isLast) {
            g2d.setColor(new Color(255, 50, 50, 180));
            g2d.setStroke(new BasicStroke(2));
            g2d.drawOval(rx - 2, ry - 2, diameter + 4, diameter + 4);
        }
    }

    private void drawPanel(Graphics2D g2d) {
        g2d.setColor(BOARD_LINE_COLOR);
        g2d.setStroke(new BasicStroke(1.2f));

        for (int i = 0; i < Model.width; i++) {
            g2d.drawLine(lx, ly + i * unit, lx + unit * (Model.width - 1), ly + i * unit);
            g2d.drawLine(lx + i * unit, ly, lx + i * unit, ly + unit * (Model.width - 1));
        }

        drawStarPoints(g2d);
    }

    private void drawStarPoints(Graphics2D g2d) {
        int r = 4;
        int[] starRows = {3, 9, 15};
        int[] starCols = {3, 9, 15};

        g2d.setColor(BOARD_LINE_COLOR);
        for (int row : starRows) {
            for (int col : starCols) {
                int x = lx + col * unit;
                int y = ly + row * unit;
                g2d.fillOval(x - r, y - r, r * 2, r * 2);
            }
        }
    }
}
