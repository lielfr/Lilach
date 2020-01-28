package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TableView;

public class UserDisplayController {

    @FXML
    private ChoiceBox<?> storeSelect;

    @FXML
    private ChoiceBox<?> typeSelect;

    @FXML
    private Button filterButton;

    @FXML
    private TableView<?> userTable;

    @FXML
    private Button backToCatalogButton;

    @FXML
    private Button refreshButton;

    @FXML
    void backToCatalog(ActionEvent event) {

    }

    @FXML
    void filter(ActionEvent event) {

    }

    @FXML
    void refreshPage(ActionEvent event) {

    }

}
