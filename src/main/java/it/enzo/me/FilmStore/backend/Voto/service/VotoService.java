package it.enzo.me.FilmStore.backend.Voto.service;

import it.enzo.me.FilmStore.backend.Voto.model.Voto;

import java.util.List;

public interface VotoService {

    List<Voto> getAllVoti();
    Voto getVotoByFilmId_Customer(String idFilm, String idCustomer);
    Voto getVotoById(String id);
    Voto addVoto(Voto v);
    Voto updateVoto (Voto nuovoVoto, String id);
    Voto deleteVotoById(String id);
    void deleteVotoByFilmId_CustomerId(String filmId, String customerId);
//    void addListaVoti();
}
