package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class PrimaryController {

    @FXML
    private TableView dataTable;

    public static CatalogItem selectedItem;


    public void initialize() {
        LilachService service = APIAccess.getService();
        service.getAllItems().enqueue(new Callback<List<CatalogItem>>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200) return;

                List<CatalogItem> items = response.body();

                // All the UI updating should be done in the UI thread. Here we enforce that.
                Platform.runLater(() -> {
                    TableColumn idColumn = new TableColumn("Catalog Number");
//                TableColumn pictureColumn = new TableColumn("Picture");
                    TableColumn descriptionColumn = new TableColumn("Description");
                    TableColumn dominantColorColumn = new TableColumn("Dominant Color");
                    TableColumn priceColumn = new TableColumn("Price");

                    dataTable.getColumns().addAll(idColumn, descriptionColumn,
                            dominantColorColumn, priceColumn);

                    idColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, Long>("id"));
//                pictureColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, byte[]>("picture"));
                    descriptionColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, String>("description"));
                    dominantColorColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, String>("dominantColor"));
                    priceColumn.setCellValueFactory(new PropertyValueFactory<CatalogItem, Double>("price"));


                    dataTable.setItems(FXCollections.observableList(items));

                    ContextMenu menu = new ContextMenu();
                    MenuItem item = new MenuItem("Change price");

                    menu.getItems().add(item);

                    menu.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
                            selectedItem = items.get(selectedIndex);
                            try {
                                App.setRoot("secondary");
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
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

            }
        });
    }
}
