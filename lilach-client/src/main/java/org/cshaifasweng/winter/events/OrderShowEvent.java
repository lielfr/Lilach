package org.cshaifasweng.winter.events;

import javafx.stage.Stage;
import org.cshaifasweng.winter.models.Order;

public class OrderShowEvent {
    private Order order;
    Stage popupStage;

    public OrderShowEvent(Order order, Stage popupStage){
        this.order = order;
        this.popupStage = popupStage;
    }

    public Order getOrder(){
        return order;
    }

    public Stage getPopupStage() {
        return popupStage;
    }
}
