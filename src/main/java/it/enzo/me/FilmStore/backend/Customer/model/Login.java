package it.enzo.me.FilmStore.backend.Customer.model;

import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
public class Login {
    private String username;
    private String password;
}
