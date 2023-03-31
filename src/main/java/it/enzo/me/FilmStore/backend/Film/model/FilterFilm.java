package it.enzo.me.FilmStore.backend.Film.model;

import lombok.Data;

import java.util.List;

@Data
public class FilterFilm {
    private String nomeFilm;
    private String formatoFilm;
    private List<String> categorieFilm;
}
