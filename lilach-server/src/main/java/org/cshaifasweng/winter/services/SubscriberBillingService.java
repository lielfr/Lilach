package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.SubscriberType;
import org.cshaifasweng.winter.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SubscriberBillingService {
    @Value("${lilach.billing.monthly-fee}")
    static long monthlySubscriptionFee;

    @Value("${lilach.billing.yearly-fee}")
    static long yearlySubscriptionFee;

    private final CustomerRepository customerRepository;

    @Autowired
    public SubscriberBillingService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Scheduled(cron = "${lilach.scheduling.subscriber-billing}")
    public void billSubscribers() {
        List<Customer> customerList = customerRepository.findAll();
        Date now = new Date();

        for (Customer customer : customerList) {
            if (customer.getSubscriberType() == SubscriberType.MONTHLY &&
                    customer.getSubscriptionEnd().before(now)) {
                customer.getTransactions()
                        .add(new Transaction(now, "Monthly subscription fee", monthlySubscriptionFee));
            }
            else if (customer.getSubscriberType() == SubscriberType.YEARLY &&
                    customer.getSubscriptionEnd().before(now)) {
                customer.getTransactions()
                        .add(new Transaction(now, "Yearly subscription fee", yearlySubscriptionFee / 12.0));
            }
            customerRepository.save(customer);
        }
    }
}
