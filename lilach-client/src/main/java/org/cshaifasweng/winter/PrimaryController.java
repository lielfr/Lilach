package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.SendEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class PrimaryController implements Refreshable {

    @FXML
    private TableView<CatalogItem> dataTable;

    public static CatalogItem selectedItem;

    @Override
    public void refresh() {
        dataTable.getItems().clear();
        dataTable.getColumns().clear();
        LilachService service = APIAccess.getService();
        service.getAllItems().enqueue(new Callback<List<CatalogItem>>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200) return;

                List<CatalogItem> items = response.body();

                // All the UI updating should be done in the UI thread. Here we enforce that.
                Platform.runLater(() -> {
                    TableColumn<CatalogItem, Long> idColumn = new TableColumn<>("Catalog Number");
                    TableColumn pictureColumn = new TableColumn("Picture");
                    TableColumn<CatalogItem, String> descriptionColumn = new TableColumn<CatalogItem, String>("Description");
                    TableColumn<CatalogItem, String> dominantColorColumn = new TableColumn<>("Dominant Color");
                    TableColumn<CatalogItem, Double> priceColumn = new TableColumn<>("Price");


                    idColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, Long>("id"));
                    pictureColumn.setCellFactory(tableColumn -> {
                        final ImageView imageView = new ImageView();

                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);

                        TableCell<CatalogItem, byte[]> tableCell = new TableCell<>() {
                            @Override
                            protected void updateItem(byte[] image, boolean b) {
                                if (image != null) {
                                    imageView.setImage(new Image(new ByteArrayInputStream(image)));
                                }
                            }
                        };
                        tableCell.setGraphic(imageView);
                        return tableCell;
                    });
                    pictureColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, byte[]>("picture"));
                    descriptionColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, String>("description"));
                    dominantColorColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, String>("dominantColor"));
                    priceColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, Double>("price"));

                    dataTable.getColumns().addAll(idColumn, descriptionColumn,
                            dominantColorColumn, priceColumn, pictureColumn);

                    dataTable.setItems(FXCollections.observableList(items));

                    ContextMenu menu = new ContextMenu();
                    MenuItem item = new MenuItem("Change price");

                    menu.getItems().add(item);

                    menu.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
                            selectedItem = items.get(selectedIndex);
                            EventBus.getDefault().post(new DashboardSwitchEvent("secondary"));
                            EventBus.getDefault().post(new SendEvent(selectedItem));
                        }
                    });

                    dataTable.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                                menu.show(dataTable, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                            } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                                menu.hide();
                            }
                        }
                    });
                });


            }

            @Override
            public void onFailure(Call<List<CatalogItem>> call, Throwable t) {
                Platform.runLater(() -> {
                    Alert alert = new Alert(Alert.AlertType.ERROR, "Could not connect to server! ", ButtonType.CLOSE);
                    alert.showAndWait();
                    try {
                        App.setRoot("connection");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            }
        });
    }
}
