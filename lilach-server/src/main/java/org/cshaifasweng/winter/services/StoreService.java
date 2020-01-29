package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.da.StoreRepository;
import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Store;
import org.cshaifasweng.winter.models.User;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;

    public StoreService(StoreRepository storeRepository, UserRepository userRepository, CustomerRepository customerRepository) {
        this.storeRepository = storeRepository;
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
    }

    public List<Store> getAllStores() {
        return storeRepository.findAll();
    }

    public List<Store> getStoresByCustomer(long id, Authentication authentication) throws LogicalException {
        User user = userRepository.findByEmail(authentication.getName());
        if (user instanceof Customer && user.getId() != id)
            throw new LogicalException("Unauthorized to view other users");
        return customerRepository.getOne(id).getStores();
    }
}
