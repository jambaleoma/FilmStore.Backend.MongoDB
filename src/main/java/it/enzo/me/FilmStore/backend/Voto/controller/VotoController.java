package it.enzo.me.FilmStore.backend.Voto.controller;

import it.enzo.me.FilmStore.backend.Voto.model.Voto;
import it.enzo.me.FilmStore.backend.Voto.service.VotoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rest/voti")
public class VotoController {

    @Autowired
    private VotoService votoService;

    @CrossOrigin
    @GetMapping(value = "/all")
    private ResponseEntity getAllVoti() {
        try {
            List<Voto> voti = this.votoService.getAllVoti();
            return ResponseEntity.status(HttpStatus.OK).header("Lista Voti","--- OK --- Lista Voti Trovata Con Successo").body(voti);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byFilm_CustomerId/{filmId}/{customerId}")
    private ResponseEntity getVotoByFilmId_Customer(@PathVariable String filmId, @PathVariable String customerId) {
        try {
            Voto votoByIdFilm = this.votoService.getVotoByFilmId_Customer(filmId, customerId);
            return ResponseEntity.status(HttpStatus.OK).header("Voto Per Film ID", "--- OK --- Voto Per Film ID Trovato Con Successo").body(votoByIdFilm);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    private ResponseEntity getVotoById(@PathVariable String id) {
        try {
            Voto votoById = this.votoService.getVotoById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Voto", "--- OK --- Voto Trovato Con Successo").body(votoById);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/insertVoto")
    public ResponseEntity insertVoto(@RequestBody Voto v) {
        try {
            votoService.addVoto(v);
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Voto", "--- OK --- Voto Creatp Con Successo").body(getAllVoti().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PutMapping(value = "/upDateVotoById/{id}")
    private ResponseEntity updateVoto(@RequestBody Voto nuovoVoto, @PathVariable String id) {
        try {
            votoService.updateVoto(nuovoVoto, id);
            return ResponseEntity.status(HttpStatus.OK).header("Aggiornamento Voto", "--- OK --- Voto Aggiornato Con Successo").body(getVotoById(id).getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteVotoById/{id}")
    private ResponseEntity deleteVotoById(@PathVariable String id) {
        try {
            votoService.deleteVotoById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Voto", "--- OK --- Voto Eliminato Con Successo").body("Il Voto con Id: " + id + " è stata Eliminato con Successo");
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteVotoByFilmId_CustomerId/{id}")
    private ResponseEntity deleteVotoByFilmId_CustomerId(@PathVariable String filmId, String customerId) {
        try {
            votoService.deleteVotoByFilmId_CustomerId(filmId, customerId);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Voto", "--- OK --- Voto Eliminato Con Successo").body("Il Voto del Film con Id: " + filmId + " è stato Eliminato con Successo");
        } catch (Exception e) {
            throw e;
        }
    }

    /*@PostMapping(value = "/insertLocalListVoi")
    public ResponseEntity addRichiesteList() {
        try {
            votoService.addListaVoti();
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Lista Voti", "--- OK --- Lista Voti Creata Con Successo").body("OK");
        } catch (Exception e) {
            throw e;
        }
    }*/
}
