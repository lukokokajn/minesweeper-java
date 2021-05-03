package cz.educanet.minesweeper.logic;

import java.util.Random;

public class Minesweeper {

    private int rowsCount;
    private int columnsCount;
    private boolean bombs[][];
    private int fields[][];

    public Minesweeper(int rows, int columns) {
        this.rowsCount = rows;
        this.columnsCount = columns;
        this.bombs = new boolean[rows][columns];
        this.fields = new int[rows][columns];
        Random random = new Random();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < columns; j++) {
                fields[i][j] = 0; //hidden
                if (random.nextInt(100) > 80) {
                    bombs[i][j] = true;
                } else {
                    bombs[i][j] = false;
                }
            }
        }
    }

    /**
     * 0 - Hidden
     * 1 - Visible
     * 2 - Flag
     * 3 - Question mark
     *
     * @param x X
     * @param y Y
     * @return field type
     */
    public int getField(int x, int y) {
        return this.fields[y][x];
    }

    /**
     * Toggles the field state, ie.
     * 0 -> 1,
     * 1 -> 2,
     * 2 -> 3 and
     * 3 -> 0
     *
     * @param x X
     * @param y Y
     */

    public void toggleFieldState(int x, int y) {
        if (fields[y][x] == 0) {
            fields[y][x] = 2;
        } else {
            fields[y][x] = fields[x][y] + 1;
            if (fields[y][x] == 4) {
                fields[y][x] = 0;
            }
        }
        System.out.println("Toggle Reveal");
    }

    /**
     * Reveals the field and all fields adjacent (with 0 adjacent bombs) and all fields adjacent to the adjacent fields... ect.
     *
     * @param x X
     * @param y Y
     */
    public void reveal(int x, int y) {
        if (x >= 0 && y >= 0 && x < columnsCount && y < rowsCount) {
            if (fields[y][x] == 0) {
                fields[y][x] = 1;
                if (getAdjacentBombCount(x, y) == 0) {
                    reveal(x - 1, y - 1);
                    reveal(x, y - 1);
                    reveal(x + 1, y - 1);
                    reveal(x - 1, y);
                    reveal(x + 1, y);
                    reveal(x - 1, y + 1);
                    reveal(x, y + 1);
                    reveal(x + 1, y + 1);
                }
            }
        }
    }

    /**
     * Returns the amount of adjacent bombs
     *
     * @param x X
     * @param y Y
     * @return number of adjacent bombs
     */
    public int getAdjacentBombCount(int x, int y) {
        int bomb = 0;
        if (x > 0) {
            if (isBombOnPosition(x - 1, y)) {
                bomb++;
            }
            if (y > 0) {
                if (isBombOnPosition(x - 1, y - 1)) {
                    bomb++;
                }
            }
            if (y < rowsCount - 1) {
                if (isBombOnPosition(x - 1, y + 1)) {
                    bomb++;
                }
            }
        }
        if (x < columnsCount - 1) {
            if (isBombOnPosition(x + 1, y)) {
                bomb++;
            }
            if (y > 0) {
                if (isBombOnPosition(x + 1, y - 1)) {
                    bomb++;
                }
            }
            if (y < rowsCount - 1) {
                if (isBombOnPosition(x + 1, y + 1)) {
                    bomb++;
                }
            }
        }
        if (y > 0) {
            if (isBombOnPosition(x, y - 1)) {
                bomb++;
            }
        }
        if (y < rowsCount - 1) {
            if (isBombOnPosition(x, y + 1)) {
                bomb++;
            }
        }
        return bomb;
    }

    /**
     * Checks if there is a bomb on the current position
     *
     * @param x X
     * @param y Y
     * @return true if bomb on position
     */
    public boolean isBombOnPosition(int x, int y) {
        return this.bombs[y][x];
    }

    /**
     * Returns the amount of bombs on the field
     *
     * @return bomb count
     */
    public int getBombCount() {
        int a = 0;
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (isBombOnPosition(j, i)) {
                    a++;
                }
            }
        }
        return a;
    }

    /**
     * total bombs - number of flags
     *
     * @return remaining bomb count
     */
    public int getRemainingBombCount() {
        int flags = 0;
        for (int i = 0; i < columnsCount; i++) {
            for (int j = 0; j < rowsCount; j++) {
                if (fields[i][j] == 2) {
                    flags++;
                }
            }

        }
        return flags - getBombCount();
    }

    /**
     * returns true if every flag is on a bomb, else false
     *
     * @return if player won
     */
    public boolean didWin() {
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (getField(j, i) == 2 && !isBombOnPosition(j, i)) { // je vlajka ale není bomba
                    return false;
                }
                if (getField(j, i) != 2 && isBombOnPosition(j, i)) { // je bomba ale není vlajka
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * returns true if player revealed a bomb, else false
     *
     * @return if player lost
     */
    public boolean didLoose() {
        for (int i = 0; i < rowsCount; i++) {
            for (int j = 0; j < columnsCount; j++) {
                if (isBombOnPosition(j, i) && getField(j, i) == 1) { // našli jsme bombu
                    return true;
                }
            }
        }
        return false;
    }

    public int getRows() {
        return rowsCount;
    }

    public int getColumns() {
        return columnsCount;
    }

}
