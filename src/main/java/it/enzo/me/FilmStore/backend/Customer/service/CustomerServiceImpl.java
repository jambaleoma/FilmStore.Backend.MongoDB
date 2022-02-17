package it.enzo.me.FilmStore.backend.Customer.service;

import it.enzo.me.FilmStore.backend.Customer.model.Customer;
import it.enzo.me.FilmStore.backend.Exception.NotFoundException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.logging.Logger;

@Controller
public class CustomerServiceImpl implements CustomerService {

    private static final Logger LOGGER = Logger.getLogger( CustomerServiceImpl.class.getName() );

    final
    MongoTemplate mongoTemplate;

    public CustomerServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Customer> getAllCustomers() {
        List<Customer> customers = (List<Customer>) mongoTemplate.findAll(Customer.class);
        if (customers.size() == 0) {
            throw new NotFoundException("Nessun Customer Trovato");
        }
        for (Customer c : customers) {
            c.setPassword("********");
        }
        StringBuilder listCustomers = new StringBuilder();
        listCustomers.append("\nLista Utenti:\n");
        for (Customer c : customers) {
            listCustomers.append("Nome: " + c.getFirstName() + " Cognome: " + c.getLastName() + "\n");
        }
        LOGGER.info(listCustomers.toString());
        return customers;
    }

    @Override
    public Customer getCustomerByName(String firstName) {
        Customer c = new Customer();
        for (Customer customer : mongoTemplate.findAll(Customer.class)) {
            if (customer.getFirstName().matches("(.*)" + firstName + "(.*)")) {
                c = customer;
                c.setPassword("********");
            }
        }
        StringBuilder listCustomer = new StringBuilder();
        listCustomer.append("\nUtente:\n");
        listCustomer.append("Nome: " + c.getFirstName() + " Cognome: " + c.getLastName() + "\n");
        LOGGER.info(listCustomer.toString());
        return c;
    }

    @Override
    public Customer getCustomerById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Customer c =  mongoTemplate.findOne(query, Customer.class);
        c.setPassword("********");
        if (c == null)
            throw new NotFoundException("Customer con id: " + id + " NON Trovato");
        StringBuilder listCustomer = new StringBuilder();
        listCustomer.append("\nUtente:\n");
        listCustomer.append("Nome: " + c.getFirstName() + " Cognome: " + c.getLastName() + "\n");
        LOGGER.info(listCustomer.toString());
        return c;
    }

    @Override
    public Customer addCustomer(Customer c) {
        c.setNumeroRichieste(0);
        return mongoTemplate.save(c);
    }

    @Override
    public Customer updateCustomer(Customer nuovoCustomer, String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Customer.class)) {
            Update update = new Update();
            update.set("firstName", nuovoCustomer.getFirstName());
            update.set("lastName", nuovoCustomer.getLastName());
            update.set("sesso", nuovoCustomer.getSesso());
            update.set("dataDiNascita", nuovoCustomer.getDataDiNascita());
            update.set("label", nuovoCustomer.getLabel());
            update.set("value", nuovoCustomer.getValue());
            update.set("numeroRichieste", nuovoCustomer.getNumeroRichieste());
            update.set("categoriePreferite", nuovoCustomer.getCategoriePreferite());
            update.set("avatar", nuovoCustomer.isAvatar());
            update.set("avatarBase64", (nuovoCustomer.getAvatarBase64()));
            update.set("admin", nuovoCustomer.isAdmin());
            mongoTemplate.updateFirst(query, update, Customer.class);
            Customer customerAggiornato = mongoTemplate.findOne(query, Customer.class);
            StringBuilder updateCustomer = new StringBuilder();
            updateCustomer.append("\nUtente Aggiornato:\n");
            updateCustomer.append("Nome: " + customerAggiornato.getFirstName() + " Cognome: " + customerAggiornato.getLastName() + "\n");
            LOGGER.info(updateCustomer.toString());
            return customerAggiornato;
        }
        else {
            throw new NotFoundException("Customer NON Aggiornato");
        }
    }

    @Override
    public Boolean changeCustomerPsw(Customer nuovoCustomer, String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Customer.class)) {
            Update update = new Update();
            update.set("password", nuovoCustomer.getPassword());
            mongoTemplate.updateFirst(query, update, Customer.class);
            if (this.getCustomerById(id).getPassword().equals(nuovoCustomer.getPassword())) {
                return true;
            } else {
                return false;
            }
        }
        else {
            throw new NotFoundException("Customer NON Aggiornato");
        }
    }

    @Override
    public Customer deleteCustomerById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Customer c = mongoTemplate.findOne(query, Customer.class);
        mongoTemplate.findAndRemove(query, Customer.class);
        StringBuilder deletedCustomer = new StringBuilder();
        deletedCustomer.append("\nUtente Eliminato:\n");
        deletedCustomer.append("Nome: " + c.getFirstName() + " Cognome: " + c.getLastName() + "\n");
        LOGGER.info(deletedCustomer.toString());
        return c;
    }

    @Override
    public Boolean loginCustomer(Customer loggingCustomer, String psw) {
        boolean login = false;
        Query query = new Query(Criteria.where("_id").is(loggingCustomer.getId()));
        Customer c = mongoTemplate.findOne(query, Customer.class);
        if (c.getPassword().equals(psw)) {
            login = true;
        }
        return login;
    }
}
