package org.davepkxxx.games.elim.three;

import java.util.ArrayList;
import java.util.List;

/**
 * 主要的逻辑运算器
 *
 * @author David Dai
 */
public class ElimCalc {

    private int rows;

    private int cols;

    /**
     * 游戏地图
     * 0代表这个坐标是空的
     */
    private int[][] map;

    /**
     * 产生一个新的数字用于放置在地图上
     * 数字范围: 1, 2, 3
     * @return 新数字
     */
    private int newNum() {
        return (int) (Math.random() * 3) + 1;
    }

    /**
     * 根据行数和列数来初始化
     *
     * @param rows 行数
     * @param cols 列数
     */
    public ElimCalc(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.map = new int[rows][cols];
    }

    /**
     * 判断两个数字是否可以进行合并
     * 合并情况1: 如果两者只和为3
     * 合并情况2: 如果两者是3的倍数并且相等
     * @param a 数字a
     * @param b 数字b
     * @return 是否可以合并
     */
    private boolean canBeCombined(int a, int b) {
        return a + b == 3 || (a % 3 == 0 && a == b);
    }

    /**
     * 判断游戏是否已结
     * 判断标准：地上上没有空位，且不能进行合并
     *
     * @return 游戏是否已结束
     */
    public boolean isOver() {
        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                if (this.map[y][x] == 0
                        || (y + 1 < this.rows && canBeCombined(this.map[y][x], this.map[y + 1][x]))
                        || (x + 1 < this.cols && canBeCombined(this.map[y][x], this.map[y][x + 1])))
                    return false;
        return true;
    }

    /**
     * 根据坐标来获取地图上的数字
     *
     * @param y 横坐标
     * @param x 纵坐标
     * @return 坐标上的数字
     */
    public int getNum(int y, int x) {
        return this.map[y][x];
    }

    /**
     * 初始化整个地图的内容
     */
    public void initMap() {
        int startY = (int) (Math.random() * this.rows);
        int startX = (int) (Math.random() * this.cols);

        for (int y = 0; y < this.rows; y++)
            for (int x = 0; x < this.cols; x++)
                this.map[y][x] = startY == y && startX == x ? newNum() : 0;
    }

    /**
     * 移动地图里的格子
     * @param direction 移动方向
     */
    public void move(MoveDirection direction) {
        List<Integer> fit = new ArrayList<>(this.rows); // 存放适合增加新数字的行或列

        boolean isUpOrLeft = direction == MoveDirection.UP || direction == MoveDirection.LEFT;
        boolean isUpOrDown = direction == MoveDirection.UP || direction == MoveDirection.DOWN;

        int iMax = (isUpOrDown ? this.rows - 1 : this.cols - 1);
        int iStart = isUpOrLeft ? 0 : iMax;
        int iEnd = isUpOrLeft ? iMax : 0;
        int iStep = isUpOrLeft ? 1 : -1;

        IndexBounds bounds = (i, b) -> isUpOrLeft ? i <= b : i >= b;
        MapGetter getter = (i, j) -> isUpOrDown ? this.map[i][j] : this.map[j][i];
        MapSetter setter = (i, j, val) -> {
            if (isUpOrDown) this.map[i][j] = val;
            else this.map[j][i] = val;
        };

        for (int i = iStart; bounds.isOutOfBonds(i, iEnd); i += iStep) {
            for (int j = 0; j < 4; j++) {
                int current = getter.getNum(i, j);
                if (i == iEnd) { // 最后一条不参与合并，只看判断是否适合增加新的数字
                    if (current == 0) {
                        fit.add(j);
                    }
                } else {
                    int next = getter.getNum(i + iStep, j);
                    if (next > 0) { // 下一个格有东西才考虑是否和本格合并
                        if (current == 0) { // 本格没有东西就直接把下格的数字填充到本格
                            setter.setNum(i, j, next);
                            setter.setNum(i + iStep, j, 0);
                        } else if (canBeCombined(current, next)) { // 两个格子可以合并就进行累加
                            int val = current + next;
                            setter.setNum(i, j, val);
                            setter.setNum(i + iStep, j, 0);
                        }
                    }
                }
            }
        }

        int fitIdx = (int) (Math.random() * fit.size());
        setter.setNum(iEnd, fit.get(fitIdx), newNum());
    }

    /**
     * 移动的方向
     */
    public enum MoveDirection {
        UP, DOWN, LEFT, RIGHT
    }

    /**
     * 判断是否在边界内，根据移动方向来确定如何判断。
     */
    @FunctionalInterface
    private interface IndexBounds {

        /**
         * 判断坐标是否越界
         *
         * @param i 横坐标或纵坐标
         * @param b 横坐标或纵坐标的边界
         * @return 是否越界
         */
        public boolean isOutOfBonds(int i, int b);

    }

    /**
     * 从地图种获取某个坐标的数字，根据移动方向来确定如何获取。
     */
    @FunctionalInterface
    private interface MapGetter {

        /**
         * 从地图种获取某个位置的数字
         *
         * @param i 横坐标或纵坐标
         * @param j 横坐标或纵坐标
         * @return 坐标上的数字
         */
        public int getNum(int i, int j);

    }

    /**
     * 设置地图上某个坐标的数字，根据移动方向来确定如何获取。
     */
    @FunctionalInterface
    private interface MapSetter {

        /**
         * 设置地图上某个坐标的数字
         *
         * @param i   横坐标或纵坐标
         * @param j   横坐标或纵坐标
         * @param val 坐标上的新数字
         */
        public void setNum(int i, int j, int val);

    }

}
