package it.enzo.me.FilmStore.backend.Stagione.repository;

import it.enzo.me.FilmStore.backend.Stagione.model.Stagione;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface StagioneRepository extends MongoRepository<Stagione, String> {}
