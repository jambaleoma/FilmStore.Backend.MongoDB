package it.enzo.me.FilmStore.backend.Serie.repository;


import it.enzo.me.FilmStore.backend.Serie.model.Serie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface SerieRepository extends MongoRepository<Serie, String> {}