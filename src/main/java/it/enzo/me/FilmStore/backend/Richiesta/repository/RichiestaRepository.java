package it.enzo.me.FilmStore.backend.Richiesta.repository;


import it.enzo.me.FilmStore.backend.Richiesta.model.Richiesta;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RichiestaRepository extends MongoRepository<Richiesta, String> {
}
