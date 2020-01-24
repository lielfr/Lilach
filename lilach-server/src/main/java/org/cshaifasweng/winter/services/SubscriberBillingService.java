package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.SubscriberType;
import org.cshaifasweng.winter.models.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;

@Component
public class SubscriberBillingService {
    // TODO: Move those to the configuration file
    private static final long runningRate = 3600 * 30; // Run every month
    private static final long monthlySubscriptionFee = 20;
    private static final long yearlySubscriptionFee = 200;

    private final CustomerRepository customerRepository;

    @Autowired
    public SubscriberBillingService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    @Scheduled(fixedRate = runningRate)
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
                        .add(new Transaction(now, "Yearly subscription fee", yearlySubscriptionFee));
            }
            customerRepository.save(customer);
        }
    }
}
