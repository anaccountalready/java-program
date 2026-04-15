package five.edu.cn.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Model {
    public static final int WHITE = 1;
    public static final int BLACK = -1;
    public static final int SPACE = 0;
    public static final int BOARD_SIZE = 19;

    private static volatile Model instance;
    private static final Object LOCK = new Object();

    private final int[][] boardData;
    private final LinkedList<Chess> chessHistory;
    private final ReadWriteLock rwLock;
    
    private int lastRow;
    private int lastCol;

    private Model() {
        this.boardData = new int[BOARD_SIZE][BOARD_SIZE];
        this.chessHistory = new LinkedList<>();
        this.rwLock = new ReentrantReadWriteLock();
        this.lastRow = -1;
        this.lastCol = -1;
    }

    public static Model getInstance() {
        if (instance == null) {
            synchronized (LOCK) {
                if (instance == null) {
                    instance = new Model();
                }
            }
        }
        return instance;
    }

    public boolean putChess(int row, int col, int color) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return false;
        }
        if (color != BLACK && color != WHITE) {
            return false;
        }

        rwLock.writeLock().lock();
        try {
            if (boardData[row][col] != SPACE) {
                return false;
            }
            boardData[row][col] = color;
            chessHistory.add(new Chess(row, col, color));
            lastRow = row;
            lastCol = col;
            return true;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public int getChess(int row, int col) {
        if (row < 0 || row >= BOARD_SIZE || col < 0 || col >= BOARD_SIZE) {
            return SPACE;
        }
        rwLock.readLock().lock();
        try {
            return boardData[row][col];
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public int judge() {
        rwLock.readLock().lock();
        try {
            if (lastRow < 0 || lastCol < 0) {
                return SPACE;
            }
            
            int currentColor = boardData[lastRow][lastCol];
            if (currentColor == SPACE) {
                return SPACE;
            }

            if (checkHorizontal(currentColor) || 
                checkVertical(currentColor) || 
                checkDiagonalLeft(currentColor) || 
                checkDiagonalRight(currentColor)) {
                return currentColor;
            }
            return SPACE;
        } finally {
            rwLock.readLock().unlock();
        }
    }

    private boolean checkHorizontal(int color) {
        int count = 1;
        for (int i = lastCol + 1; i < BOARD_SIZE && boardData[lastRow][i] == color; i++) {
            count++;
        }
        for (int i = lastCol - 1; i >= 0 && boardData[lastRow][i] == color; i--) {
            count++;
        }
        return count >= 5;
    }

    private boolean checkVertical(int color) {
        int count = 1;
        for (int i = lastRow + 1; i < BOARD_SIZE && boardData[i][lastCol] == color; i++) {
            count++;
        }
        for (int i = lastRow - 1; i >= 0 && boardData[i][lastCol] == color; i--) {
            count++;
        }
        return count >= 5;
    }

    private boolean checkDiagonalLeft(int color) {
        int count = 1;
        for (int i = lastRow - 1, j = lastCol - 1; 
             i >= 0 && j >= 0 && boardData[i][j] == color; 
             i--, j--) {
            count++;
        }
        for (int i = lastRow + 1, j = lastCol + 1; 
             i < BOARD_SIZE && j < BOARD_SIZE && boardData[i][j] == color; 
             i++, j++) {
            count++;
        }
        return count >= 5;
    }

    private boolean checkDiagonalRight(int color) {
        int count = 1;
        for (int i = lastRow - 1, j = lastCol + 1; 
             i >= 0 && j < BOARD_SIZE && boardData[i][j] == color; 
             i--, j++) {
            count++;
        }
        for (int i = lastRow + 1, j = lastCol - 1; 
             i < BOARD_SIZE && j >= 0 && boardData[i][j] == color; 
             i++, j--) {
            count++;
        }
        return count >= 5;
    }

    public void clearChess() {
        rwLock.writeLock().lock();
        try {
            chessHistory.clear();
            for (int i = 0; i < BOARD_SIZE; i++) {
                for (int j = 0; j < BOARD_SIZE; j++) {
                    boardData[i][j] = SPACE;
                }
            }
            lastRow = -1;
            lastCol = -1;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public boolean undo() {
        rwLock.writeLock().lock();
        try {
            if (chessHistory.size() < 2) {
                return false;
            }
            
            Chess last1 = chessHistory.removeLast();
            boardData[last1.getRow()][last1.getCol()] = SPACE;
            
            Chess last2 = chessHistory.removeLast();
            boardData[last2.getRow()][last2.getCol()] = SPACE;
            
            if (!chessHistory.isEmpty()) {
                Chess current = chessHistory.getLast();
                lastRow = current.getRow();
                lastCol = current.getCol();
            } else {
                lastRow = -1;
                lastCol = -1;
            }
            return true;
        } finally {
            rwLock.writeLock().unlock();
        }
    }

    public List<Chess> getChessHistory() {
        rwLock.readLock().lock();
        try {
            return new ArrayList<>(chessHistory);
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public int getHistorySize() {
        rwLock.readLock().lock();
        try {
            return chessHistory.size();
        } finally {
            rwLock.readLock().unlock();
        }
    }

    public Chess getLastChess() {
        rwLock.readLock().lock();
        try {
            if (chessHistory.isEmpty()) {
                return null;
            }
            return chessHistory.getLast();
        } finally {
            rwLock.readLock().unlock();
        }
    }
}
