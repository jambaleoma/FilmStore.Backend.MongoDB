package it.enzo.me.FilmStore.backend.Voto.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Voti")
@Data
public class Voto {

    @Id
    private String id;

    private String idFilm;
    private String nomeFilm;
    private String idCustomer;
    private String firstNameCustomer;
    private String lastNameCustomer;
    private String dataCreazioneVoto;
    private Integer votazione;
    private Boolean like;

}
