package org.cshaifasweng.winter;

import javafx.event.EventHandler;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class Utils<T> {
    public javafx.util.Callback<TableColumn<T, Date>, TableCell<T, Date>> dateFactory = tableColumn -> {
        TableCell<T, Date> cell = new TableCell<>() {
            private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz");
            @Override
            protected void updateItem(Date date, boolean b) {
                if (date != null) {
                    setText(dateFormat.format(date));
                }
            }
        };
        return cell;
    };

    public static void addContextMenu(TableView tableView, ContextMenu menu) {
        tableView.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton() == MouseButton.SECONDARY) {
                    menu.show(tableView, mouseEvent.getScreenX(), mouseEvent.getScreenY());
                } else if (mouseEvent.getButton() == MouseButton.PRIMARY) {
                    menu.hide();
                }
            }
        });
    }

    public static LocalDate toLocalDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public static boolean emptyOrNullCheckField(TextField checkedField, Label errorField, String empty) {
        if (checkedField == null || checkedField.getText() == null || checkedField.getText().isEmpty()) {
            errorField.setVisible(true);
            errorField.setText(empty);
            return false;
        }
        return true;
    }

    public static void showError(String error) {
        Alert alert = new Alert(Alert.AlertType.ERROR, error);
        alert.show();
    }

    public static void showInfo(String info) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, info);
        alert.show();
    }

}
