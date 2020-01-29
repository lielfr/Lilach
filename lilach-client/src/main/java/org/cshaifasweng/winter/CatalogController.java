package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import org.cshaifasweng.winter.events.*;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class CatalogController implements Refreshable, Initializable {

    private LilachService service;

    private List<Store> stores = Collections.synchronizedList(new ArrayList<>());

    private List<CatalogItem> items;



    private List<Item> cart;
    private Map<Long, Long> quantities;

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
    private Button cartButton;

    @FXML
    private Button customItemButton;

    @FXML
    private TextField searchField;

    @Subscribe
    public void handleBackToCatalog(BackToCatalogEvent event) {
        cart.addAll(event.getCart());
        quantities.putAll(event.getQuantities());
        updateCartButton();
    }

    @FXML
    public void goToCart() {
        EventBus.getDefault().post(new DashboardSwitchEvent("order"));
        EventBus.getDefault().post(new OrderCreateEvent(cart, quantities));
    }

    @FXML
    public void backPage() throws IOException {
        if (page == 1) return;
        page--;
        populateGrid();
    }

    @FXML
    public void nextPage() throws IOException {
        if (page == pages) return;
        page++;
        populateGrid();
    }

    @FXML
    public void pageNumChanged() {
        int pageNumVal = Integer.parseInt(pageNum.getText());
        if (pageNumVal > 1 && pageNumVal <= pages) {
            page = pageNumVal;
            updatePages();
        } else {
            pageNum.setText(String.valueOf(page));
        }
    }

    private void updateBuyButtons() {
        boolean isLoggedIn = APIAccess.getCurrentUser() != null;
        customItemButton.setVisible(isLoggedIn);
        cartButton.setVisible(isLoggedIn);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void handleLogin(LoginChangeEvent changeEvent) {
        User user = APIAccess.getCurrentUser();
        if (user == null || user instanceof Employee)
            return;
        Customer customer = (Customer) APIAccess.getCurrentUser();
        updateBuyButtons();
        service.getStoresByCustomer(customer.getId()).enqueue(new Callback<List<Store>>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                // The following two lines are CRITICAL. These were the reason for that annoying catalog bug.
                List<Store> responseStores = response.body();
                responseStores.forEach((store -> System.out.println("STORE: " + store.getName())));
                if (responseStores == null) return;

                Platform.runLater(() -> {
                    stores.clear();
                    stores.addAll(responseStores);
                    storeChoiceBox.getItems().clear();
                    storeChoiceBox.getItems().addAll(
                            responseStores
                                    .stream()
                                    .map(Store::getName).collect(Collectors.toList()));
                    storeChoiceBox.getSelectionModel().select(0);
                });

            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable throwable) {
                Utils.showError("Network failure");
            }
        });

    }

    private void updateStoresList() {
        if (APIAccess.getCurrentUser() == null || APIAccess.getCurrentUser() instanceof Employee) {
            storeChoiceBox.getItems().clear();
            service.getAllStores().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                    if (response.code() != 200 || response.body() == null) return;
                    if (stores == null) return;
                    stores.clear();
                    stores.addAll(response.body());

                    List<String> storeNames = stores
                            .stream()
                            .map(Store::getName)
                            .collect(Collectors.toList());

                    Platform.runLater(() -> {
                        storeChoiceBox.getItems().addAll(storeNames);
                        storeChoiceBox.setValue(storeNames.get(0));
                        // We have to do that because we may override the user's stores here
                        handleLogin(null);
                    });

                }

                @Override
                public void onFailure(Call<List<Store>> call, Throwable throwable) {
                    Utils.showError("Network failure");
                }
            });
        } else {
            handleLogin(null);
        }
    }

    private void populateGrid() throws IOException {
        Platform.runLater(() -> {
            catalogGrid.getChildren().clear();
            for (int i = 0; i < NUM_ROWS; i++) {
                for (int j = 0; j < NUM_COLS; j++) {
                    Pair<Parent, Object> viewData = null;
                    try {
                        viewData = LayoutManager.getInstance().getFXML("catalog_item");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Node itemCell = viewData.getKey();
                    CatalogItemViewController controller =
                            (CatalogItemViewController) viewData.getValue();
                    int index = (page - 1) * NUM_COLS * NUM_ROWS + i * NUM_COLS + j;
                    if (index >= items.size())
                        break;
                    CatalogItem item = items.get(index);
                    controller.setItem(item);
                    controller.setItemLabel(item.getDescription());
                    controller.setItemImage(new Image(APIAccess.getImageUrl(item.getPicture())));
                    controller.setItemPriceLabel(item.getPrice() + " NIS");
                    double discountedPrice = item.getPrice();
                    discountedPrice *= (100 - item.getDiscountPercent()) / 100;
                    discountedPrice -= item.getDiscountAmount();
                    if (discountedPrice < item.getPrice())
                        controller.setDiscount(discountedPrice);
                    catalogGrid.add(itemCell, j, i);
                }
            }
            updatePages();
        });

    }

    private void updatePages() {
        pageNum.setText(String.valueOf(page));
        pages = (int) Math.ceil((double)items.size() / ((double)NUM_ROWS * (double)NUM_COLS));
        totalPages.setText(String.valueOf(pages));

        backButton.setDisable(page == 1);
        forwardButton.setDisable(page == pages);
    }

    private void searchCatalog(long id, String keywords) {
        service.searchCatalogByStore(id, keywords).enqueue(new Callback<List<CatalogItem>>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200) {
                    Utils.showError("Error code: " + response.code());
                }

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
                Utils.showError("Network failure");
            }
        });
    }

    private void getCatalog(long id) {
        service.getCatalogByStore(id).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<CatalogItem>> call, Response<List<CatalogItem>> response) {
                if (response.code() != 200) {
                    Utils.showError("Error code: " + response.code());
                }

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
                throwable.printStackTrace();
                Utils.showError("Network failure");
            }
        });
    }

    @Override
    public void refresh() {
        updateBuyButtons();
        cart = new ArrayList<>();

        EventBus.getDefault().register(this);
        service = APIAccess.getService();

        storeChoiceBox.getSelectionModel().selectedIndexProperty().addListener(
                (observableValue, number, t1) -> {
                    int selectedIndex = storeChoiceBox.getSelectionModel().getSelectedIndex();
                    if (selectedIndex < 0 || selectedIndex >= stores.size())
                        return;
                    getCatalog(stores.get
                            (selectedIndex).getId());
                    updateCartButton();
                });
        updateStoresList();

        searchField.textProperty().addListener((observableValue, s, t1) -> {
            long storeId = stores.get(storeChoiceBox.getSelectionModel().getSelectedIndex()).getId();
            if (searchField.getText().equals(""))
                getCatalog(storeId);
            else {
                searchCatalog(storeId, searchField.getText());
            }
        });
    }

    @Override
    public void onSwitch() {
        catalogGrid.getChildren().clear();
        EventBus.getDefault().unregister(this);
    }

    private void updateCartButton() {
        String builder = "Go to cart (" +
                cart.size() +
                " " +
                (cart.size() == 1 ? "item" : "items") +
                ")";
        cartButton.setText(builder);
    }

    @Subscribe
    public void handleItemBuy(CatalogItemBuyEvent event) throws IOException {
        if (!cart.contains(event.getItem())) {
            quantities.put(event.getItem().getId(), (long) event.getQuantity());
            cart.add(event.getItem());
        } else {
            quantities.put(event.getItem().getId(), quantities.get(event.getItem().getId()) + event.getQuantity());
        }
        updateCartButton();

        Parent dialog = LayoutManager.getInstance().getFXML("popup_add_item").getKey();
        Stage popupStage = new Stage();
        Scene popupScene = new Scene(dialog);
        EventBus.getDefault().post(new PopupAddItemEvent(cart, quantities, popupStage));
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    @FXML
    void newCustomItem() {

        EventBus.getDefault().post(new DashboardSwitchEvent("new_custom_item"));
        EventBus.getDefault().post(new CustomItemBeginEvent(
                stores.get(storeChoiceBox.getSelectionModel().getSelectedIndex()), cart, quantities));
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        quantities = new HashMap<>();
    }

    @Subscribe
    public void onCustomItemFinish(CustomItemFinishEvent event) {
        // We need to save those since we
        cart.addAll(event.getCurrentCart());
        quantities.putAll(event.getQuantities());
        cart.add(event.getItem());
        quantities.put(event.getItem().getId(), 1L);
        updateCartButton();
    }


}
