package it.enzo.me.FilmStore.backend.Film.service;

import it.enzo.me.FilmStore.backend.Film.model.Film;

import java.util.List;

public interface FilmService {

        List<Film> getAllFilms();
        String getRecentOlderYear();
        List<Film> getAllNewFilms (Integer numeroNuoviFilm);
        List<Film> getAllFilmsByName(String nome);
        List<Film> getAllFilmsByCategory(String categoria);
        Film getFilmById(String id);
        Film addFilm(Film f);
        Film deleteFilmById(String id);
        Film updateFilmById (Film nuovoFilm, String id);
//        void addListaFilms();
}
