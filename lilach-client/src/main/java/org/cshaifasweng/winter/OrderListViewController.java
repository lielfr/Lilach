package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Order;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Date;
import java.util.List;

public class OrderListViewController implements Refreshable {

    @FXML
    private Button backToCatalogButton;

    @FXML
    private TableView<Order> tableView1;

    private List<Order> orders;

    private void populateTable() {
        if (orders == null) return;

        TableColumn<Order, Long> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));

        TableColumn<Order, Date> createdDateColumn = new TableColumn<>("Created in");
        createdDateColumn.setCellValueFactory(new PropertyValueFactory<>("orderDate"));
        createdDateColumn.setCellFactory(new Utils<Order>().dateFactory);

        TableColumn<Order, Date> deliveryDateColumn = new TableColumn<>("Supply date");
        deliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("supplyDate"));
        deliveryDateColumn.setCellFactory(new Utils<Order>().dateFactory);

        TableColumn<Order, Double> priceColumn = new TableColumn<>("Total price");
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));


        tableView1.getColumns().clear();
        tableView1.getColumns().addAll(
                idColumn,
                createdDateColumn,
                deliveryDateColumn,
                priceColumn
        );
        tableView1.setItems(FXCollections.observableList(orders));

        ContextMenu menu = new ContextMenu();
        MenuItem detailsMenuItem = new MenuItem("Show order");
        detailsMenuItem.setOnAction((event) -> {

        });
        menu.getItems().add(detailsMenuItem);

        Utils.addContextMenu(tableView1, menu);
    }

    @FXML
    void back(MouseEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
    }

    @FXML
    void updateTable(MouseEvent event) {
        populateTable();
    }

    @FXML
    void updateTableButton(DragEvent event) {
        populateTable();
    }

    @Override
    public void refresh() {
        LilachService service = APIAccess.getService();
        Customer customer = (Customer) APIAccess.getCurrentUser();
        service.getOrdersByCustomer(customer.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Order>> call, Response<List<Order>> response) {
                if (response.code() != 200) return;

                orders = response.body();

                Platform.runLater(() -> {
                    populateTable();
                });

            }

            @Override
            public void onFailure(Call<List<Order>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onSwitch() {

    }
}
