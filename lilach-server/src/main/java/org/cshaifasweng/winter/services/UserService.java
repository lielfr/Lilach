package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.da.RoleRepository;
import org.cshaifasweng.winter.da.UserRepository;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final RoleRepository roleRepository;

    @Autowired
    public UserService(UserRepository userRepository, CustomerRepository customerRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void setUserLoggedIn(long id, boolean isLoggedIn) {
        User user = userRepository.getOne(id);
        user.setLoggedIn(isLoggedIn);
        userRepository.save(user);
    }

    @Transactional
    public void setUserLoggedIn(String email, boolean isLoggedIn) {
        User user = userRepository.findByEmail(email);
        user.setLoggedIn(isLoggedIn);
        userRepository.save(user);
    }

    @Transactional
    public Customer newCustomer(Customer customer) throws LogicalException {
        if (customer == null) {
            throw new LogicalException("Cannot register empty user");
        }



        Map<String, Object> requiredFields = new HashMap<>();

        try {
            requiredFields.put("email", customer.getEmail());
            requiredFields.put("password", customer.getPassword());
            requiredFields.put("firstName", customer.getFirstName());
            requiredFields.put("lastName", customer.getLastName());
            requiredFields.put("phone", customer.getPhone());
            requiredFields.put("creditCard", customer.getCreditCard());
            requiredFields.put("dateOfBirth", customer.getDateOfBirth());
        } catch (NullPointerException e) {
            throw new LogicalException("Received malformed object");
        }


        List<String> missingFields = new ArrayList<>();
        for (Map.Entry<String, Object> entry: requiredFields.entrySet()) {

            if (entry.getValue() == null) {
                missingFields.add(entry.getKey());
            }
        }
        if (!missingFields.isEmpty())
            throw new LogicalException("Missing field(s): "+ String.join(", ", missingFields));

        Customer customerInDB = customerRepository.findByEmail(customer.getEmail());
        if (customerInDB != null) {
            throw new LogicalException("User already exists");
        }

        customerInDB = new Customer(customer.getEmail(), new BCryptPasswordEncoder().encode(customer.getPassword()),
                customer.getFirstName(), customer.getLastName(), customer.getPhone(),
                Arrays.asList(roleRepository.findByName(SecurityConstants.ROLE_CUSTOMER)),
                customer.getCreditCard(), customer.getExpireDate(), customer.getCvv(), customer.getDateOfBirth());

        customerRepository.save(customerInDB);

        // Copy it so we don't accidentally set the password to null
        customerInDB = (Customer) customerInDB.copy();

        // Hide stuff that shouldn't be in the JSON
        customerInDB.setPassword(null);
        return customerInDB;
    }

    @Transactional
    public User getUser(String email) throws LogicalException {
        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new LogicalException("User does not exist");

        User userCopy = user.copy();
        userCopy.setPassword(null);

        System.out.println("Type: " + user.getClass());

        return userCopy;
    }
}
