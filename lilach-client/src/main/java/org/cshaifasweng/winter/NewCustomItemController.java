package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
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

public class NewCustomItemController implements Refreshable {

    @FXML
    private ListView<CatalogItem> itemsListView;

    private CustomItem createdItem = new CustomItem();

    private LilachService service = APIAccess.getService();

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
        if (event.isAdd())
            createdItem.getItems().add(event.getItem());
        else
            createdItem.getItems().remove(event.getItem());
    }

    private void getAvailableItems() {
        if (store == null) return;

        service.getCatalogByStore(store.getId(), true).enqueue(new Callback<List<CatalogItem>>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200 || response.body() == null)
                    return;

                Platform.runLater(() -> {
                    itemsListView.setItems(FXCollections.observableList(response.body()));
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
