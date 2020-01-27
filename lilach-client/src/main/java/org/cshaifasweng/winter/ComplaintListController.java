package org.cshaifasweng.winter;

import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import org.cshaifasweng.winter.events.ComplaintHandleEvent;
import org.cshaifasweng.winter.events.DashboardSwitchEvent;
import org.cshaifasweng.winter.models.Complaint;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.web.APIAccess;
import org.cshaifasweng.winter.web.LilachService;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.Date;
import java.util.List;

public class ComplaintListController implements Refreshable {

    @FXML
    private TableView<Complaint> complaintListTable;

    @FXML
    private Button backButton;

    private List<Complaint> complaints;

    @FXML
    void back(MouseEvent event) {
        EventBus.getDefault().post(new DashboardSwitchEvent("primary"));
    }

    @Subscribe
    public void handleEvent(ComplaintHandleEvent event) {
        event.getComplaint();
    }

    private void populateTableComplaints(){
        complaintListTable.getColumns().clear();
        TableColumn<Complaint, Long> idColumn = new TableColumn<>("Id");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("Id"));
        TableColumn<Complaint, Date> complaintOpenColumn = new TableColumn<>("Opened in");
        complaintOpenColumn.setCellValueFactory(new PropertyValueFactory<>("complaintOpen"));

        complaintOpenColumn.setCellFactory(new Utils<Complaint>().dateFactory);
        TableColumn<Complaint, Customer> complaintOpenedByColumn = new TableColumn<>("Opened by");
        complaintOpenedByColumn.setCellValueFactory(new PropertyValueFactory<>("openedBy"));
        complaintOpenColumn.setCellValueFactory(features ->
                new SimpleObjectProperty<>(features.getValue().getComplaintOpen()));
        complaintOpenedByColumn.setCellFactory(tableColumn -> {
            TableCell<Complaint, Customer> cell = new TableCell<>() {
                @Override
                protected void updateItem(Customer s, boolean b) {
                    if (s != null) {
                        setText(s.getFirstName() + " " + s.getLastName());
                    }
                }
            };
            return cell;
        });

        TableColumn<Complaint, Boolean> complaintIsOpenedColumn = new TableColumn<>("Is open");
        complaintIsOpenedColumn.setCellValueFactory(features ->
                new SimpleBooleanProperty(features.getValue().isOpen()));
        complaintIsOpenedColumn.setCellFactory(tableColumn -> {
            TableCell<Complaint, Boolean> cell = new TableCell<>(){
                @Override
                protected void updateItem(Boolean isOpen, boolean b) {
                    if (isOpen != null)
                        setText(isOpen?"Yes":"No");
                }
            };
            return cell;
        });

        TableColumn<Complaint, Date> complaintCloseColumn = new TableColumn<>("Closed in");
        complaintCloseColumn.setCellValueFactory(features ->
                new SimpleObjectProperty<>(features.getValue().getComplaintClose()));
        complaintCloseColumn.setCellFactory(new Utils<Complaint>().dateFactory);

        TableColumn<Complaint, Employee> complaintHandledByColumn = new TableColumn<>("Handled by");
        complaintHandledByColumn.setCellValueFactory(new PropertyValueFactory<>("handledBy"));
        complaintHandledByColumn.setCellFactory(tableColumn -> {
            TableCell<Complaint, Employee> cell = new TableCell<>() {
                @Override
                protected void updateItem(Employee employee, boolean b) {
                    if (employee != null) {
                        setText(employee.getFirstName() + " " + employee.getLastName());
                    }
                }
            };
            return cell;
        });

        TableColumn<Complaint, String> complaintAnswerColumn = new TableColumn<>("Response");
        complaintAnswerColumn.setCellValueFactory(new PropertyValueFactory<>("answer"));

        TableColumn<Complaint, Double> complaintCompensationColumn = new TableColumn<>("Compensation");
        complaintCompensationColumn.setCellValueFactory(new PropertyValueFactory<>("compensation"));

        complaintListTable.getColumns().addAll(
                idColumn,
                complaintOpenColumn,
                complaintOpenedByColumn,
                complaintIsOpenedColumn,
                complaintCloseColumn,
                complaintHandledByColumn,
                complaintAnswerColumn,
                complaintCompensationColumn
        );
        complaintListTable.setItems(FXCollections.observableList(complaints));

    }

    @Override
    public void refresh() {
        LilachService service = APIAccess.getService();
        Customer customer = (Customer) APIAccess.getCurrentUser();
        service.getComplaintsByCustomer(customer.getId()).enqueue(new Callback<List<Complaint>>() {
            @Override
            public void onResponse(Call<List<Complaint>> call, Response<List<Complaint>> response) {
                if (response.code() != 200)
                    return;

                complaints = response.body();

                for (Complaint complaint : complaints) {
                    System.out.println("ID: " + complaint.getId() +", Opened in: " + complaint.getComplaintOpen());
                }

                Platform.runLater(() -> {
                    populateTableComplaints();
                });

            }

            @Override
            public void onFailure(Call<List<Complaint>> call, Throwable throwable) {

            }
        });
    }

    @Override
    public void onSwitch() {

    }

    @FXML
    public void updateTable() {
        populateTableComplaints();
    }
}
