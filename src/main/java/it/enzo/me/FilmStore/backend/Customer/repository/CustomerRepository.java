package it.enzo.me.FilmStore.backend.Customer.repository;

import it.enzo.me.FilmStore.backend.Customer.model.Customer;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends MongoRepository<Customer, String> {

}