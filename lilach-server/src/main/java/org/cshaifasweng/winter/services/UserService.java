package org.cshaifasweng.winter.services;

import org.cshaifasweng.winter.da.*;
import org.cshaifasweng.winter.exceptions.LogicalException;
import org.cshaifasweng.winter.models.Customer;
import org.cshaifasweng.winter.models.Employee;
import org.cshaifasweng.winter.models.User;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final CustomerRepository customerRepository;
    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final StoreRepository storeRepository;

    @Autowired
    public UserService(UserRepository userRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, RoleRepository roleRepository, StoreRepository storeRepository) {
        this.userRepository = userRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.roleRepository = roleRepository;
        this.storeRepository = storeRepository;
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
    public Customer newCustomer(Customer customer, long storeId) throws LogicalException {
        if (customer == null) {
            throw new LogicalException("Cannot register empty user");
        }



        Map<String, Object> requiredFields = new HashMap<>();

        try {
            requiredFields.put("misparZehut", customer.getMisparZehut());
            requiredFields.put("email", customer.getEmail());
            requiredFields.put("password", customer.getPassword());
            requiredFields.put("firstName", customer.getFirstName());
            requiredFields.put("lastName", customer.getLastName());
            requiredFields.put("phone", customer.getPhone());
            requiredFields.put("creditCard", customer.getCreditCard());
            requiredFields.put("dateOfBirth", customer.getDateOfBirth());
            requiredFields.put("address", customer.getAddress());
        } catch (NullPointerException e) {
            throw new LogicalException("Received malformed object");
        }


        enforceFields(requiredFields);

        Customer customerInDB = customerRepository.findByEmail(customer.getEmail());
        if (customerInDB != null) {
            throw new LogicalException("User already exists");
        }

        customerInDB = new Customer(customer.getMisparZehut(), customer.getEmail(), new BCryptPasswordEncoder().encode(customer.getPassword()),
                customer.getFirstName(), customer.getLastName(), customer.getPhone(),
                Arrays.asList(roleRepository.findByName(SecurityConstants.ROLE_CUSTOMER)),
                customer.getCreditCard(), customer.getExpireDate(), customer.getCvv(), customer.getDateOfBirth());
        customerInDB.getStores().add(storeRepository.getOne(storeId));
        Calendar yearFromNow = Calendar.getInstance();
        yearFromNow.add(Calendar.YEAR, 1);

        if (customer.getSubscriberType() != null)
            customer.setExpireDate(yearFromNow.getTime());

        customerRepository.save(customerInDB);

        // Copy it so we don't accidentally set the password to null
        customerInDB = (Customer) customerInDB.copy();

        // Hide stuff that shouldn't be in the JSON
        customerInDB.setPassword(null);
        return customerInDB;
    }

    static void enforceFields(Map<String, Object> requiredFields) throws LogicalException {
        List<String> missingFields = new ArrayList<>();
        for (Map.Entry<String, Object> entry: requiredFields.entrySet()) {

            if (entry.getValue() == null) {
                missingFields.add(entry.getKey());
            }
        }
        if (!missingFields.isEmpty())
            throw new LogicalException("Missing field(s): "+ String.join(", ", missingFields));
    }

    @Transactional
    public User getUser(String email) throws LogicalException {
        User user = userRepository.findByEmail(email);

        if (user == null)
            throw new LogicalException("User does not exist");

        return user;
    }

    @Transactional
    public Customer updateCustomer(long id, Customer customer) throws LogicalException {
        Customer dbObject = customerRepository.getOne(id);

        if (dbObject.getId() != id)
            throw new LogicalException("Id does not match");
        // Mispar Zehut should not be changed
        if (customer.getPassword() != null && !customer.getPassword().isEmpty()) {
            System.out.println("Set password to: " + customer.getPassword());
            dbObject.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        }
        dbObject.setFirstName(customer.getFirstName());
        dbObject.setLastName(customer.getLastName());
        dbObject.setSubscriberType(customer.getSubscriberType());
        dbObject.setAddress(customer.getAddress());
        dbObject.setCreditCard(customer.getCreditCard());
        dbObject.setCvv(customer.getCvv());
        dbObject.setPhone(customer.getPhone());
        dbObject.setDateOfBirth(customer.getDateOfBirth());
        dbObject.setExpireDate(customer.getExpireDate());
        dbObject.setMisparZehut(customer.getMisparZehut());

        Calendar yearFromNow = Calendar.getInstance();
        yearFromNow.add(Calendar.YEAR, 1);

        if (customer.getSubscriberType() != null && !customer.getSubscriberType().equals(dbObject.getSubscriberType()))
            dbObject.setExpireDate(yearFromNow.getTime());

        customerRepository.save(dbObject);

        return customer;
    }

    @Transactional
    public Employee updateEmployee(long id, Employee employee) {
        return employeeRepository.save(employee);
    }

    @Transactional
    public List<User> getEmployeesByStore(long id) {
        return employeeRepository.findAllByAssignedStore(storeRepository.getOne(id))
                .stream().map(employee -> (User) employee).collect(Collectors.toList());
    }

    @Transactional
    public List<User> getCustomersByStore(long id) {
        return customerRepository.findAllByStoresContains(storeRepository.getOne(id))
                .stream().map(customer -> (User) customer).collect(Collectors.toList());
    }

    @Transactional
    public List<User> getUsersByStore(long id) {
        List<User> employees = getEmployeesByStore(id);
        List<User> customers = getCustomersByStore(id);
        List<User> ret = new ArrayList<>();
        ret.addAll(employees);
        ret.addAll(customers);

        return ret;
    }

    @Transactional
    public List<User> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(employee -> (User) employee).collect(Collectors.toList());
    }

    @Transactional
    public List<User> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customer -> (User) customer).collect(Collectors.toList());
    }

    @Transactional
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}
