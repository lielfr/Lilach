package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import org.cshaifasweng.winter.events.CatalogItemEditEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.CatalogItem;
import org.cshaifasweng.winter.models.CatalogItemType;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

public class AddItemController implements Refreshable, Initializable {
    private static final Logger log = Logger.getLogger(String.valueOf(AddItemController.class));

    @FXML
    private Label titleLabel;

    @FXML
    private TextField itemNameField;

    @FXML
    private TextField discountSumField;

    @FXML
    private TextField discountPercentageField;

    @FXML
    private Button addImageButton;

    @FXML
    private ImageView imageViewer;

    @FXML
    private Button cancelButton;

    @FXML
    private Button addItemButton;

    @FXML
    private TextField itemPriceField;

    @FXML
    private ChoiceBox<CatalogItemType> typeChoiceBox;

    @FXML
    private ChoiceBox<Store> storeChoiceBox;

    private LilachService lilachService;

    private CatalogItem createdItem;

    private List<Store> stores;

    byte[] imageAsBytes;

    private boolean isEdit = false;
    private boolean imageChanged = false;

    /**
     * Choosing the Item Image you want to upload to the catalog.
     * @param event
     */
    @FXML
    void addImage(ActionEvent event) throws IOException {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose image");
        File file = fileChooser.showOpenDialog(new Stage());
        if (file == null) return;
        imageAsBytes = new FileInputStream(file).readAllBytes();
        imageViewer.setImage(new Image(new ByteArrayInputStream(imageAsBytes)));
        imageChanged = true;
    }

    @FXML
    void addItem(ActionEvent event) {
        createdItem.setAvailableCount(50);
        createdItem.setCanBeAssembled(createdItem.getItemType() == CatalogItemType.ONE_FLOWER);
        createdItem.setDescription(itemNameField.getText());
        createdItem.setPrice(Double.parseDouble(itemPriceField.getText()));
        createdItem.setDiscountAmount(Double.parseDouble(discountSumField.getText()));
        createdItem.setDiscountPercent(Double.parseDouble(discountPercentageField.getText()));
        System.out.println("MOO: " + storeChoiceBox.getValue().getName());
        createdItem.setStore(storeChoiceBox.getValue());
        createdItem.setItemType(typeChoiceBox.getValue());

        Callback<CatalogItem> createEditItemCB = new Callback<>() {
            @Override
            public void onResponse(Call<CatalogItem> call, Response<CatalogItem> response) {
                if (response.code() != 200) {
                    log.severe("Got response code " + response.code());
                    return;
                }
                log.info("Successfully created item");

                Platform.runLater(() -> {
                    EventBus.getDefault().post(new DashboardSwitchEvent("edit_catalog_list"));
                });

            }

            @Override
            public void onFailure(Call<CatalogItem> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        };

        if (imageChanged) {
            RequestBody reqFileBody = RequestBody.create(MediaType.parse("multipart/form-data"), imageAsBytes);
            lilachService.uploadImage(MultipartBody.Part.createFormData("file", "name", reqFileBody)).enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    log.severe("Got response code " + response.code());
                    if (response.code() != 200) return;
                    createdItem.setPicture(response.body());
                    if (isEdit) {
                        lilachService.updateItem(createdItem.getId(), storeChoiceBox.getValue().getId(), createdItem).enqueue(createEditItemCB);
                    } else {
                        lilachService.newCatalogItem(storeChoiceBox.getValue().getId(), createdItem).enqueue(createEditItemCB);
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    throwable.printStackTrace();
                }
            });
        } else {
            lilachService.updateItem(createdItem.getId(), storeChoiceBox.getValue().getId(), createdItem).enqueue(createEditItemCB);
        }


    }

    @FXML
    void cancelItemAddition(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("edit_catalog_list"));

    }

    @Override
    public void refresh() {
        imageViewer.setImage(new Image(String.valueOf(LayoutManager.class.getResource("no-image-icon-13.png"))));
        typeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(CatalogItemType itemType) {
                switch (itemType) {
                    case ONE_FLOWER:
                        return "One Flower (for custom items)";
                    case FLOWERPOT:
                        return "Flowerpot";
                    case BRIDAL_BOUQUET:
                        return "Bridal Bouquet";
                    case FLOWER_ARRANGEMENT:
                        return "Flower Arrangement";
                    default:
                        return "None";
                }
            }

            @Override
            public CatalogItemType fromString(String s) {
                return null;
            }
        });
        typeChoiceBox.setItems(FXCollections.observableList(Arrays.asList(CatalogItemType.values())));
        typeChoiceBox.getSelectionModel().select(0);

        storeChoiceBox.setConverter(new StringConverter<>() {
            @Override
            public String toString(Store store) {
                if (store == null) return null;
                return store.getName();
            }

            @Override
            public Store fromString(String s) {
                return null;
            }
        });

        lilachService = APIAccess.getService();
        lilachService.getAllStores().enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Store>> call, Response<List<Store>> response) {
                if (response.code() != 200 || response.body() == null) return;
                stores = response.body();

                Platform.runLater(() -> {
                    storeChoiceBox.setItems(FXCollections.observableList(response.body()));
                    storeChoiceBox.getSelectionModel().select(0);
                });

            }

            @Override
            public void onFailure(Call<List<Store>> call, Throwable throwable) {

            }
        });

        createdItem = new CatalogItem();

        storeChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, number, t1) -> createdItem.setStore(storeChoiceBox.getValue()));

        typeChoiceBox.getSelectionModel().selectedIndexProperty()
                .addListener((observableValue, number, t1) -> createdItem.setItemType(typeChoiceBox.getValue()));
    }

    @Override
    public void onSwitch() {
    }

    @Subscribe
    public void handleEdit(CatalogItemEditEvent event) {
        createdItem = event.getCatalogItem();
        titleLabel.setText("Edit Item");
        addItemButton.setText("Save Changes");
        addImageButton.setText("Change Image");

        itemNameField.setText(createdItem.getDescription());
        itemPriceField.setText(String.valueOf(createdItem.getPrice()));
        discountSumField.setText(String.valueOf(createdItem.getDiscountAmount()));
        discountPercentageField.setText(String.valueOf(createdItem.getDiscountPercent()));
        typeChoiceBox.setValue(createdItem.getItemType());
        storeChoiceBox.setValue(createdItem.getStore());

        imageViewer.setImage(new Image(APIAccess.getImageUrl(createdItem.getPicture())));
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        EventBus.getDefault().register(this);
    }
}
