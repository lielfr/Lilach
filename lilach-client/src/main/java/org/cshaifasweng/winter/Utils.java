package org.cshaifasweng.winter;

import javafx.event.EventHandler;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.text.SimpleDateFormat;
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

}
