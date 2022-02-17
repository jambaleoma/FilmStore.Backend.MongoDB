package it.enzo.me.FilmStore.backend.Serie.controller;

import it.enzo.me.FilmStore.backend.Serie.model.Serie;
import it.enzo.me.FilmStore.backend.Serie.service.SerieService;
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
@RequestMapping("/rest/serie")
public class SerieController {

    @Autowired
    private SerieService serieService;

    @CrossOrigin
    @GetMapping(value = "/all")
    private ResponseEntity getAllSerie() {
        try {
            List<Serie> serie = this.serieService.getAllSerie();
            return ResponseEntity.status(HttpStatus.OK).header("Lista Serie","--- OK --- Lista Serie Trovata Con Successo").body(serie);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/allNewSerie/{numeroNuoveSerie}")
    private ResponseEntity getAllNewSerie(@PathVariable String numeroNuoveSerie) {
        try {
            List<Serie> serie = this.serieService.getAllNewSerie(Integer.parseInt(numeroNuoveSerie));
            return ResponseEntity.status(HttpStatus.OK).header("Lista Nuove Serie", "--- OK --- Lista Nuove Serie Trovata Con Successo").body(serie);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byName/{name}")
    private ResponseEntity getAllSerieByName(@PathVariable String name) {
        try {
            List<Serie> serieByValue = this.serieService.getAllSerieByName(name);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Serie Per Nome", "--- OK --- Lista Serie Per Nome Trovata Con Successo").body(serieByValue);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    private ResponseEntity getSerieById(@PathVariable String id) {
        try {
            Serie serieById = this.serieService.getSerieById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Serie", "--- OK --- Serie Trovato Con Successo").body(serieById);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/insertSerie")
    public ResponseEntity addSerie(@RequestBody Serie g) {
        try {
            serieService.addSerie(g);
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Serie", "--- OK --- Serie Creata Con Successo").body(getAllSerie().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PutMapping(value = "/upDateSerieById/{id}")
    private ResponseEntity updateSerie(@RequestBody Serie nuovaSerie, @PathVariable String id) {
        try {
            Serie serieAggiornata = serieService.updateSerie(nuovaSerie, id);
            return ResponseEntity.status(HttpStatus.OK).header("Aggiornamento Serie", "--- OK --- Serie Aggiornata Con Successo").body(getAllSerie().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteSerieById/{id}")
    private ResponseEntity deleteSerieById(@PathVariable String id) {
        try {
            serieService.deleteSerieById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Serie", "--- OK --- Serie Eliminata Con Successo").body("La Serie con Id: " + id + " è stata Eliminata con Successo");
        } catch (Exception e) {
            throw e;
        }
    }


    @CrossOrigin(origins = "*")
    @PostMapping(value = "/locandina/saveLocandinaImage/{serieId}")
    private ResponseEntity saveSerieLocandinaImage(@RequestParam("serieLocandina") MultipartFile file, @PathVariable String serieId) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Locandina Serie", "Non è stato trovato nessun File da caricare").body("Errore");
        }
        try {
            byte[] bytes = file.getBytes();
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
            Serie serieToAddLocandina = serieService.getSerieById(serieId);
            serieToAddLocandina.setLocandina(sb.toString());
            serieService.updateSerie(serieToAddLocandina, serieId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).header("Locandina Serie", "Locandina Serie Aggiornata con Successo").body("OK");
    }

    /*@CrossOrigin
    @PostMapping(value = "/insertLocalListSerie")
    public ResponseEntity addSerieLista() {
        try {
            serieService.addListaSerie();
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Lista Serie", "--- OK --- Lista Serie Creata Con Successo").body("OK");
        } catch (Exception e) {
            throw e;
        }
    }*/

}