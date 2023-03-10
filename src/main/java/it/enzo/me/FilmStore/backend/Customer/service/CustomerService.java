package it.enzo.me.FilmStore.backend.Customer.service;

import it.enzo.me.FilmStore.backend.Customer.model.Customer;

import java.util.List;

public interface CustomerService {

    List<Customer> getAllCustomers();
    Customer getCustomerByName(String name);
    Customer getCustomerById(String id);
    Customer addCustomer(Customer c);
    Customer updateCustomer(Customer nuovoCustomer, String id);
    Boolean changeCustomerPsw(Customer nuovoCustomer, String id);
    Customer deleteCustomerById(String id);
    Boolean loginCustomer(Customer loggingCustomer, String psw);

}