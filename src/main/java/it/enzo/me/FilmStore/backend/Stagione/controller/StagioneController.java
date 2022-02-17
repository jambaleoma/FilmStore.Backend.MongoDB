package it.enzo.me.FilmStore.backend.Stagione.controller;

import it.enzo.me.FilmStore.backend.Stagione.model.Stagione;
import it.enzo.me.FilmStore.backend.Stagione.service.StagioneService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/stagioni")
public class StagioneController {

    @Autowired
    private StagioneService stagioneService;

    @CrossOrigin
    @GetMapping(value = "/all")
    private ResponseEntity getAllStagioni() {
        try {
            List<Stagione> stagioni = this.stagioneService.getAllStagioni();
            return ResponseEntity.status(HttpStatus.OK).header("Lista Stagioni","--- OK --- Lista Stagioni Trovata Con Successo").body(stagioni);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/bySerieId/{serieId}")
    private ResponseEntity getAllStagioniByIdSerie(@PathVariable String serieId) {
        try {
            List<Stagione> stagioniByIdSerie = this.stagioneService.getAllStagioniByIdSerie(serieId);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Stagioni Per Serie ID", "--- OK --- Lista Stagioni Per Serie ID Trovata Con Successo").body(stagioniByIdSerie);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    private ResponseEntity getStagioneById(@PathVariable String id) {
        try {
            Stagione stagioneById = this.stagioneService.getStagioneById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Stagione", "--- OK --- Stagione Trovata Con Successo").body(stagioneById);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/insertStagione")
    public ResponseEntity addStagione(@RequestBody Stagione s) {
        try {
            stagioneService.addStagione(s);
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Stagione", "--- OK --- Stagione Creata Con Successo").body(getAllStagioniByIdSerie(s.getSerie_id()).getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PutMapping(value = "/upDateStagioneById/{id}")
    private ResponseEntity updateStagione(@RequestBody Stagione nuovaStagione, @PathVariable String id) {
        try {
            stagioneService.updateStagione(nuovaStagione, id);
            return ResponseEntity.status(HttpStatus.OK).header("Aggiornamento Stagione", "--- OK --- Stagione Aggiornata Con Successo").body(getAllStagioniByIdSerie(nuovaStagione.getSerie_id()).getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteStagioneById/{id}")
    private ResponseEntity deleteStagioneById(@PathVariable String id) {
        try {
            stagioneService.deleteStagioneById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Stagione", "--- OK --- Stagione Eliminata Con Successo").body("La Stagione con Id: " + id + " è stata Eliminata con Successo");
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteStagioniBySerieId/{id}")
    private ResponseEntity deleteStagioniBySerieId(@PathVariable String id) {
        try {
            stagioneService.deleteStagioniBySerieId(id);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Stagioni", "--- OK --- Stagioni Eliminate Con Successo").body("Le Stagioni della Serie con Id: " + id + " sono state Eliminate con Successo");
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/locandina/saveLocandinaImage/{stagioneId}")
    private ResponseEntity saveCustomerImage(@RequestParam("stagioneLocandina") MultipartFile file, @PathVariable String stagioneId) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Locandina Stagione", "Non è stato trovato nessun File da caricare").body("Errore");
        }
        try {
            byte[] bytes = file.getBytes();
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
            Stagione stagioneToAddLocandina = stagioneService.getStagioneById(stagioneId);
            stagioneToAddLocandina.setLocandina(sb.toString());
            stagioneService.updateStagione(stagioneToAddLocandina, stagioneId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).header("Locandina Stagione", "Locandina Stagione Aggiornata con Successo").body("OK");
    }

    /*@PostMapping(value = "/insertLocalListStagione")
    public ResponseEntity addStagioneList() {
        try {
            stagioneService.addListaStagioni();
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Lista Stagioni", "--- OK --- Lista Stagioni Creata Con Successo").body("OK");
        } catch (Exception e) {
            throw e;
        }
    }*/
}
