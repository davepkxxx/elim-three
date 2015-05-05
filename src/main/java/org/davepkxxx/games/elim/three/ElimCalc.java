package org.davepkxxx.games.elim.three;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by daviddai on 15/5/4.
 */
public class ElimCalc {

    private int start;

    private int rows;

    private int cols;

    private int[][] map;

    public ElimCalc(int start, int rows, int cols) {
        this.start = start;
        this.rows = rows;
        this.cols = cols;
        this.map = new int[rows][cols];
    }

    public boolean isOver() {
        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                if (this.map[y][x] == 0)
                    return false;
        return true;
    }

    public int getNum(int y, int x) {
        return this.map[y][x];
    }

    public void initMap() {
        int startY = (int) (Math.random() * this.rows);
        int startX = (int) (Math.random() * this.cols);

        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.map[y][x] = startY == y && startX == x ? start : 0;
    }

    public void move(MoveDirection direction) {
        List<Integer> fit = new ArrayList<>(this.rows);

        boolean isUpOrLeft = direction == MoveDirection.UP || direction == MoveDirection.LEFT;
        boolean isUpOrDown = direction == MoveDirection.UP || direction == MoveDirection.DOWN;

        int iMax = (isUpOrDown ? this.rows - 1 : this.cols - 1);
        int iStart = isUpOrLeft ? 0 : iMax;
        int iEnd = isUpOrLeft ? iMax : 0;
        int iStep = isUpOrLeft ? 1 : -1;

        IndexBounds bounds = (i, b) -> isUpOrLeft ? i <= b : i >= b;
        MapGetter getter = (i, j) -> isUpOrDown ? this.map[i][j] : this.map[j][i];
        MapSetter setter = (i, j, val) -> { if (isUpOrDown) this.map[i][j] = val; else this.map[j][i] = val; };

        for (int i = iStart; bounds.isOutOfBonds(i, iEnd); i += iStep) {
            for (int j = 0; j < 4; j++) {
                int current = getter.getNum(i, j);
                if (i == iEnd) {
                    if (current == 0) {
                        fit.add(j);
                    }
                } else {
                    int next = getter.getNum(i + iStep, j);
                    if (next > 0) {
                        if (current == 0) {
                            setter.setNum(i, j, next);
                            setter.setNum(i + iStep, j, 0);
                        } else if (current + next == 3 || (current % 3 == 0 && current == next)) {
                            int val = current + next;
                            setter.setNum(i, j, val);
                            setter.setNum(i + iStep, j, 0);
                        }
                    }
                }
            }
        }

        int fitIdx = (int) (Math.random() * fit.size());
        int newNum = (int) (Math.random() * 3) + 1;
        setter.setNum(iEnd, fit.get(fitIdx), newNum);
    }

    public enum MoveDirection {

        UP, DOWN, LEFT, RIGHT

    }

    @FunctionalInterface
    private interface IndexBounds {

        public boolean isOutOfBonds(int i, int j);

    }

    @FunctionalInterface
    private interface MapGetter {

        public int getNum(int i, int j);

    }

    @FunctionalInterface
    private interface MapSetter {

        public void setNum(int i, int j, int val);

    }

}
