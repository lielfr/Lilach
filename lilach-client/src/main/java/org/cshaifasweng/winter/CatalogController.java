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
import org.cshaifasweng.winter.events.*;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
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

    private Set<Item> cart;

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
    public void goToCart() {
        EventBus.getDefault().post(new DashboardSwitchEvent("order"));
        EventBus.getDefault().post(new OrderCreateEvent(cart));
        cart.clear();
    }

    @FXML
    public void backPage() {
        if (page == 1) return;
        page--;
        updatePages();
    }

    @FXML
    public void nextPage() {
        if (page == pages) return;
        page++;
        updatePages();
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

    @Subscribe
    public void handleLogin(LoginChangeEvent changeEvent) {
        User user = APIAccess.getCurrentUser();
        if (user == null || user instanceof Employee)
            return;
        Customer customer = (Customer) APIAccess.getCurrentUser();
        if (customer.getStores() == null) return;
        storeChoiceBox.getItems().clear();
        storeChoiceBox.getItems().addAll(
                customer
                        .getStores()
                        .stream()
                        .map(Store::getName).collect(Collectors.toList()));
        storeChoiceBox.getSelectionModel().select(0);
    }

    private void updateStoresList() {
        if (APIAccess.getCurrentUser() == null || APIAccess.getCurrentUser() instanceof Employee) {
            storeChoiceBox.getItems().clear();
            service.getAllStores().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                    if (response.code() != 200 || response.body() == null) return;
                    stores.clear();
                    stores.addAll(response.body());
                    if (stores == null) return;
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

                }
            });
        } else {
            handleLogin(null);
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
                (observableValue, number, t1) -> {
                    int selectedIndex = storeChoiceBox.getSelectionModel().getSelectedIndex();
                    if (selectedIndex < 0 || selectedIndex >= stores.size())
                        return;
                    getCatalog(stores.get
                            (selectedIndex).getId());
                    cart.clear();
                    updateCartButton();
                });
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
        if (!cart.add(event.getItem())) {
            int previousQuantity = cart.stream()
                    .filter((element) -> element.equals(event.getItem()))
                    .collect(Collectors.toList()).get(0).getQuantity();
            cart.remove(event.getItem());
            cart.add(event.getItem());
        }
        updateCartButton();

        Parent dialog = LayoutManager.getInstance().getFXML("popup_add_item");
        Stage popupStage = new Stage();
        Scene popupScene = new Scene(dialog);
        EventBus.getDefault().post(new PopupAddItemEvent(cart, popupStage));
        popupStage.setScene(popupScene);
        popupStage.show();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        cart = new HashSet<>();
    }

    @FXML
    void newCustomItem() {

        EventBus.getDefault().post(new DashboardSwitchEvent("new_custom_item"));
        EventBus.getDefault().post(new CustomItemBeginEvent(
                stores.get(storeChoiceBox.getSelectionModel().getSelectedIndex())));
    }
}
