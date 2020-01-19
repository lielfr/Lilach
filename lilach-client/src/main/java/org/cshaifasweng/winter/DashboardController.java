package org.cshaifasweng.winter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.events.LoginEvent;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.web.APIAccess;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


public class DashboardController implements Initializable {
    protected static final Logger log = Logger.getLogger(DashboardController.class.getName());

    public DashboardController() {
        LoggerUtils.setupLogger(log);
    }

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
    public void handleUserLogin(LoginEvent event) {

        User user = APIAccess.getCurrentUser();
        log.finest("Dashboard: got LoginEvent, user = " + user);
        if (user == null) return;
        welcomeLabel.setText("Welcome, "+user.getFirstName());
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage("primary");
        EventBus.getDefault().register(this);
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

}
