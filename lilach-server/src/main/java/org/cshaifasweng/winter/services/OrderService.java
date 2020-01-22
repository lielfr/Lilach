package org.cshaifasweng.winter.services;

import org.apache.juli.logging.Log;
import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.da.OrderRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MailService mailService;
    private final CustomerRepository customerRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MailService mailService, CustomerRepository customerRepository) {
        this.orderRepository = orderRepository;
        this.mailService = mailService;
        this.customerRepository = customerRepository;
    }

    public Order newOrder(Order newOrder, Customer customer) throws LogicalException {
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

        newOrder.setOrderedBy(customer);

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

    public OrderCompensation cancelOrderCustomer(long id, Customer customer) throws LogicalException {
        Order order = orderRepository.getOne(id);

        if (!order.getOrderedBy().equals(customer))
            throw new LogicalException("Cannot cancel others' order");

        order.setStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);

        Date supplyDate = order.getSupplyDate();

        Calendar threeHoursFromNow = Calendar.getInstance();
        threeHoursFromNow.add(Calendar.HOUR_OF_DAY, 3);

        Calendar anHourFromNow = Calendar.getInstance();
        anHourFromNow.add(Calendar.HOUR_OF_DAY, 1);

        if (supplyDate.after(threeHoursFromNow.getTime())) {
            return refundCustomer(customer, order, 100);
        } else if (supplyDate.before(anHourFromNow.getTime())) {
            return refundCustomer(customer, order, 50);
        } else {
            return refundCustomer(customer, order, 0);
        }
    }

    @Transactional
    OrderCompensation refundCustomer(Customer customer, Order order, double percentage) {
        double refund = order.getPrice() * (percentage / 100.0);
        customer.getTransactions().add(
                new Transaction(new Date(), "Order #" + order.getId() + " " +
                        percentage + "% refunded", refund)
        );
        customerRepository.save(customer);
        return new OrderCompensation(refund);
    }

    @Transactional
    public OrderCompensation cancelOrderEmployee(long id) {
        Order order = orderRepository.getOne(id);
        order.setStatus(OrderStatus.CANCELLED);

        return new OrderCompensation(0.0);
    }
}
