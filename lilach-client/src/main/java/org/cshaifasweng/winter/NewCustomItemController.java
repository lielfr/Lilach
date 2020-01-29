package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Pair;
import javafx.util.StringConverter;
import org.cshaifasweng.winter.events.CustomItemAddButtonEvent;
import org.cshaifasweng.winter.events.CustomItemBeginEvent;
import org.cshaifasweng.winter.events.CustomItemFinishEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.ui.CustomItemCell;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewCustomItemController implements Refreshable {

    @FXML
    private ListView<Pair<CatalogItem, Long>> itemsListView;

    @FXML
    private Label estimatedCost;

    @FXML
    private TextField lowerPriceBound;

    @FXML
    private TextField upperPriceBound;

    @FXML
    private ColorPicker dominantColor;

    @FXML
    private ChoiceBox<CustomItemType> typeChoiceBox;

    private CustomItem createdItem = new CustomItem();

    private LilachService service = APIAccess.getService();

    private List<CatalogItem> items;

    private List<Item> cart;

    private Map<Long, Long> quantities;

    Store store;

    private void updateCost() {
        double totalCost = createdItem.getItems().stream()
                .map(item -> item.getPrice() * createdItem.getQuantities().get(item.getId()))
                .reduce(Double::sum)
                .orElse(0.0);
        createdItem.setPrice(totalCost);
        estimatedCost.setText(String.valueOf((totalCost)));
    }

    @FXML
    void addItem(ActionEvent event) {
        createdItem.setItems(createdItem.getItems()
                .stream()
                .filter(item -> createdItem.getQuantities().get(item.getId()) > 0)
                .collect(Collectors.toList()));
        updateCost();

        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));

        service.newCustomItem(createdItem).enqueue(new Callback<CustomItem>() {
            @Override
            public void onResponse(Call<CustomItem> call, Response<CustomItem> response) {
                if (response.code() != 200) {
                    Utils.showError("Error code: " + response.code());
                }
                if (response.code() == 200) {
                    createdItem = response.body();
                    System.out.println("New custom item, id: " + createdItem.getId());
                    Platform.runLater(() -> {
                        EventBus.getDefault().post(new CustomItemFinishEvent(createdItem, cart, quantities));
                    });
                }

            }

            @Override
            public void onFailure(Call<CustomItem> call, Throwable throwable) {
                Utils.showError("Network failure");
            }
        });

    }

    @FXML
    void cancel(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("catalog"));
    }

    @FXML
    void priceRangeSet() {
        String onlyNumbers = "^[0-9]+$";
        if (!lowerPriceBound.getText().matches(onlyNumbers))
            lowerPriceBound.setText("10");
        if (!upperPriceBound.getText().matches(onlyNumbers))
            upperPriceBound.setText("1000");
        createdItem.setLowerPriceBound(Double.parseDouble(lowerPriceBound.getText()));
        createdItem.setUpperPriceBound(Double.parseDouble(upperPriceBound.getText()));
    }

    @Subscribe
    public void storeSet(CustomItemBeginEvent event) {
        store = event.getStore();
        cart = event.getCurrentCart();
        quantities = event.getQuantities();
        getAvailableItems();
    }

    @Subscribe
    public void onAddItem(CustomItemAddButtonEvent event) {
        CatalogItem item = event.getItem();
        Map<Long, Long> quantities = createdItem.getQuantities();
        if (event.isAdd()) {
            quantities.put(item.getId(), quantities.get(item.getId()) + 1);
            if (!createdItem.getItems().contains(item))
                createdItem.getItems().add(item);
        } else {
            if (!quantities.containsKey(item.getId())) return;
            long currentQuantity = quantities.get(item.getId());
            if (currentQuantity > 0)
                quantities.put(item.getId(), currentQuantity - 1);
        }
        updateTable();
        updateCost();
    }

    private void updateTable() {
        itemsListView.setItems(FXCollections.observableList(
                items.stream()
                        .map(
                                item -> new Pair<CatalogItem, Long>
                                        (item, createdItem.getQuantities().get(item.getId())))
                        .collect(Collectors.toList())));
    }

    private void getAvailableItems() {
        if (store == null) return;

        service.getCatalogByStore(store.getId(), true).enqueue(new Callback<List<CatalogItem>>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200 || response.body() == null)
                    return;

                items = response.body();
                items.forEach((item) -> createdItem.getQuantities().put(item.getId(), 0L));

                Platform.runLater(() -> {
                    updateTable();
                });

            }

            @Override
            public void onFailure(Call<List<CatalogItem>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void refresh() {
        EventBus.getDefault().register(this);
        itemsListView.setCellFactory(param -> new CustomItemCell());
        getAvailableItems();
        typeChoiceBox.setConverter(new StringConverter<CustomItemType>() {
            @Override
            public String toString(CustomItemType customItemType) {
                return customItemType.name();
            }

            @Override
            public CustomItemType fromString(String s) {
                return null;
            }
        });
        typeChoiceBox.setItems(FXCollections.observableList(Arrays.asList(CustomItemType.values())));
        typeChoiceBox.getSelectionModel().selectedItemProperty().addListener(
                (observableValue, customItemType, t1) -> createdItem.setType(typeChoiceBox.getValue()));
        dominantColor.setOnAction(actionEvent -> createdItem.setDominantColor(dominantColor.getValue().toString()));
    }

    @Override
    public void onSwitch() {
        EventBus.getDefault().unregister(this);
    }
}
