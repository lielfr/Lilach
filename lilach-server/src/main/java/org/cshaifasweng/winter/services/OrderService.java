package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.EmployeeRepository;
import org.cshaifasweng.winter.da.OrderRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.DeliveryMethod;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MailService mailService;

    @Autowired
    public OrderService(OrderRepository orderRepository, MailService mailService) {
        this.orderRepository = orderRepository;
        this.mailService = mailService;
    }

    public Order newOrder(Order newOrder) throws LogicalException {
        Map<String, Object> requiredFields = new HashMap<>();
        requiredFields.put("supplyDate", newOrder.getSupplyDate());
        requiredFields.put("store", newOrder.getStore());
        requiredFields.put("deliveryMethod", newOrder.getDeliveryMethod());

        if (
                newOrder.getDeliveryMethod() != null
                && newOrder.getDeliveryMethod() == DeliveryMethod.DELIVER_TO_ADDRESS
                && newOrder.isDeliverToAnother()
        ) {
            requiredFields.put("deliveryAddress", newOrder.getDeliveryAddress());
            requiredFields.put("recipientMail", newOrder.getRecipientMail());
        }

        validateRequiredFields(requiredFields);

        if (newOrder.getSupplyDate().before(new Date()))
            throw new LogicalException("Supply date cannot be in the past");

        newOrder.setOrderDate(new Date());
        orderRepository.save(newOrder);

        Employee manager = newOrder.getStore().getManager();
        mailService.sendMail(manager.getEmail(), "New order",
                "<p>You got a new order: #" + newOrder.getId() + "</p>");


        return newOrder;
    }

    public static void validateRequiredFields(Map<String, Object> requiredFields) throws LogicalException {
        List<String> missingFields = new ArrayList<>();

        for (Map.Entry<String, Object> entry : requiredFields.entrySet()) {
            if (entry.getValue() == null)
                missingFields.add(entry.getKey());
        }

        if (!missingFields.isEmpty())
            throw new LogicalException("Missing field(s): " + String.join(", ", missingFields));
    }

    public void cancelOrder(long id) {
        Order order = orderRepository.getOne(id);

        Date supplyDate = order.getSupplyDate();

        Calendar threeHoursFromNow = Calendar.getInstance();
        threeHoursFromNow.add(Calendar.HOUR_OF_DAY, 3);

        Calendar anHourFromNow = Calendar.getInstance();
        anHourFromNow.add(Calendar.HOUR_OF_DAY, 1);

        if (supplyDate.after(threeHoursFromNow.getTime())) {
            // TODO: Full refund
        } else if (supplyDate.after(anHourFromNow.getTime())) {
            // TODO: Partial refund
        } else {
            // TODO: No Refund
        }
    }
}
