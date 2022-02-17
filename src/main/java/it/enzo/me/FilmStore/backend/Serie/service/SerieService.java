package it.enzo.me.FilmStore.backend.Serie.service;

import it.enzo.me.FilmStore.backend.Serie.model.Serie;

import java.util.List;

public interface SerieService {

    List<Serie> getAllSerie();
    List<Serie> getAllNewSerie (Integer numeroNuoveSerie);
    List<Serie> getAllSerieByName(String name);
    Serie getSerieById(String id);
    Serie addSerie(Serie s);
    Serie updateSerie (Serie nuovaSerie, String id);
    Serie deleteSerieById(String id);
//    void addListaSerie();
}
