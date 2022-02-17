package it.enzo.me.FilmStore.backend.Customer.model;

import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Document("Customers")
@Data
public class Customer {

    @Id
    private String id;

    private String firstName;
    private String lastName;
    private String sesso;
    private String dataDiNascita;
    private String password;
    private String label;
    private String value;
    private long numeroRichieste;
    private String[] categoriePreferite;
    private boolean avatar;
    private String avatarBase64;
    private boolean admin;
}
