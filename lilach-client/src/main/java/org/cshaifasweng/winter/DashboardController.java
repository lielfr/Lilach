package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.LoginChangeEvent;
import org.cshaifasweng.winter.events.TokenSetEvent;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;


public class DashboardController implements Initializable {
    protected static final Logger log = Logger.getLogger(DashboardController.class.getName());


    private boolean CartIsEmpty=false;   // Needs to be setup
    private boolean LoggedIn=true;       // Needs to be setup

    @FXML
    private ScrollPane containerPane;

    @FXML
    private Label welcomeLabel;

    private void setPage(String page) {
        try {
            containerPane.setContent(LayoutManager.getInstance().getFXML(page));
            Refreshable controller = (Refreshable) LayoutManager.getInstance().getController(page);
            controller.refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Subscribe
    public void handlePageChange(DashboardSwitchEvent event) throws IOException {
        setPage(event.pageName);
    }

    @Subscribe
    public void handleUserLogin(LoginChangeEvent event) {

        User user = APIAccess.getCurrentUser();
        log.finest("Dashboard: got LoginEvent, user = " + user);
        if (user == null)
            welcomeLabel.setText("Welcome, Guest");
        else
            welcomeLabel.setText("Welcome, "+user.getFirstName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage("primary");
        EventBus.getDefault().register(this);
        LoggerUtils.setupLogger(log);
    }
    @FXML
    void fileComplaint(ActionEvent event) {
        setPage("complaint_filing");
    }

    @FXML
    void handleComplaint(ActionEvent event) {
        setPage("complaint_handling");
    }

    @FXML
    void showLoginScreen(ActionEvent event) {
        setPage("login_screen");
    }

    @FXML
    void logout(ActionEvent event) {
        if (APIAccess.getCurrentUser() != null) {
            LilachService service = APIAccess.getService();
            service.logout().enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.code() != 200) {
                        log.severe("Something really weird happened: could not log out.");
                        return;
                    }

                    Platform.runLater(() -> {
                        APIAccess.setCurrentUser(null);
                        EventBus.getDefault().post(new TokenSetEvent(""));
                        EventBus.getDefault().post(new LoginChangeEvent());
                        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
                    });
                }

                @Override
                public void onFailure(Call<Void> call, Throwable throwable) {

                }
            });
        }

    }

    @FXML
     void cartButton(ActionEvent event) {
        if (CartIsEmpty && LoggedIn) {
            setPage("Empty_Cart_Customer");
        }
        else if (CartIsEmpty) {
            setPage("Empty_Cart_Guest");
        }
        else  {
            setPage("Customer_Cart");
        }


    }

    @FXML
    void backToCatalog(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @FXML
    void addItem(ActionEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("add_item"));
    }

}
