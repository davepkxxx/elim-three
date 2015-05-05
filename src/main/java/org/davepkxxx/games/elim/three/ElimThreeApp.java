package org.davepkxxx.games.elim.three;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * 游戏主体程序
 * @author David Dai
 */
public class ElimThreeApp extends Application {
    
    private ElimCalc calc;

    private Parent map;
    
    private int mapRows;
    
    private int mapCols;

    /**
     * 根据地图上的坐标获取格子
     * @param y 横坐标
     * @param x 纵坐标
     * @return 格子
     */
    private Label getNumBlock(int y, int x) {
        VBox vbox = (VBox) this.map;
        HBox hbox = (HBox) vbox.getChildren().get(y);
        return (Label) hbox.getChildren().get(x);
    }

    /**
     * 初始化游戏参数
     */
    public void init() {
        this.mapRows = 4;
        this.mapCols = 4;
        this.calc = new ElimCalc(this.mapRows, this.mapCols);
    }

    /**
     * 初始化地图
     * @return 地图组件
     */
    public VBox createMap() {
        VBox vbox = new VBox();
        vbox.setSpacing(4);

        for (int row = 0; row < this.mapRows; row++) {
            HBox hbox = new HBox();
            hbox.setSpacing(4);
            vbox.getChildren().add(hbox);

            for (int col = 0; col < this.mapCols; col++) {
                Label lbl = new Label();
                lbl.setAlignment(Pos.CENTER);
                lbl.setTextFill(Color.WHITE);
                lbl.setMinSize(64, 64);
                lbl.setMaxSize(64, 64);
                hbox.getChildren().add(lbl);
            }
        }

        return vbox;
    }

    /**
     * 绘制地图上的格子里的具体内容
     */
    public void drawMap() {
        for (int row = 0; row < this.mapRows; row++) {
            for (int col = 0; col < this.mapCols; col++) {
                Label lbl = getNumBlock(row, col);
                int num = this.calc.getNum(row, col);

                if (num == 0) { // 格子是空的就去掉文字和背景，否则根据数字声称内容和背景。
                    lbl.setText("");
                    lbl.setBackground(null);
                } else {
                    lbl.setText(String.valueOf(num));
                    Color color = num == 1 ? Color.RED : num == 2 ? Color.BLUE : Color.GREEN;
                    lbl.setBackground(new Background(new BackgroundFill(color, CornerRadii.EMPTY, Insets.EMPTY)));
                }
            }
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        init();

        this.map = createMap();
        primaryStage.setScene(new Scene(this.map));

        primaryStage.getScene().setOnKeyPressed((event) -> {
            if (KeyCode.N.equals(event.getCode())) { //按N开始新游戏
                this.calc.initMap();
                drawMap();
            } else if (!this.calc.isOver()) { // 在游戏没有结束时可以做的事情
                if (KeyCode.UP.equals(event.getCode())) { // 按UP可以向上移动
                    this.calc.move(ElimCalc.MoveDirection.UP);
                    drawMap();
                } else if (KeyCode.DOWN.equals(event.getCode())) { // 按DOWN可以向下移动
                    this.calc.move(ElimCalc.MoveDirection.DOWN);
                    drawMap();
                } else if (KeyCode.LEFT.equals(event.getCode())) { // 按LEFT可以向左移动
                    this.calc.move(ElimCalc.MoveDirection.LEFT);
                    drawMap();
                } else if (KeyCode.RIGHT.equals(event.getCode())) { // 按RIGHT可以向右移动
                    this.calc.move(ElimCalc.MoveDirection.RIGHT);
                    drawMap();
                }
            }
        });

        this.calc.initMap();
        drawMap();

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
