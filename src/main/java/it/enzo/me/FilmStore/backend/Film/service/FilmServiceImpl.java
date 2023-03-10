package it.enzo.me.FilmStore.backend.Film.service;

import it.enzo.me.FilmStore.backend.Customer.model.Customer;
import it.enzo.me.FilmStore.backend.Customer.service.CustomerServiceImpl;
import it.enzo.me.FilmStore.backend.Exception.NotFoundException;
import it.enzo.me.FilmStore.backend.Film.model.Film;
import it.enzo.me.FilmStore.backend.Film.repository.FilmRepository;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class FilmServiceImpl implements FilmService {

    private static final Logger LOGGER = Logger.getLogger( CustomerServiceImpl.class.getName() );

    final
    MongoTemplate mongoTemplate;

    public FilmServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Film> getAllFilms() {
        List<Film> films = (List<Film>) mongoTemplate.findAll(Film.class);
        if (films == null) {
            throw new NotFoundException("Nessun Film Trovato");
        }
        StringBuilder listFilms = new StringBuilder();
        listFilms.append("\nRichiesta elenco totale Film:\n");
        listFilms.append("\nTrovati " + films.size() + " Film\n");
        LOGGER.info(listFilms.toString());
        return films;
    }

    @Override
    public String getRecentOlderYear() {
        String recentOlderYear = "";
        int recentYear = 0;
        int olderYear = Calendar.getInstance().get(Calendar.YEAR);
        for (Film f : mongoTemplate.findAll(Film.class)) {
            if (f.getAnno() < olderYear) {
                olderYear = f.getAnno();
            }
            if (f.getAnno() > recentYear) {
                recentYear = f.getAnno();
            }
            recentOlderYear = olderYear + "-" + recentYear;
        }
        StringBuilder recentOlderYearFilm = new StringBuilder();
        recentOlderYearFilm.append("\nRichiesta anno film meno recente e pi?? recente:\n");
        recentOlderYearFilm.append("\nAnno meno recente: " + olderYear + " Anno pi?? recente: " + recentYear +"\n");
        LOGGER.info(recentOlderYearFilm.toString());
        return recentOlderYear;
    }

    @Override
    public List<Film> getAllNewFilms(Integer numeroNuoviFilm) {
        List<Film> allNewFilms = this.getAllFilms();
        Collections.sort(allNewFilms, new DataCreazioneComparatore());
        allNewFilms = allNewFilms.subList(0, numeroNuoviFilm);
        StringBuilder listNewFilms = new StringBuilder();
        listNewFilms.append("\nRichiesta elenco " + numeroNuoviFilm + " Film pi?? recenti:\n");
        listNewFilms.append("\nTrovati " + allNewFilms.size() + " Film\n");
        listNewFilms.append("\nElenco " + numeroNuoviFilm + " Film pi?? recenti:\n");
        for (Film f : allNewFilms) {
            listNewFilms.append("Nome: " + f.getNome() + " Anno: " + f.getAnno() + " Data Creazione: " + f.getDataCreazione() + "\n");
        }
        LOGGER.info(listNewFilms.toString());
        return allNewFilms;
    }

    @Override
    public List<Film> getAllFilmsByName(String nome) {
        List<Film> films = new ArrayList<>();
        for (Film f : mongoTemplate.findAll(Film.class)) {
            if (f.getNome().matches("(.*)" + nome + "(.*)"))
                films.add(f);
        }
        if (films.size() == 0) {
            throw new NotFoundException("Nessun Film con Nome: " + nome + " ?? stato Trovato");
        }
        StringBuilder listFilmsByName = new StringBuilder();
        listFilmsByName.append("\nRichiesta elenco Film filtrati per Nome '" + nome + "':\n");
        listFilmsByName.append("\nTrovati " + films.size() + " Film\n");
        listFilmsByName.append("\nElenco Film filtrati per Nome '" + nome + "':\n\n");
        for (Film f : films) {
            listFilmsByName.append("Nome: " + f.getNome() + " Anno: " + f.getAnno() + " Data Creazione: " + f.getDataCreazione() + "\n");
        }
        LOGGER.info(listFilmsByName.toString());
        return films;
    }

    @Override
    public List<Film> getAllFilmsByCategory(String categoria) {
        List<Film> films = new ArrayList<>();
        for (Film f : mongoTemplate.findAll(Film.class)) {
            for(int i = 0; i < f.getCategoria().size(); i++) {
                if (i > 3)
                    break;
                if (f.getCategoria().get(i).equals(categoria))
                    films.add(f);
            }
        }
        if (films.size() == 0) {
            throw new NotFoundException("Nessun Film con categoria: " + categoria + " ?? stato Trovato");
        }
        StringBuilder listFilmsByCategory = new StringBuilder();
        listFilmsByCategory.append("\nRichiesta elenco Film filtrati Per Categoria '" + categoria + "':\n");
        listFilmsByCategory.append("\nTrovati " + films.size() + " Film\n");
        listFilmsByCategory.append("\nElenco Film filtrati Per Categoria '" + categoria + "':\n\n");
        for (Film f : films) {
            listFilmsByCategory.append("Nome: " + f.getNome() + " Anno: " + f.getAnno() + " Data Creazione: " + f.getDataCreazione() + "\n");
        }
        LOGGER.info(listFilmsByCategory.toString());
        return films;
    }

    @Override
    public Film getFilmById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Film f =  mongoTemplate.findOne(query, Film.class);
        if (f == null)
            throw new NotFoundException("Film con id: " + id + " NON Trovato");
        StringBuilder filmById = new StringBuilder();
        filmById.append("\nRichiesta Film filtrando Per Id '" + id + "':\n");
        filmById.append("Nome: " + f.getNome() + " Anno: " + f.getAnno() + " Data Creazione: " + f.getDataCreazione() + "\n");
        LOGGER.info(filmById.toString());
        return f;
    }

    @Override
    public Film addFilm(Film f) {
        String newDateFilm = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
        f.setDataCreazione(Long.parseLong(newDateFilm));
        StringBuilder saveFilm = new StringBuilder();
        saveFilm.append("\nRichiesta Salvataggio Film:\n");
        saveFilm.append("Nome: " + f.getNome() + " Anno: " + f.getAnno() + " Data Creazione: " + f.getDataCreazione() + "\n");
        LOGGER.info(saveFilm.toString());
        return mongoTemplate.save(f);
    }

    @Override
    public Film updateFilmById(Film nuovoFilm, String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Film.class)) {
            Update update = new Update();
            update.set("nome", nuovoFilm.getNome());
            update.set("anno", nuovoFilm.getAnno());
            update.set("formato", nuovoFilm.getFormato());
            update.set("categoria", nuovoFilm.getCategoria());
            update.set("linguaAudio", nuovoFilm.getLinguaAudio());
            update.set("linguaSottotitoli", nuovoFilm.getLinguaSottotitoli());
            update.set("trama", nuovoFilm.getTrama());
            update.set("locandina", nuovoFilm.getLocandina());
            update.set("dataCreazione", nuovoFilm.getDataCreazione());
            mongoTemplate.updateFirst(query, update, Film.class);
            Film filmAggiornato = mongoTemplate.findOne(query, Film.class);
            StringBuilder updateFilm = new StringBuilder();
            updateFilm.append("\nRichiesta Aggiornamento Film:\n");
            updateFilm.append("\nFilm Aggiornato:\n");
            updateFilm.append("Nome: " + filmAggiornato.getNome() + " Anno: " + filmAggiornato.getAnno() + " Data creazione: " + filmAggiornato.getDataCreazione() + "\n");
            LOGGER.info(updateFilm.toString());
            return filmAggiornato;
        } else {
            throw new NotFoundException("Film con id: " + id + " NON Trovato");
        }
    }

    @Override
    public Film deleteFilmById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Film f = mongoTemplate.findOne(query, Film.class);
        mongoTemplate.findAndRemove(query, Film.class);
        StringBuilder deletedFilm = new StringBuilder();
        deletedFilm.append("\nRichiesta Eliminazione Film:\n");
        deletedFilm.append("\nFilm Eliminato:\n");
        deletedFilm.append("Nome: " + f.getNome() + " Anno: " + f.getAnno() + " Data creazione: " + f.getDataCreazione() + "\n");
        LOGGER.info(deletedFilm.toString());
        return f;
    }

    // Riempie il DB di MONGO Con i film nel JSON di riferimento
    // Commentare le righe 125 e 126 in addFilm per evitare di sovrascrivere la data creazione
    /*@Override
    public void addListaFilms() {

        JSONParser parser = new JSONParser();

        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(new FileReader("C:\\Users\\Enzo\\Desktop\\responseFilm.json"));

            for (Object o : a) {
                JSONObject film = (JSONObject) o;

                String nome = (String) film.get("nome");
                Integer anno = (Integer) film.get("anno");
                String formato = (String) film.get("formato");
                JSONArray categoria = (JSONArray) film.get("categoria");
                List<String> categoriaList = new ArrayList<>();
                if (categoria != null) {
                    for (Object la : categoria) {
                        categoriaList.add(la.toString());
                    }
                }
                JSONArray linguaAudio = (JSONArray) film.get("linguaAudio");
                List<String> lingueAudio = new ArrayList<>();
                if (linguaAudio != null) {
                    for (Object la : linguaAudio) {
                        lingueAudio.add(la.toString());
                    }
                }
                JSONArray linguaSottotitoli = (JSONArray) film.get("linguaSottotitoli");
                List<String> lingueSottotitoli = new ArrayList<>();
                if (linguaSottotitoli != null) {
                    for (Object ls : linguaSottotitoli) {
                        lingueSottotitoli.add(ls.toString());
                    }
                }
                String trama = (String) film.get("trama");
                String locandina = (String) film.get("locandina");
                Long dataCreazione = (Long) film.get("dataCreazione");

                Film newFilm = new Film();
                newFilm.setNome(nome);
                newFilm.setAnno(anno);
                newFilm.setFormato(formato);
                newFilm.setCategoria(categoriaList);
                newFilm.setLinguaAudio(lingueAudio);
                newFilm.setLinguaSottotitoli(lingueSottotitoli);
                newFilm.setTrama(trama);
                newFilm.setLocandina(locandina);
                newFilm.setDataCreazione(dataCreazione);

                this.addFilm(newFilm);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

}