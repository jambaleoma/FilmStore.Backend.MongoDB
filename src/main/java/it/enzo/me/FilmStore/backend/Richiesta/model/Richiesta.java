package it.enzo.me.FilmStore.backend.Richiesta.model;


import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("Richieste")
@Data
public class Richiesta {

    @Id
    private String id;

    private String titoloFilmRichiesto;
    private String formatoFilmRichiesto;
    private String dataInserimento;
    private String nomeCliente;
    private String stato;
    private String note;
}
