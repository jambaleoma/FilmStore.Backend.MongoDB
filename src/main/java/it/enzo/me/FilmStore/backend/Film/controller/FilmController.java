package it.enzo.me.FilmStore.backend.Film.controller;

import it.enzo.me.FilmStore.backend.Film.model.Film;
import it.enzo.me.FilmStore.backend.Film.model.FilmPage;
import it.enzo.me.FilmStore.backend.Film.model.FilterFilm;
import it.enzo.me.FilmStore.backend.Film.service.FilmService;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.codec.binary.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/rest/films")
public class FilmController {

    @Autowired
    private FilmService filmsService;

    @CrossOrigin
    @GetMapping(value = "/all")
    private ResponseEntity getAllFilms() {
        try {
            List<Film> films = this.filmsService.getAllFilms();
            return ResponseEntity.status(HttpStatus.OK).header("Lista Film", "--- OK --- Lista Film Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/pageAll")
    private ResponseEntity<Page<Film>> getAllPagebleFilms(FilmPage filmPage) {
        try {
            Page<Film> films = this.filmsService.getAllPagebleFilms(filmPage);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Film", "--- OK --- Lista Film Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/recentOlderYear")
    private ResponseEntity getRecentOlderYear() {
        try {
            String recentOlderYear = this.filmsService.getRecentOlderYear();
            return ResponseEntity.status(HttpStatus.OK).header("Anno minore e maggiore", "--- OK --- Anni Trovati Con Successo").body(recentOlderYear);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/allNewFilms/{numeroNuoviFilm}")
    private ResponseEntity getAllNewFilms(@PathVariable String numeroNuoviFilm) {
        try {
            List<Film> films = this.filmsService.getAllNewFilms(Integer.parseInt(numeroNuoviFilm));
            return ResponseEntity.status(HttpStatus.OK).header("Lista Nuovi Film", "--- OK --- Lista Nuovi Film Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byName/{nome}")
    private ResponseEntity getAllFilmsByName(@PathVariable String nome, FilmPage filmPage) {
        try {
            Page<Film> films = this.filmsService.getAllFilmsByName(nome, filmPage);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Films per Nome", "--- OK --- Lista Films per Nome Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byFormat/{format}")
    private ResponseEntity getAllFilmsByFormat(@PathVariable String format, FilmPage filmPage) {
        try {
            Page<Film> films = this.filmsService.getAllFilmsByFormat(format, filmPage);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Films per Nome", "--- OK --- Lista Films per Nome Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/byCategory")
    private ResponseEntity getFilmsByCategory(@RequestBody List<String> categories, FilmPage filmPage) {
        try {
            Page<Film> films = this.filmsService.getAllFilmsByCategory(categories, filmPage);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Films per Categoria", "--- OK --- Lista Films per Categoria Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/byYear/{year}")
    private ResponseEntity getFilmsByYear(@PathVariable Integer year, FilmPage filmPage) {
        try {
            Page<Film> films = this.filmsService.getAllFilmsByYear(year, filmPage);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Films per Categoria", "--- OK --- Lista Films per Categoria Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/allFilteredFilms")
    private ResponseEntity getAllFilteredFilms(@RequestBody FilterFilm filterFilm, FilmPage filmPage) {
        try {
            Page<Film> films = this.filmsService.getAllFilteredFilms(filterFilm, filmPage);
            return ResponseEntity.status(HttpStatus.OK).header("Lista Films per Categoria", "--- OK --- Lista Films per Categoria Trovata Con Successo").body(films);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @GetMapping(value = "/{id}")
    private ResponseEntity getFilmById(@PathVariable String id) {
        try {
            Film filmById = this.filmsService.getFilmById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Ricerca Film", "--- OK --- Film Trovato Con Successo").body(filmById);
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PostMapping(value = "/insertFilm")
    public ResponseEntity addFilm(@RequestBody Film f) {
        try {
            filmsService.addFilm(f);
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Film", "--- OK --- Film Creato Con Successo").body(getAllFilms().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @PutMapping(value = "/upDateFilmById/{id}")
    private ResponseEntity upDateFilmById(@RequestBody Film nuovoFilm, @PathVariable String id) {
        try {
            filmsService.updateFilmById(nuovoFilm, id);
            return ResponseEntity.status(HttpStatus.OK).header("Aggiornamento Film", "--- OK --- Film Aggiornato Con Successo").body(getAllFilms().getBody());
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin
    @DeleteMapping(value = "/deleteFilmById/{id}")
    private ResponseEntity deleteFilmById(@PathVariable String id) {
        try {
            filmsService.deleteFilmById(id);
            return ResponseEntity.status(HttpStatus.OK).header("Eliminazione Film", "--- OK --- Film Eliminato Con Successo").body("Il Film con Id: " + id + " è stato Eliminato con Successo");
        } catch (Exception e) {
            throw e;
        }
    }

    @CrossOrigin(origins = "*")
    @PostMapping(value = "/locandina/saveLocandinaImage/{filmId}")
    private ResponseEntity saveFilmLocandinaImage(@RequestParam("filmLocandina") MultipartFile file, @PathVariable String filmId) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).header("Locandina Film", "Non è stato trovato nessun File da caricare").body("Errore");
        }
        try {
            byte[] bytes = file.getBytes();
            StringBuilder sb = new StringBuilder();
            sb.append("data:image/png;base64,");
            sb.append(StringUtils.newStringUtf8(Base64.encodeBase64(bytes, false)));
            Film filmToAddLocandina = filmsService.getFilmById(filmId);
            filmToAddLocandina.setLocandina(sb.toString());
            filmsService.updateFilmById(filmToAddLocandina, filmId);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).header("Locandina Film", "Locandina Film Aggiornata con Successo").body("OK");
    }

   /* @PostMapping(value = "/insertLocalListFilm")
    public ResponseEntity addFilmsList() {
        try {
            filmsService.addListaFilms();
            return ResponseEntity.status(HttpStatus.CREATED).header("Creazione Lista Film", "--- OK --- Lista Film Creata Con Successo").body("OK");
        } catch (Exception e) {
            throw e;
        }
    }*/

}
