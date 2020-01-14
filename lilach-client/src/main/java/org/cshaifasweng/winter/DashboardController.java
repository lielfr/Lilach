package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.event.ActionEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class DashboardController implements Initializable {
    @FXML
    private ScrollPane containerPane;

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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        setPage("primary");
        EventBus.getDefault().register(this);
    }
}
