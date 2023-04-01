package it.enzo.me.FilmStore.backend.Film.service;

import it.enzo.me.FilmStore.backend.Customer.service.CustomerServiceImpl;
import it.enzo.me.FilmStore.backend.Exception.NotFoundException;
import it.enzo.me.FilmStore.backend.Film.model.Film;
import it.enzo.me.FilmStore.backend.Film.model.FilmPage;
import it.enzo.me.FilmStore.backend.Film.model.FilterFilm;
import it.enzo.me.FilmStore.backend.Film.repository.FilmRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Controller
public class FilmServiceImpl implements FilmService {

    private static final Logger LOGGER = Logger.getLogger( CustomerServiceImpl.class.getName() );

    final
    MongoTemplate mongoTemplate;

    private final
    FilmRepository filmRepository;

    public FilmServiceImpl(MongoTemplate mongoTemplate, FilmRepository filmRepository) {
        this.mongoTemplate = mongoTemplate;
        this.filmRepository = filmRepository;
    }

    @Override
    public List<Film> getAllFilms() {
        Query q = new Query();
        q.addCriteria(Criteria.where("_class").is("it.enzo.me.FilmStore.backend.Film.model.Film")).with(Sort.by(Sort.Direction.DESC, "dataCreazione")).limit(10);
        List<Film> films = (List<Film>) mongoTemplate.find(q, Film.class);
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
    public Page<Film> getAllPagebleFilms(FilmPage filmPage) {
        Sort sort = Sort.by(filmPage.getSortDirection(), filmPage.getSortBy());
        Pageable pageable = PageRequest.of(filmPage.getPageNumber(),
                filmPage.getPageSize(), sort);
        return filmRepository.findAll(pageable);
    }

    @Override
    public String getRecentOlderYear() {
        String recentOlderYear = "";
        int recentYear = this.max();
        int olderYear = this.min();
        recentOlderYear = olderYear + "-" + recentYear;
        StringBuilder recentOlderYearFilm = new StringBuilder();
        recentOlderYearFilm.append("\nRichiesta anno film meno recente e più recente:\n");
        recentOlderYearFilm.append("\nAnno meno recente: " + olderYear + " Anno più recente: " + recentYear +"\n");
        LOGGER.info(recentOlderYearFilm.toString());
        return recentOlderYear;
    }
    @Override
    public List<Film> getAllNewFilms(Integer numeroNuoviFilm) {
        List<Film> allNewFilms;
        Query q = new Query();
        q.addCriteria(Criteria.where("_class").is("it.enzo.me.FilmStore.backend.Film.model.Film")).with(Sort.by(Sort.Direction.DESC, "dataCreazione")).limit(numeroNuoviFilm);
        allNewFilms = mongoTemplate.find(q,Film.class);
        StringBuilder listNewFilms = new StringBuilder();
        listNewFilms.append("\nRichiesta elenco " + numeroNuoviFilm + " Film più recenti:\n");
        listNewFilms.append("\nTrovati " + allNewFilms.size() + " Film\n");
        listNewFilms.append("\nElenco " + numeroNuoviFilm + " Film più recenti:\n");
        for (Film f : allNewFilms) {
            listNewFilms.append("Nome: " + f.getNome() + " Anno: " + f.getAnno() + " Data Creazione: " + f.getDataCreazione() + "\n");
        }
        LOGGER.info(listNewFilms.toString());
        return allNewFilms;
    }

    @Override
    public Page<Film> getAllFilmsByName(String nome, FilmPage filmPage) {

        Query q = new Query();
        Pattern pattern = Pattern.compile(Pattern.quote(nome), Pattern.CASE_INSENSITIVE);
        Sort sort = Sort.by(filmPage.getSortDirection(), filmPage.getSortBy());
        Pageable pageable = PageRequest.of(filmPage.getPageNumber(), filmPage.getPageSize(), sort);
        q.addCriteria(Criteria.where("nome").regex(pattern)).with(pageable);

        StringBuilder listFilmsByName = new StringBuilder();
        listFilmsByName.append("\nRichiesta elenco Film filtrati per Nome '" + nome + "':\n");
        LOGGER.info(listFilmsByName.toString());

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(q, Film.class),
                pageable,
                () -> mongoTemplate.count(q.skip(0).limit(0), Film.class)
        );
    }

    @Override
    public Page<Film> getAllFilmsByFormat(String format, FilmPage filmPage) {

        Query q = new Query();
        Pattern pattern = Pattern.compile(Pattern.quote(format), Pattern.CASE_INSENSITIVE);
        Sort sort = Sort.by(filmPage.getSortDirection(), filmPage.getSortBy());
        Pageable pageable = PageRequest.of(filmPage.getPageNumber(), filmPage.getPageSize(), sort);
        q.addCriteria(Criteria.where("formato").regex(pattern)).with(pageable);

        StringBuilder listFilmsByName = new StringBuilder();
        listFilmsByName.append("\nRichiesta elenco Film filtrati per Formato '" + format);
        LOGGER.info(listFilmsByName.toString());

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(q, Film.class),
                pageable,
                () -> mongoTemplate.count(q.skip(0).limit(0), Film.class)
        );
    }

    @Override
    public Page<Film> getAllFilmsByCategory(List<String> categories, FilmPage filmPage) {

        Query q = new Query();
        Sort sort = Sort.by(filmPage.getSortDirection(), filmPage.getSortBy());
        Pageable pageable = PageRequest.of(filmPage.getPageNumber(), filmPage.getPageSize(), sort);
        List<String> categoryList = new ArrayList<>(categories);
        q.addCriteria(Criteria.where("categoria").in(categoryList)).with(pageable);

        StringBuilder listFilmsByName = new StringBuilder();
        listFilmsByName.append("\nRichiesta elenco Film filtrati per Categoria: " + categories);
        LOGGER.info(listFilmsByName.toString());

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(q, Film.class),
                pageable,
                () -> mongoTemplate.count(q.skip(0).limit(0), Film.class)
        );
    }

    @Override
    public Page<Film> getAllFilteredFilms(FilterFilm filterFilm, FilmPage filmPage) {

        Query q = new Query();
        Sort sort = Sort.by(filmPage.getSortDirection(), filmPage.getSortBy());
        Pageable pageable = PageRequest.of(filmPage.getPageNumber(), filmPage.getPageSize(), sort);
        if (StringUtils.isNotBlank(filterFilm.getNomeFilm())) {
            Pattern patternNome = Pattern.compile(Pattern.quote(filterFilm.getNomeFilm()), Pattern.CASE_INSENSITIVE);
            q.addCriteria(Criteria.where("nome").regex(patternNome)).with(pageable);
        }
        if (StringUtils.isNotBlank(filterFilm.getFormatoFilm())) {
            Pattern patternFormato = Pattern.compile(Pattern.quote(filterFilm.getFormatoFilm()), Pattern.CASE_INSENSITIVE);
            q.addCriteria(Criteria.where("formato").regex(patternFormato)).with(pageable);
        }
        if (filterFilm.getCategorieFilm().size() > 0) {
            List<String> categoryList = new ArrayList<>(filterFilm.getCategorieFilm());
            q.addCriteria(Criteria.where("categoria").in(categoryList)).with(pageable);
        }

        StringBuilder listFilmsByName = new StringBuilder();
        listFilmsByName.append("\nRichiesta elenco Film filtrati per Categoria: \n");
        LOGGER.info(listFilmsByName.toString());

        return PageableExecutionUtils.getPage(
                mongoTemplate.find(q, Film.class),
                pageable,
                () -> mongoTemplate.count(q.skip(0).limit(0), Film.class)
        );
    }

    @Override
    public Integer min() {
        return filmRepository.min();
    }

    @Override
    public Integer max() {
        return filmRepository.max();
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