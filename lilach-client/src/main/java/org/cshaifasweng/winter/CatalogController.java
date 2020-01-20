package org.cshaifasweng.winter;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import org.controlsfx.control.GridCell;
import org.controlsfx.control.GridView;
import org.controlsfx.control.cell.ColorGridCell;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.ResourceBundle;

public class CatalogController implements Initializable {


    @FXML
    private AnchorPane rootPane;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Random random = new Random(System.currentTimeMillis());
        List<Color> colors = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            colors.add(new Color(random.nextDouble(),
                    random.nextDouble(), random.nextDouble(), 1.0));
        }

        GridView<Color> gridView = new GridView<>(FXCollections.observableArrayList(colors));
        gridView.setCellFactory(colorGridView -> new ColorGridCell());

        rootPane.getChildren().add(gridView);
    }
}
