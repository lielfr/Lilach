package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import org.cshaifasweng.winter.events.LoginChangeEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CatalogController implements Refreshable {

    private LilachService service;

    private List<Store> stores;

    private List<CatalogItem> items;

    private static final int NUM_COLS = 3;
    private static final int NUM_ROWS = 3;

    @FXML
    private ChoiceBox<String> storeChoiceBox;

    @FXML
    private GridPane catalogGrid;

    @FXML
    private Button backButton;

    @FXML
    private TextField pageNum;

    @FXML
    private Button forwardButton;

    @FXML
    private Label totalPages;

    @FXML
    void backPage(ActionEvent event) {

    }

    @FXML
    void nextPage(ActionEvent event) {

    }

    @FXML
    void pageNumChanged(ActionEvent event) {

    }

    @Subscribe
    public void handleLogin(LoginChangeEvent changeEvent) {

    }

    private void updateStoresList() {
        if (APIAccess.getCurrentUser() == null || APIAccess.getCurrentUser() instanceof Employee) {
            storeChoiceBox.getItems().clear();
            service.getAllStores().enqueue(new Callback<List<Store>>() {
                @Override
                public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                    if (response.code() != 200) return;
                    stores = response.body();
                    List<String> storeNames = stores
                            .stream()
                            .map((store) -> store.getName())
                            .collect(Collectors.toList());

                    Platform.runLater(() -> {
                        storeChoiceBox.getItems().addAll(storeNames);
                        storeChoiceBox.setValue(storeNames.get(0));
                    });

                }

                @Override
                public void onFailure(Call<List<Store>> call, Throwable throwable) {

                }
            });
        } else {
            storeChoiceBox.getItems().clear();
            storeChoiceBox.getItems().addAll(
                    ((Customer) APIAccess.getCurrentUser())
                            .getStores()
                            .stream()
                            .map((store) -> store.getName()).collect(Collectors.toList()));
        }
    }

    private void populateGrid() throws IOException {
        catalogGrid.getChildren().clear();
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Node itemCell = LayoutManager.getInstance().getFXML("catalog_item");
                CatalogItemViewController controller =
                        (CatalogItemViewController) LayoutManager.getInstance().getController("catalog_item");
                controller.setItemLabel(items.get(i * NUM_COLS + j).getDescription());
                catalogGrid.add(itemCell, i, j);
            }
        }
    }

    private void getCatalog(long id) {
        service.getCatalogByStore(id).enqueue(new Callback<List<CatalogItem>>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200) return;

                items = response.body();

                Platform.runLater(() -> {
                    try {
                        populateGrid();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
        service = APIAccess.getService();
        updateStoresList();
        storeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> getCatalog(stores.get
                        (storeChoiceBox.getSelectionModel().getSelectedIndex()).getId()));
    }
}
