package org.cshaifasweng.winter.loaders;

import org.cshaifasweng.winter.SpringServer;
import org.cshaifasweng.winter.da.*;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.*;

@Component
public class SecurityDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean done = false; // Make sure we don't run this more than once

    private final PrivilegeRepository privilegeRepository;

    private final RoleRepository roleRepository;

    private final CustomerRepository customerRepository;

    private final EmployeeRepository employeeRepository;

    private final StoreRepository storeRepository;

    private final CatalogItemsRepository catalogItemsRepository;

    public SecurityDataLoader(PrivilegeRepository privilegeRepository, RoleRepository roleRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository, StoreRepository storeRepository, CatalogItemsRepository catalogItemsRepository) {
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
        this.storeRepository = storeRepository;
        this.catalogItemsRepository = catalogItemsRepository;
    }

    private byte[] imageAsBytes(String name) throws IOException {
        return SpringServer.class.getResourceAsStream(name).readAllBytes();
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        if (done)
            return;

        Privilege catalogEditPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_CATALOG_EDIT);
        Privilege usersEditPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_USERS_EDIT);
        Privilege complaintFilePrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_COMPLAINT_FILE);
        Privilege complaintHandlePrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_COMPLAINT_HANDLE);
        Privilege rolesEditPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_ROLES_EDIT);
        Privilege ordersCreatePrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_ORDERS_CREATE);
        Privilege ordersCancelPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_ORDERS_CANCEL);
        Privilege reportsViewPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_REPORTS_VIEW);
        Privilege reportsViewAllPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_REPORTS_VIEW_ALL);

        Role adminRole = createOrReturnRole(SecurityConstants.ROLE_ADMIN, Arrays.asList(
                catalogEditPrivilege,
                usersEditPrivilege,
                complaintFilePrivilege,
                complaintHandlePrivilege,
                rolesEditPrivilege,
                ordersCreatePrivilege,
                ordersCancelPrivilege,
                reportsViewAllPrivilege,
                reportsViewPrivilege
        ));

        Role customerRole = createOrReturnRole(SecurityConstants.ROLE_CUSTOMER, Arrays.asList(
                complaintFilePrivilege,
                ordersCreatePrivilege,
                ordersCancelPrivilege
        ));

        Role customerServiceEmployeeRole = createOrReturnRole(SecurityConstants.ROLE_CUSTOMER_SERVICE_EMPLOYEE, Arrays.asList(
                complaintHandlePrivilege,
                complaintFilePrivilege,
                ordersCancelPrivilege,
                ordersCreatePrivilege
        ));

        Role storeManagerEmployeeRole = createOrReturnRole(SecurityConstants.ROLE_STORE_MANAGER, Arrays.asList(
                complaintHandlePrivilege,
                reportsViewPrivilege
        ));

        Role storeChainManagerEmployeeRole = createOrReturnRole(SecurityConstants.ROLE_STORE_CHAIN_MANAGER, Arrays.asList(
                complaintFilePrivilege,
                usersEditPrivilege,
                reportsViewAllPrivilege,
                reportsViewPrivilege
        ));

        Store haifaUniBranch = createOrReturnStore("Haifa University Branch", "Abba Houshy Av. 199, Haifa",
                "04-9899999", "Every day between 8AM to 8PM");

        Store qiryatYamBranch = createOrReturnStore("Qiryat Yam Branch", "Moshe Sharet 12, Qiryat Yam",
                "04-8799999", "Every day betwen 8AM to 6PM");

        Calendar customerBirth = Calendar.getInstance();
        customerBirth.set(2000, 1, 1);

        Calendar customerCreditCardExpire = Calendar.getInstance();
        customerCreditCardExpire.set(2022, 1, 1);

        Customer customer = createOrReturnCustomer("customer@lilach.com", "moo",
                Collections.singletonList(customerRole), "Lilach", "Customer", "0509999999",
                11, customerCreditCardExpire.getTime(), 222, customerBirth.getTime(), Arrays.asList(haifaUniBranch));

        Employee admin = createOrReturnEmployee("lielft@gmail.com", "AdminBaby!",
                "Liel", "Fridman", "0509999999",
                Collections.singletonList(adminRole));

        Employee haifaUniManager = createOrReturnEmployee("haifa.uni.manager@lilach.com", "haifarocks",
                "Aharon", "Cohen", "0500009000",
                Collections.singletonList(storeManagerEmployeeRole));
        haifaUniManager.setManagedStore(haifaUniBranch);
        haifaUniManager.setAssignedStore(haifaUniBranch);
        haifaUniBranch.setManager(haifaUniManager);

        Employee qiryatYamManager = createOrReturnEmployee("qy.manager@lilach.com", "iloveky",
                "Lilach", "Schwartzman", "0509811999",
                Collections.singletonList(storeManagerEmployeeRole));
        qiryatYamBranch.setManager(qiryatYamManager);
        qiryatYamManager.setManagedStore(qiryatYamBranch);
        qiryatYamManager.setAssignedStore(qiryatYamBranch);

        employeeRepository.save(haifaUniManager);
        employeeRepository.save(qiryatYamManager);
        storeRepository.save(haifaUniBranch);
        storeRepository.save(qiryatYamBranch);


        List<CatalogItem> items = new ArrayList<>();
        try {
            CatalogItem item1 = createOrReturnItem(25, "Just another flower",
                    imageAsBytes("flower1.jpg"),
                    4, qiryatYamBranch, true, CatalogItemType.ONE_FLOWER);
            item1.setDiscountAmount(5);
            items.add(item1);
            items.add(createOrReturnItem(15, "A cheaper flower",
                    imageAsBytes("flower2.jpg"),
                    3, qiryatYamBranch, true, CatalogItemType.ONE_FLOWER));
            items.add(createOrReturnItem(30, "Classic Rose",
                    imageAsBytes("flower3.jpg"),
                    1, haifaUniBranch, true, CatalogItemType.ONE_FLOWER));
            items.add(createOrReturnItem(10, "Cheapest flower available",
                    imageAsBytes("flower4.jpg"),
                    5, haifaUniBranch, true, CatalogItemType.ONE_FLOWER));
            items.add(createOrReturnItem(40, "A flower in the sun (pun intended)",
                    imageAsBytes("flower5.jpg"),
                    0, haifaUniBranch, false, CatalogItemType.ONE_FLOWER));

            catalogItemsRepository.saveAll(items);
        } catch (IOException e) {
            e.printStackTrace();
        }

        done = true;
    }

    @Transactional
    Privilege createOrReturnPrivilege(String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }

        return privilege;
    }

    @Transactional
    Role createOrReturnRole(String name, Collection<Privilege> privileges) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name, privileges);
            roleRepository.save(role);
        }

        return role;
    }

    @Transactional
    Customer createOrReturnCustomer(String email, String password, Collection<Role> roles,
                                    String firstName, String lastName, String phone,
                                    long creditCard, Date expireDate, int cvv, Date dateOfBirth, List<Store> stores) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            customer = new Customer(email, new BCryptPasswordEncoder().encode(password),
                    firstName, lastName, phone, roles, creditCard, expireDate, cvv, dateOfBirth);
            customer.setStores(stores);
            customerRepository.save(customer);
        }

        return customer;
    }

    @Transactional
    Employee createOrReturnEmployee(String email, String password,
                                    String firstName, String lastName, String phone,
                                    Collection<Role> roles) {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            employee = new Employee(email, new BCryptPasswordEncoder().encode(password), firstName,
                    lastName, phone, roles, new Date());
            employeeRepository.save(employee);
        }

        return employee;
    }

    @Transactional
    Store createOrReturnStore(String name, String address, String phone, String openingHours) {
        Store store = storeRepository.findByName(name);

        if (store == null) {
            store = new Store(name, address, phone, openingHours);
            storeRepository.save(store);
        }

        return store;
    }

    @Transactional
    CatalogItem createOrReturnItem(double price, String description,
                                   byte[] picture, long availableCount, Store store, boolean canBeAssembled,
                                   CatalogItemType type) {
        CatalogItem item = catalogItemsRepository.findByStoreAndDescription(store, description);

        if (item == null) {
            item = new CatalogItem(price, description, picture, availableCount, store, canBeAssembled, type);
            catalogItemsRepository.save(item);
        }

        return item;
    }
}
