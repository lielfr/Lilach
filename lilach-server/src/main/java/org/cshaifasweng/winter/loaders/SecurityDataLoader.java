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
        Privilege ordersViewAllPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_ORDERS_VIEW_ALL);
        Privilege complaintsViewAllPrivilege = createOrReturnPrivilege(SecurityConstants.PRIVILEGE_COMPLAINTS_VIEW_ALL);
        Privilege ordersMarkAsDeliveredPrivilege =
                createOrReturnPrivilege(SecurityConstants.PRIVILEGE_ORDERS_MARK_AS_DELIVERED);

        Role adminRole = createOrReturnRole(SecurityConstants.ROLE_ADMIN, Arrays.asList(
                catalogEditPrivilege,
                usersEditPrivilege,
                complaintFilePrivilege,
                complaintHandlePrivilege,
                rolesEditPrivilege,
                ordersCreatePrivilege,
                ordersCancelPrivilege,
                reportsViewAllPrivilege,
                reportsViewPrivilege,
                ordersViewAllPrivilege,
                complaintsViewAllPrivilege,
                ordersMarkAsDeliveredPrivilege
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
                ordersCreatePrivilege,
                ordersViewAllPrivilege,
                complaintsViewAllPrivilege,
                ordersMarkAsDeliveredPrivilege
        ));

        Role storeManagerEmployeeRole = createOrReturnRole(SecurityConstants.ROLE_STORE_MANAGER, Arrays.asList(
                complaintHandlePrivilege,
                reportsViewPrivilege,
                ordersViewAllPrivilege,
                complaintsViewAllPrivilege,
                ordersMarkAsDeliveredPrivilege
        ));

        Role storeChainManagerEmployeeRole = createOrReturnRole(SecurityConstants.ROLE_STORE_CHAIN_MANAGER, Arrays.asList(
                complaintFilePrivilege,
                usersEditPrivilege,
                reportsViewAllPrivilege,
                reportsViewPrivilege,
                ordersViewAllPrivilege,
                complaintFilePrivilege,
                ordersMarkAsDeliveredPrivilege
        ));

        Store haifaUniBranch = createOrReturnStore("University of Haifa Branch", "Abba Houshy Av. 199, Haifa",
                "04-9899999", "Every day between 8AM to 8PM");

        Store qiryatYamBranch = createOrReturnStore("Qiryat Yam Branch", "Moshe Sharet 12, Qiryat Yam",
                "04-8799999", "Every day betwen 8AM to 6PM");

        Calendar customerBirth = Calendar.getInstance();
        customerBirth.set(2000, 1, 1);

        Calendar customerCreditCardExpire = Calendar.getInstance();
        customerCreditCardExpire.set(2022, 1, 1);

        Customer customer = createOrReturnCustomer("000000999", "customer@lilach.com", "moo",
                Collections.singletonList(customerRole), "Lilach", "Customer", "0509999999",
                11, customerCreditCardExpire.getTime(), 222, customerBirth.getTime(), Arrays.asList(haifaUniBranch));

        Customer customerBob = createOrReturnCustomer("000000001", "a@a.a", "a",
                Collections.singletonList(customerRole), "Bob", "Marley", "0509999999",
                11, customerCreditCardExpire.getTime(), 123, customerBirth.getTime(), Arrays.asList(haifaUniBranch));

        Customer customerNoah = createOrReturnCustomer("022200001", "Noah@gmail.com", "Noah",
                Collections.singletonList(customerRole), "Noah", "Johnson", "0509999111",
                11, customerCreditCardExpire.getTime(), 123, customerBirth.getTime(), Arrays.asList(qiryatYamBranch));

        Customer customerLiam = createOrReturnCustomer("022200001", "Liam@gmail.com", "Liam",
                Collections.singletonList(customerRole), "Liam", "Smith", "0509999111",
                11, customerCreditCardExpire.getTime(), 123, customerBirth.getTime(), Arrays.asList(haifaUniBranch));

        Customer customerWilliam = createOrReturnCustomer("022330001", "William@gmail.com", "William",
                Collections.singletonList(customerRole), "William", "Williams", "0529999111",
                11, customerCreditCardExpire.getTime(), 123, customerBirth.getTime(), Arrays.asList(haifaUniBranch));

        Employee admin = createOrReturnEmployee("000000998", "lielft@gmail.com", "AdminBaby!",
                "Liel", "Fridman", "0509999999",
                Collections.singletonList(adminRole));

        Employee admin2 = createOrReturnEmployee("000000998", "lielft@gmail.com", "ETPhoneHome",
                "Amit", "Fridman", "0508888888",
                Collections.singletonList(adminRole));

        Employee haifaUniManager = createOrReturnEmployee("000000997", "haifa.uni.manager@lilach.com", "haifarocks",
                "Aharon", "Cohen", "0500009000",
                Collections.singletonList(storeManagerEmployeeRole));
        haifaUniManager.setManagedStore(haifaUniBranch);
        haifaUniManager.setAssignedStore(haifaUniBranch);
        haifaUniBranch.setManager(haifaUniManager);

        Employee qiryatYamManager = createOrReturnEmployee("000000996", "qy.manager@lilach.com", "iloveky",
                "Lilach", "Schwartzman", "0509811999",
                Collections.singletonList(storeManagerEmployeeRole));
        qiryatYamBranch.setManager(qiryatYamManager);
        qiryatYamManager.setManagedStore(qiryatYamBranch);
        qiryatYamManager.setAssignedStore(qiryatYamBranch);

        Employee networkManager = createOrReturnEmployee("111110996", "boss@lilach.com", "boss",
                "rock", "theSmok", "0509123456",
                Collections.singletonList(storeChainManagerEmployeeRole));

        employeeRepository.save(haifaUniManager);
        employeeRepository.save(qiryatYamManager);
        storeRepository.save(haifaUniBranch);
        storeRepository.save(qiryatYamBranch);


        List<CatalogItem> items = new ArrayList<>();
        CatalogItem item1 = createOrReturnItem(25, "Just another flower",
                "flower1.jpg",
                4, qiryatYamBranch, true, CatalogItemType.ONE_FLOWER);
        item1.setDiscountAmount(5);
        items.add(item1);
        items.add(createOrReturnItem(15, "A cheaper flower",
                "flower2.jpg",
                3, qiryatYamBranch, true, CatalogItemType.ONE_FLOWER));
        items.add(createOrReturnItem(30, "Classic Rose",
                "flower3.jpg",
                1, haifaUniBranch, true, CatalogItemType.ONE_FLOWER));
        items.add(createOrReturnItem(10, "Cheapest flower available",
                "flower4.jpg",
                5, haifaUniBranch, true, CatalogItemType.ONE_FLOWER));
        items.add(createOrReturnItem(40, "A flower in the sun (pun intended)",
                "flower5.jpg",
                0, haifaUniBranch, false, CatalogItemType.ONE_FLOWER));

        catalogItemsRepository.saveAll(items);

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
    Customer createOrReturnCustomer(String misparZehut, String email, String password, Collection<Role> roles,
                                    String firstName, String lastName, String phone,
                                    long creditCard, Date expireDate, int cvv, Date dateOfBirth, List<Store> stores) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            customer = new Customer(misparZehut, email, new BCryptPasswordEncoder().encode(password),
                    firstName, lastName, phone, roles, creditCard, expireDate, cvv, dateOfBirth);
            customer.setStores(stores);
            customerRepository.save(customer);
        }

        return customer;
    }

    @Transactional
    Employee createOrReturnEmployee(String misparZehut, String email, String password,
                                    String firstName, String lastName, String phone,
                                    Collection<Role> roles) {
        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            employee = new Employee(misparZehut, email, new BCryptPasswordEncoder().encode(password), firstName,
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
                                   String picture, long availableCount, Store store, boolean canBeAssembled,
                                   CatalogItemType type) {
        CatalogItem item = catalogItemsRepository.findByStoreAndDescription(store, description);

        if (item == null) {
            item = new CatalogItem(price, description, picture, availableCount, store, canBeAssembled, type);
            catalogItemsRepository.save(item);
        }

        return item;
    }
}
