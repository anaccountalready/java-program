package five.edu.cn.model;

import java.io.Serializable;

public class Chess implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private final int color;
    private final int row;
    private final int col;

    public Chess() {
        this.color = Model.SPACE;
        this.row = 0;
        this.col = 0;
    }

    public Chess(int row, int col, int color) {
        if (row < 0 || row >= Model.BOARD_SIZE) {
            throw new IllegalArgumentException("Row out of bounds: " + row);
        }
        if (col < 0 || col >= Model.BOARD_SIZE) {
            throw new IllegalArgumentException("Column out of bounds: " + col);
        }
        if (color != Model.BLACK && color != Model.WHITE && color != Model.SPACE) {
            throw new IllegalArgumentException("Invalid color: " + color);
        }
        this.row = row;
        this.col = col;
        this.color = color;
    }

    public int getColor() {
        return color;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    @Override
    public String toString() {
        return "Chess [color=" + color + ", row=" + row + ", col=" + col + "]";
    }

    @Override
    public int hashCode() {
        int result = 17;
        result = 31 * result + color;
        result = 31 * result + row;
        result = 31 * result + col;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Chess other = (Chess) obj;
        return color == other.color && row == other.row && col == other.col;
    }
}
