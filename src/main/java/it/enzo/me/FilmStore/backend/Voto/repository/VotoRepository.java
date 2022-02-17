package it.enzo.me.FilmStore.backend.Voto.repository;

import it.enzo.me.FilmStore.backend.Voto.model.Voto;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface VotoRepository extends MongoRepository<Voto, String> {}
