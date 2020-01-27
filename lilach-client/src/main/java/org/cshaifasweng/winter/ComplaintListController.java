package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.greenrobot.eventbus.EventBus;

public class ComplaintListController {

    @FXML
    private TableView<?> complaintListTable;

    @FXML
    private Button backButton;

    @FXML
    void back(MouseEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

}
