package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class AddItemController implements Refreshable {

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField discountSumField;

    @FXML
    private TextField discountPrecnetageField;

    @FXML
    private Button addImgaeButton;

    @FXML
    private ImageView imageViewer;

    @FXML
    private Button cancelButton;

    @FXML
    private Button addItemButton;

    @FXML
    private TextField itemPriceField;

    @FXML
    private ChoiceBox<?> dominantColorChoiceBox;

    @FXML
    private ChoiceBox<?> typeChiceBox;

    @FXML
    void addImage(ActionEvent event) {

    }

    @FXML
    void addItem(ActionEvent event) {

    }

    @FXML
    void cancelItemAddition(ActionEvent event) {


    }

    @Override
    public void refresh() {
        imageViewer.setImage(new Image(String.valueOf(LayoutManager.class.getResource("no-image-icon-13.png"))));

    }
}
