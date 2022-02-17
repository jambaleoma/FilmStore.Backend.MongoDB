package it.enzo.me.FilmStore.backend.Film.repository;

import it.enzo.me.FilmStore.backend.Film.model.Film;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;


public interface FilmRepository extends MongoRepository<Film, String> {

    @Query("SELECT COUNT(*) AS count FROM #{#n1ql.bucket} WHERE #{#n1ql.filter} and formato = $1")
    List<Film> getFilmByFormatoQuery(String formato);
}