package it.enzo.me.FilmStore.backend.Stagione.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Stagioni")
@Data
public class Stagione {

    @Id
    private String id;

    private String serie_id;
    private String nome_serie;
    private Integer numeroStagione;
    private String formato;
    private Integer anno;
    private Integer numeroEpisodi;
    private String[] episodi;
    private List<String> linguaAudio;
    private List<String> linguaSottotitoli;
    private String trama;
    private String locandina;
}
