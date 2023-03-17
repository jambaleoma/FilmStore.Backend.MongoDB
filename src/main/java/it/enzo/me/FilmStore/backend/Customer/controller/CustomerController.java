package it.enzo.me.FilmStore.backend.Customer.controller;

import it.enzo.me.FilmStore.backend.Customer.model.Customer;
import it.enzo.me.FilmStore.backend.Customer.model.Login;
import it.enzo.me.FilmStore.backend.Customer.service.CustomerService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @CrossOrigin
    @GetMapping(value = "/all")
    private ResponseEntity getAllCustomers() {
        try {
            List<Customer> customers = this.customerService.getAllCustomers();
            return ResponseEntity.status(HttpStatus.OK).header("Lista Customers", "--- OK --- Lista Customers Trovata Con Successo").body(customers);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byName/{name}")
    private ResponseEntity getCustomerByName(@PathVariable String name) {
        try {
            Customer customers = this.customerService.getCustomerByName(name);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Customers per Nome", "--- OK --- Lista Customers per Nome Trovata Con Successo").body(customers);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    private ResponseEntity getCustomerById(@PathVariable String id) {
        try {
            Customer customerById = this.customerService.getCustomerById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Customer", "--- OK --- Customer Trovato Con Successo").body(customerById);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/insertCustomer")
    public ResponseEntity addCustomer(@RequestBody Customer c) {
        try {
            Customer customerSalvato = customerService.addCustomer(c);
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Customer", "--- OK --- Customer Creato Con Successo").body(getAllCustomers().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PutMapping(value = "/upDateCustomer/{id}")
    private ResponseEntity updateCustomer (@RequestBody Customer nuovoCustomer, @PathVariable String id) {
        try {
            Customer customerAggiornato = customerService.updateCustomer(nuovoCustomer, id);
            return ResponseEntity.status(HttpStatus.OK).header("Aggiornamento Customer", "--- OK --- Customer Aggiornato Con Successo").body(getAllCustomers().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PutMapping(value = "/changeCustomerPsw/{id}")
    private ResponseEntity changeCustomerPsw (@RequestBody Customer nuovoCustomer, @PathVariable String id) {
        try {
            Boolean aggiornato = customerService.changeCustomerPsw(nuovoCustomer, id);
            return ResponseEntity.status(HttpStatus.OK).header("Aggiornamento Customer", "--- OK --- Customer Aggiornato Con Successo").body(getAllCustomers().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteCustomerById/{id}")
    private ResponseEntity deleteCustomerById(@PathVariable String id) {
        try {
            customerService.deleteCustomerById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Customer", "--- OK --- Customer Eliminato Con Successo").body("Il Customer con Id: " + id + " è stato Eliminato con Successo");
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/loginCustomer")
    private ResponseEntity loginCustomer(@RequestBody Login loggingCustomer) {
        try {
            Customer login = customerService.loginCustomer(loggingCustomer);
            return ResponseEntity.status(HttpStatus.OK).header("Customer Loggato con Successo", "--- OK --- Il Customers è stato Loggato con Successo").body(login);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/avatar/saveCustomerImage/{customerId}")
    private ResponseEntity saveCustomerImage(@RequestParam("customerAvatar") MultipartFile file, @PathVariable String customerId) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Avatar Customer", "Non è stato trovato nessun File da caricare").body("Errore");
        }
        try {
            byte[] bytes = file.getBytes();
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
            Customer customerToAddAvatar = customerService.getCustomerById(customerId);
            customerToAddAvatar.setAvatarBase64(sb.toString());
            customerToAddAvatar.setAvatar(true);
            customerService.updateCustomer(customerToAddAvatar, customerId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).header("Avatar Customer", "Avatar Customer Aggirnato con Successo").body("OK");
    }

}