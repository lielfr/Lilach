package org.cshaifasweng.winter;

import javafx.application.Platform;
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

    private int page = 1;
    private int pages = 1;

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
    void backPage() {
        if (page == 1) return;
        page--;
        updatePages();
    }

    @FXML
    void nextPage() {
        if (page == pages) return;
        page++;
        updatePages();
    }

    @FXML
    void pageNumChanged() {
        int pageNumVal = Integer.parseInt(pageNum.getText());
        if (pageNumVal > 1 && pageNumVal <= pages) {
            page = pageNumVal;
            updatePages();
        } else {
            pageNum.setText(String.valueOf(page));
        }
    }

    @Subscribe
    public void handleLogin(LoginChangeEvent changeEvent) {
        updateStoresList();
    }

    private void updateStoresList() {
        if (APIAccess.getCurrentUser() == null || APIAccess.getCurrentUser() instanceof Employee) {
            storeChoiceBox.getItems().clear();
            service.getAllStores().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                    if (response.code() != 200) return;
                    stores = response.body();
                    if (stores == null) return;
                    List<String> storeNames = stores
                            .stream()
                            .map(Store::getName)
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
            Customer customer = (Customer) APIAccess.getCurrentUser();
            System.out.println("Got stores: " + customer.getStores());
            if (customer.getStores() == null) return;
            storeChoiceBox.getItems().clear();
            storeChoiceBox.getItems().addAll(
                    customer
                            .getStores()
                            .stream()
                            .map(Store::getName).collect(Collectors.toList()));
        }
    }

    private void populateGrid() throws IOException {
        catalogGrid.getChildren().clear();
        for (int i = 0; i < NUM_ROWS; i++) {
            for (int j = 0; j < NUM_COLS; j++) {
                Node itemCell = LayoutManager.getInstance().getFXML("catalog_item");
                CatalogItemViewController controller =
                        (CatalogItemViewController) LayoutManager.getInstance().getController("catalog_item");
                int index = (page - 1) + i * NUM_COLS + j;
                if (index >= items.size())
                    break;
                CatalogItem item = items.get(index);
                controller.setItemLabel(item.getDescription());
                catalogGrid.add(itemCell, i, j);
            }
        }
        updatePages();
    }

    private void updatePages() {
        pageNum.setText(String.valueOf(page));
        pages = (int) Math.ceil((double)items.size() / ((double)NUM_ROWS * (double)NUM_COLS));
        totalPages.setText(String.valueOf(pages));

        backButton.setDisable(page == 1);
        forwardButton.setDisable(page == pages);
    }

    private void getCatalog(long id) {
        service.getCatalogByStore(id).enqueue(new Callback<>() {
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
