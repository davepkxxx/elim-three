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
 * Created by daviddai on 15/5/3.
 * @author David Dai
 */
public class ElimThreeApp extends Application {
    
    private ElimCalc calc;

    private Parent map;
    
    private int mapRows;
    
    private int mapCols;

    private Label getNumBlock(int y, int x) {
        VBox vbox = (VBox) this.map;
        HBox hbox = (HBox) vbox.getChildren().get(y);
        return (Label) hbox.getChildren().get(x);
    }

    public void init() {
        this.mapRows = 4;
        this.mapCols = 4;
        this.calc = new ElimCalc(1, this.mapRows, this.mapCols);
    }

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

    public void drawMap() {
        for (int row = 0; row < this.mapRows; row++) {
            for (int col = 0; col < this.mapCols; col++) {
                Label lbl = getNumBlock(row, col);
                int num = this.calc.getNum(row, col);

                if (num == 0) {
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
            if (KeyCode.N.equals(event.getCode())) {
                this.calc.initMap();
                drawMap();
            } else if (!this.calc.isOver()) {
                if (KeyCode.UP.equals(event.getCode())) {
                    this.calc.move(ElimCalc.MoveDirection.UP);
                    drawMap();
                } else if (KeyCode.DOWN.equals(event.getCode())) {
                    this.calc.move(ElimCalc.MoveDirection.DOWN);
                    drawMap();
                } else if (KeyCode.LEFT.equals(event.getCode())) {
                    this.calc.move(ElimCalc.MoveDirection.LEFT);
                    drawMap();
                } else if (KeyCode.RIGHT.equals(event.getCode())) {
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
