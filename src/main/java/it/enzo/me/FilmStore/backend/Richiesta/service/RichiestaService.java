package it.enzo.me.FilmStore.backend.Richiesta.service;

import it.enzo.me.FilmStore.backend.Richiesta.model.Richiesta;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface RichiestaService {
    List<Richiesta> getAllRichieste();
    List<Richiesta> getAllRichiesteByNomeCliente(String nomeCliente);
    Richiesta getRichiestaById(String id);
    Richiesta addRichiesta(Richiesta r);
    Richiesta updateRichiesta(Richiesta nuovaRichiesta, String id);
    Richiesta deleteRichiestaById(String id);
    Map<String, Map<String, Integer>> getRichiesteForStatisticsMethod();
    ArrayList getRichiesteYearForStatistiche();
    ArrayList getRichiesteForStatistiche(String year);
//    void addListaRichieste();
}
