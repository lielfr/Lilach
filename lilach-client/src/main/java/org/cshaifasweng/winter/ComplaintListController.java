package org.cshaifasweng.winter;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.ComplaintHandleEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.Complaint;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class ComplaintListController {

    @FXML
    private TableView<?> complaintListTable;

    @FXML
    private Button backButton;

    @FXML
    void back(MouseEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @Subscribe
    public void handleEvent(ComplaintHandleEvent event) {
        event.getComplaint();
    }

    private void populateTableComplaints(){
//        complaintListTable.getColumns().clear();
//        TableColumn<Complaint, Long> idColumn = new TableColumn<>("Id");
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
////        TableColumn


    }

}
