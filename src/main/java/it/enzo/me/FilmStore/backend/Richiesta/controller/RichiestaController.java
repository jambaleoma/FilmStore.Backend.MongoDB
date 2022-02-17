package it.enzo.me.FilmStore.backend.Richiesta.controller;

import it.enzo.me.FilmStore.backend.Richiesta.model.Richiesta;
import it.enzo.me.FilmStore.backend.Richiesta.service.RichiestaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/rest/richieste")
public class RichiestaController {

    @Autowired
    private RichiestaService richiestaService;

    @CrossOrigin
    @GetMapping(value = "/all")
    private ResponseEntity getAllRichieste() {
        try {
            List<Richiesta> richieste = this.richiestaService.getAllRichieste();
            return ResponseEntity.status(HttpStatus.OK).header("Lista Richieste", "--- OK --- Lista Richieste Trovata Con Successo").body(richieste);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    private ResponseEntity getRichiestaById(@PathVariable String id) {
        try {
            Richiesta richiestaById = this.richiestaService.getRichiestaById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Richiesta", "--- OK --- Richiesta Trovata Con Successo").body(richiestaById);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byNomeCliente/{nomeCliente}")
    private ResponseEntity getRichiesteByNomeCliente(@PathVariable String nomeCliente) {
        try {
            List<Richiesta> richiesteByNomeCliente = this.richiestaService.getAllRichiesteByNomeCliente(nomeCliente);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Richieste", "--- OK --- Richieste Trovate Con Successo").body(richiesteByNomeCliente);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/insertRichiesta")
    public ResponseEntity addRichiesta(@RequestBody Richiesta r) {
        try {
            Richiesta richiestaDaSalvare = this.richiestaService.addRichiesta(r);
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Richiesta", "--- OK --- Richiesta Creata Con Successo").body(getAllRichieste().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PutMapping(value = "/upDateRichiesta/{id}")
    private ResponseEntity updateRichiesta (@RequestBody Richiesta nuovaRichiesta, @PathVariable String id) {
        try {
            if (nuovaRichiesta.getId() != null) {
                nuovaRichiesta.setId(null);
            }
            Richiesta richiestaAggiornata = richiestaService.updateRichiesta(nuovaRichiesta, id);
            return ResponseEntity.status(HttpStatus.OK).header("Aggiornamento Richiesta", "--- OK --- Richiesta Aggiornata Con Successo").body(getAllRichieste().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteRichiestaById/{id}")
    private ResponseEntity deleteRichiestaById(@PathVariable String id) {
        try {
            richiestaService.deleteRichiestaById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Richiesta", "--- OK --- Richiesta Eliminata Con Successo").body("La Richiesta con Id: " + id + " è stato Eliminata con Successo");
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/statistics/richieste2Years/{year}")
    private ResponseEntity getRichiesta2Statistics(@PathVariable String year) {
        try {
            ArrayList richieste2Year = this.richiestaService.getRichiesteForStatistiche(year);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Richieste Per fini Statistici", "--- OK --- Richieste Trovate Con Successo").body(richieste2Year);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/statistics/years")
    private ResponseEntity getStatisticsYears() {
        try {
            ArrayList statisticsYears = this.richiestaService.getRichiesteYearForStatistiche();
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Anni Richieste Per fini Statistici", "--- OK --- Anni Richieste Trovati Con Successo").body(statisticsYears);
        } catch (Exception e) {
            throw e;
        }
    }

    /*@PostMapping(value = "/insertLocalListRichieste")
    public ResponseEntity addRichiesteList() {
        try {
            richiestaService.addListaRichieste();
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Lista Richieste", "--- OK --- Lista Richieste Creata Con Successo").body("OK");
        } catch (Exception e) {
            throw e;
        }
    }*/

}
