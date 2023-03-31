package it.enzo.me.FilmStore.backend.Film.service;

import it.enzo.me.FilmStore.backend.Film.model.Film;
import it.enzo.me.FilmStore.backend.Film.model.FilmPage;
import it.enzo.me.FilmStore.backend.Film.model.FilterFilm;
import org.springframework.data.domain.Page;

import java.util.List;

public interface FilmService {

        List<Film> getAllFilms();
        Page<Film> getAllPagebleFilms(FilmPage filmPage);
        String getRecentOlderYear();
        List<Film> getAllNewFilms (Integer numeroNuoviFilm);
        Page<Film> getAllFilmsByName(String nome, FilmPage filmPage);
        Page<Film> getAllFilmsByFormat(String nome, FilmPage filmPage);
        Page<Film> getAllFilmsByCategory(List<String> categories, FilmPage filmPage);
        Page<Film> getAllFilteredFilms(FilterFilm filterFilm, FilmPage filmPage);
        Integer min();
        Integer max();
        Film getFilmById(String id);
        Film addFilm(Film f);
        Film deleteFilmById(String id);
        Film updateFilmById (Film nuovoFilm, String id);
//        void addListaFilms();
}
