package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.util.Pair;
import org.cshaifasweng.winter.events.CustomItemAddButtonEvent;
import org.cshaifasweng.winter.events.CustomItemBeginEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.CustomItem;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.ui.CustomItemCell;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class NewCustomItemController implements Refreshable {

    @FXML
    private ListView<Pair<CatalogItem, Long>> itemsListView;

    private CustomItem createdItem = new CustomItem();

    private LilachService service = APIAccess.getService();

    private List<CatalogItem> items;

    Store store;

    @FXML
    void addItem(ActionEvent event) {

    }

    @FXML
    void cancel(ActionEvent event) {

    }

    @Subscribe
    public void storeSet(CustomItemBeginEvent event) {
        store = event.getStore();
        getAvailableItems();
    }

    @Subscribe
    public void onAddItem(CustomItemAddButtonEvent event) {
        CatalogItem item = event.getItem();
        Map<Long, Long> quantities = createdItem.getQuantities();
        if (event.isAdd()) {
            quantities.put(item.getId(), quantities.get(item.getId()) + 1);
        } else {
            if (!quantities.containsKey(item.getId())) return;
            long currentQuantity = quantities.get(item.getId());
            if (currentQuantity > 0)
                quantities.put(item.getId(), currentQuantity - 1);
        }
        updateTable();
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
    }
}
