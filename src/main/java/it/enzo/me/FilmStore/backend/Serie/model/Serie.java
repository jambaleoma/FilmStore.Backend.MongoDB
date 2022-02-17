package it.enzo.me.FilmStore.backend.Serie.model;

import it.enzo.me.FilmStore.backend.Stagione.model.Stagione;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Serie")
@Data
public class Serie {

    @Id
    private String _id;

    private String nome;
    private String locandina;
    private List<Stagione> stagioni;
    private Long dataCreazione;
}
