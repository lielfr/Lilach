package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.da.OrderRepository;
import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final MailService mailService;
    private final CustomerRepository customerRepository;
    private final UserRepository userRepository;

    @Autowired
    public OrderService(OrderRepository orderRepository, MailService mailService, CustomerRepository customerRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.mailService = mailService;
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    public Order newOrder(Order newOrder, Customer customer) throws LogicalException {
        Map<String, Object> requiredFields = new HashMap<>();
        requiredFields.put("supplyDate", newOrder.getSupplyDate());
        requiredFields.put("store", newOrder.getStore());
        requiredFields.put("deliveryMethod", newOrder.getDeliveryMethod());
        requiredFields.put("quantities", newOrder.getQuantities());

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

        double price = 0.0;
        for (Item item : newOrder.getItems()) {
            double itemPrice = item.getPrice();
            // Handling discount
            if (item instanceof CatalogItem) {
                CatalogItem catalogItem = (CatalogItem) item;
                itemPrice -= catalogItem.getDiscountAmount();
                itemPrice *= (100 - catalogItem.getDiscountPercent()) / 100;
            }
            price += itemPrice * newOrder.getQuantities().get(item.getId());
            if (item instanceof CustomItem) {
                CustomItem customItem = (CustomItem) item;
                for (CatalogItem customItemChild : customItem.getItems()) {
                    if (customItemChild.getItemType() != CatalogItemType.ONE_FLOWER)
                        throw new LogicalException("Illegal custom item: contains items that cannot be assembled.");
                }
            }
        }


        // Discount price for subscribers.
        if (customer.getSubscriberType() != null) {
            switch (customer.getSubscriberType()) {
                case MONTHLY:
                    price = price * 0.75;
                    break;
                case YEARLY:
                    price = price * 0.65;
                    break;
            }
        }

        newOrder.setPrice(price);

        newOrder.setStatus(OrderStatus.PENDING);

        orderRepository.save(newOrder);

        Employee manager = newOrder.getStore().getManager();
        mailService.sendMail(manager.getEmail(), "New order",
                "<p>You got a new order: #" + newOrder.getId() + "</p>");


        return newOrder;
    }

    public static void validateRequiredFields(Map<String, Object> requiredFields) throws LogicalException {
        UserService.enforceFields(requiredFields);
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

    @Transactional
    public List<Order> findByCustomer(long id, Authentication authentication) throws LogicalException {
        User user = userRepository.findByEmail(authentication.getName());

        if (user.getId() != id &&
                !authentication.getAuthorities()
                        .stream().map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList()).contains(SecurityConstants.PRIVILEGE_ORDERS_VIEW_ALL))
            throw new LogicalException("Unauthorized to view this user's orders");
        return orderRepository.findAllByOrderedByAndStatus(customerRepository.getOne(id), OrderStatus.PENDING);
    }

    @Transactional
    public Order markAsDelivered(long id) {
        Order order = orderRepository.getOne(id);
        order.setStatus(OrderStatus.DELIVERED);

        if (order.isDeliverToAnother()) {
            String message = "An order for you has arrived.<br>";
            if (order.getGreeting() != null && !order.getGreeting().isEmpty())
                message += "The sender would also like you to read this:<br>" + order.getGreeting() + "<br>";
            message += "Greetings,<br>The Lilach team.";
            mailService.sendMail(order.getRecipientMail(), "Order arrived", message);
        } else {
            mailService.sendMail(order.getOrderedBy().getEmail(), "Order arrived",
                    "Your order #" + order.getId() + "has arrived.<br>Greetings,<br>The Lilach team.");
        }

        return order;

    }
}
