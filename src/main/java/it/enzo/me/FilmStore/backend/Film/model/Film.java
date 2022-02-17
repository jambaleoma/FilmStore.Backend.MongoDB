package it.enzo.me.FilmStore.backend.Film.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document("Films")
@Data
public class Film {

    @Id
    private String id;

    private String nome;
    private Integer anno;
    private String formato;
    private List<String> categoria;
    private List<String> linguaAudio;
    private List<String> linguaSottotitoli;
    private String trama;
    private String locandina;
    private Long dataCreazione;
}
