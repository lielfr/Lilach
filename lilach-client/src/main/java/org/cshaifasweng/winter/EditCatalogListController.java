package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventTarget;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.StringConverter;
import org.cshaifasweng.winter.events.CatalogItemEditEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;

public class EditCatalogListController implements Refreshable {

    @FXML
    private TableView<CatalogItem> dataTable;

    @FXML
    private ChoiceBox<Store> storeChoiceBox;

    public static CatalogItem selectedItem;

    private LilachService service;

    private void populateTable(long storeId) {
        dataTable.getColumns().clear();
        service.getCatalogByStore(storeId).enqueue(new Callback<List<CatalogItem>>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200) return;

                List<CatalogItem> items = response.body();

                // All the UI updating should be done in the UI thread. Here we enforce that.
                Platform.runLater(() -> {
                    TableColumn<CatalogItem, Long> idColumn = new TableColumn<>("Catalog Number");
                    TableColumn<CatalogItem, String> pictureColumn = new TableColumn<>("Picture");
                    TableColumn<CatalogItem, String> descriptionColumn = new TableColumn<CatalogItem, String>("Description");
                    TableColumn<CatalogItem, Double> priceColumn = new TableColumn<>("Price");


                    idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
                    pictureColumn.setCellFactory(tableColumn -> {
                        final ImageView imageView = new ImageView();

                        imageView.setFitHeight(50);
                        imageView.setFitWidth(50);

                        TableCell<CatalogItem, String> tableCell = new TableCell<>() {
                            @Override
                            protected void updateItem(String image, boolean b) {
                                if (image != null) {
                                    imageView.setImage(new Image(APIAccess.getImageUrl(image)));
                                }
                            }
                        };
                        tableCell.setGraphic(imageView);
                        return tableCell;
                    });
                    pictureColumn.setCellValueFactory(new PropertyValueFactory<>("picture"));
                    descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
                    priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

                    dataTable.getColumns().addAll(idColumn, descriptionColumn,
                            priceColumn, pictureColumn);

                    dataTable.setItems(FXCollections.observableList(items));

                    ContextMenu menu = new ContextMenu();
                    MenuItem item = new MenuItem("Edit item");
                    MenuItem item2 = new MenuItem("Delete item");

                    menu.getItems().addAll(item, item2);

                    menu.setOnAction(new EventHandler<ActionEvent>() {
                        @Override
                        public void handle(ActionEvent actionEvent) {
                            EventTarget target = actionEvent.getTarget();
                            int selectedIndex = dataTable.getSelectionModel().getSelectedIndex();
                            selectedItem = items.get(selectedIndex);
                            if (item.equals(target)) {
                                EventBus.getDefault().post(new DashboardSwitchEvent("add_item"));
                                EventBus.getDefault().post(new CatalogItemEditEvent(selectedItem));
                            } else if (item2.equals(target)) {
                                service.deleteItem(selectedItem.getId()).enqueue(new Callback<Void>() {
                                    @Override
                                    public void onResponse(Call<Void> call, Response<Void> response) {
                                        Platform.runLater(() -> {
                                            populateTable(storeChoiceBox.getValue().getId());
                                        });

                                    }

                                    @Override
                                    public void onFailure(Call<Void> call, Throwable throwable) {

                                    }
                                });
                            }

                        }
                    });

                    Utils.addContextMenu(dataTable, menu);
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

    @Override
    public void refresh() {
        dataTable.getItems().clear();
        dataTable.getColumns().clear();
        service = APIAccess.getService();
        storeChoiceBox.setConverter(new StringConverter<Store>() {
            @Override
            public String toString(Store store) {
                return store.getName();
            }

            @Override
            public Store fromString(String s) {
                return null;
            }
        });

        storeChoiceBox.getSelectionModel().selectedItemProperty()
                .addListener((observableValue, store, t1) -> populateTable(storeChoiceBox.getValue().getId()));

        Employee employee = (Employee) APIAccess.getCurrentUser();
        if (employee.getAssignedStore() != null) {
            storeChoiceBox.setItems(FXCollections.observableArrayList(employee.getAssignedStore()));
            storeChoiceBox.getSelectionModel().select(0);
        } else {
            service.getAllStores().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                    if (response.code() != 200) return;

                    Platform.runLater(() -> {
                        storeChoiceBox.setItems(FXCollections.observableArrayList(response.body()));
                        storeChoiceBox.getSelectionModel().select(0);
                    });
                }

                @Override
                public void onFailure(Call<List<Store>> call, Throwable throwable) {

                }
            });
        }


    }

    @Override
    public void onSwitch() {

    }

    @FXML
    public void addItem() {
        EventBus.getDefault().post(new DashboardSwitchEvent("add_item"));
    }
}
