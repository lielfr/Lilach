package org.cshaifasweng.winter.loaders;

import org.cshaifasweng.winter.da.CustomerRepository;
import org.cshaifasweng.winter.da.EmployeeRepository;
import org.cshaifasweng.winter.da.PrivilegeRepository;
import org.cshaifasweng.winter.da.RoleRepository;
import org.cshaifasweng.winter.models.*;
import org.cshaifasweng.winter.security.SecurityConstants;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

@Component
public class SecurityDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    boolean done = false; // Make sure we don't run this more than once

    private final PrivilegeRepository privilegeRepository;

    private final RoleRepository roleRepository;

    private final CustomerRepository customerRepository;

    private final EmployeeRepository employeeRepository;

    public SecurityDataLoader(PrivilegeRepository privilegeRepository, RoleRepository roleRepository, CustomerRepository customerRepository, EmployeeRepository employeeRepository) {
        this.privilegeRepository = privilegeRepository;
        this.roleRepository = roleRepository;
        this.customerRepository = customerRepository;
        this.employeeRepository = employeeRepository;
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

        Role adminRole = createOrReturnRole("ROLE_ADMIN", Arrays.asList(
                catalogEditPrivilege,
                usersEditPrivilege,
                complaintFilePrivilege,
                complaintHandlePrivilege,
                rolesEditPrivilege,
                ordersCreatePrivilege,
                ordersCancelPrivilege
        ));

        Role customerRole = createOrReturnRole("ROLE_CUSTOMER", Arrays.asList(
                complaintFilePrivilege,
                ordersCreatePrivilege,
                ordersCancelPrivilege
        ));

        Role customerServiceEmployeeRole = createOrReturnRole("ROLE_CUSTOMER_SERIVCE_EMPLOYEE", Arrays.asList(
            complaintHandlePrivilege
        ));



        Customer customer = createOrReturnCustomer("customer@lilach.com", "moo",
                Collections.singletonList(customerRole),"Lilach", "Customer", "0509999999",
                11);

        Employee admin = createOrReturnEmployee("lielft@gmail.com", "AdminBaby!",
                "Liel", "Fridman", "0509999999",
                Collections.singletonList(adminRole));


        done = true;
    }

    @Transactional
    Privilege createOrReturnPrivilege (String name) {
        Privilege privilege = privilegeRepository.findByName(name);
        if (privilege == null) {
            privilege = new Privilege(name);
            privilegeRepository.save(privilege);
        }

        return privilege;
    }

    @Transactional
    Role createOrReturnRole (String name, Collection<Privilege> privileges) {
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
                                    long creditCard) {
        Customer customer = customerRepository.findByEmail(email);
        if (customer == null) {
            customer = new Customer(email, new BCryptPasswordEncoder().encode(password),
                    firstName, lastName, phone, roles, creditCard);
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
                    lastName, phone, roles);
            employeeRepository.save(employee);
        }

        return employee;
    }
}
