package it.enzo.me.FilmStore.backend.Serie.service;

import it.enzo.me.FilmStore.backend.Customer.service.CustomerServiceImpl;
import it.enzo.me.FilmStore.backend.Exception.NotFoundException;
import it.enzo.me.FilmStore.backend.Serie.model.Serie;
import it.enzo.me.FilmStore.backend.Stagione.model.Stagione;
import it.enzo.me.FilmStore.backend.Stagione.service.StagioneService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class SerieServiceImpl implements SerieService {

    private static final Logger LOGGER = Logger.getLogger( CustomerServiceImpl.class.getName() );

    final
    MongoTemplate mongoTemplate;

    final
    StagioneService stagioneService;

    public SerieServiceImpl(MongoTemplate mongoTemplate, StagioneService stagioneService) {
        this.mongoTemplate = mongoTemplate;
        this.stagioneService = stagioneService;
    }

    @Override
    public List<Serie> getAllSerie() {
        List<Serie> serie = (List<Serie>) mongoTemplate.findAll(Serie.class);
        if (serie.size() == 0) {
            throw new NotFoundException("Nessuna Serie Trovata");
        }
        StringBuilder listSerie = new StringBuilder();
        listSerie.append("\nRichiesta elenco totale Serie:\n");
        listSerie.append("\nTrovate " + serie.size() + " Serie\n");
        LOGGER.info(listSerie.toString());
        return serie;
    }

    @Override
    public List<Serie> getAllNewSerie(Integer numeroNuoveSerie) {
        List<Serie> allNewSerie;
        Query q = new Query();
        q.addCriteria(Criteria.where("_class").is("it.enzo.me.FilmStore.backend.Serie.model.Serie")).with(Sort.by(Sort.Direction.DESC, "dataCreazione")).limit(numeroNuoveSerie);
        allNewSerie = mongoTemplate.find(q,Serie.class);
        StringBuilder listNewSerie = new StringBuilder();
        listNewSerie.append("\nRichiesta elenco " + numeroNuoveSerie + " Serie più recenti:\n");
        listNewSerie.append("\nTrovate " + allNewSerie.size() + " Serie\n");
        listNewSerie.append("\nElenco " + numeroNuoveSerie + " Serie più recenti:\n");
        for (Serie s : allNewSerie) {
            listNewSerie.append("Nome: " + s.getNome() + " Data Creazione: " + s.getDataCreazione() + "\n");
        }
        LOGGER.info(listNewSerie.toString());
        return allNewSerie;
    }

    @Override
    public List<Serie> getAllSerieByName(String nome) {
        List<Serie> serie = new ArrayList<>();
        for (Serie s : mongoTemplate.findAll(Serie.class)) {
            if (s.getNome().equals(nome)) {
                serie.add(s);
            }
        }
        if (serie.size() == 0) {
            throw new NotFoundException("Nessuna Serie con Nome: " + nome + " è stata Trovata");
        }
        StringBuilder listSerieByName = new StringBuilder();
        listSerieByName.append("\nRichiesta elenco Serie filtrate per Nome '" + nome + "':\n");
        listSerieByName.append("\nTrovati " + serie.size() + " Serie\n");
        listSerieByName.append("\nElenco Serie filtrate per Nome '" + nome + "':\n\n");
        for (Serie s : serie) {
            listSerieByName.append("Nome: " + s.getNome() + " Data Creazione: " + s.getDataCreazione() + "\n");
        }
        LOGGER.info(listSerieByName.toString());
        return serie;
    }

    @Override
    public Serie getSerieById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Serie s =  mongoTemplate.findOne(query, Serie.class);
        if (s == null)
            throw new NotFoundException("Serie con id: " + id + " NON Trovata");
        StringBuilder serieById = new StringBuilder();
        serieById.append("\nRichiesta Serie filtrando Per Id '" + id + "':\n");
        serieById.append("Nome: " + s.getNome() + " Data Creazione: " + s.getDataCreazione() + "\n");
        LOGGER.info(serieById.toString());
        return s;
    }

    @Override
    public Serie addSerie(Serie s) {
//        String newDateSerie = new SimpleDateFormat("yyyyMMddHHmmss").format(Calendar.getInstance().getTime());
//        s.setDataCreazione(Long.parseLong(newDateSerie));
        StringBuilder saveSerie = new StringBuilder();
        saveSerie.append("\nRichiesta Salvataggio Serie:\n");
        saveSerie.append("Nome: " + s.getNome() + " Data Creazione: " + s.getDataCreazione() + "\n");
        LOGGER.info(saveSerie.toString());
        return mongoTemplate.save(s);
    }

    @Override
    public Serie updateSerie(Serie nuovaSerie, String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Serie.class)) {
            Update update = new Update();
            List<Stagione> stagioni = stagioneService.getAllStagioniByIdSerie(id);
            update.set("nome", nuovaSerie.getNome());
            update.set("locandina", nuovaSerie.getLocandina());
            update.set("stagioni", stagioni);
            update.set("dataCreazione", nuovaSerie.getDataCreazione());
            mongoTemplate.updateFirst(query, update, Serie.class);
            Serie serieAggiornata = mongoTemplate.findOne(query, Serie.class);
            StringBuilder updateSerie = new StringBuilder();
            updateSerie.append("\nRichiesta Aggiornamento Serie:\n");
            updateSerie.append("\nSerie Aggiornata:\n");
            updateSerie.append("Nome: " + serieAggiornata.getNome() + " Data creazione: " + serieAggiornata.getDataCreazione() + "\n");
            LOGGER.info(updateSerie.toString());
            return serieAggiornata;
        } else {
            throw new NotFoundException("Serie con id: " + id + " NON Trovata");
        }
    }

    @Override
    public Serie deleteSerieById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Serie s = mongoTemplate.findOne(query, Serie.class);
        mongoTemplate.findAndRemove(query, Serie.class);
        StringBuilder deletedSerie = new StringBuilder();
        deletedSerie.append("\nRichiesta Eliminazione Serie:\n");
        deletedSerie.append("\nSerie Eliminato:\n");
        deletedSerie.append("Nome: " + s.getNome() + " Data creazione: " + s.getDataCreazione() + "\n");
        LOGGER.info(deletedSerie.toString());
        return s;
    }

    // Riempie il DB Couchbase Con le SerieTV nel JSON di riferimento
    // Commentare le righe 112 e 113 in addSerie per evitare di sovrascrivere la data creazione
    /*@Override
    public void addListaSerie() {
        JSONParser parser = new JSONParser();

        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(new FileReader("C:\\Users\\Enzo\\Desktop\\responseSerie.json"));

            for (Object o : a) {
                JSONObject serie = (JSONObject) o;

                String serie_id = (String) serie.get("serie_id");
                String nome = (String) serie.get("nome");
                String locandina = (String) serie.get("locandina");
                JSONArray stagioni = (JSONArray) serie.get("stagioni");
                List<Stagione> stagioniList = new ArrayList<>();
                if (stagioni != null) {
                    for (Object s : stagioni) {
                        JSONObject stagione = (JSONObject) s;

                        String serie_idStagione = (String) stagione.get("serie_id");
                        String nome_serie = (String) stagione.get("nome_serie");
                        Integer numeroStagione = (Integer) stagione.get("numeroStagione");
                        String formato = (String) stagione.get("formato");
                        Integer anno = (Integer) stagione.get("anno");
                        Integer numeroEpisodi = (Integer) stagione.get("numeroEpisodi");
                        JSONArray episodi = (JSONArray) stagione.get("episodi");
                        String[] episodiList = new String[episodi.size()];
                        if (episodi != null) {
                            for (int i = 0; i < episodi.size(); i++) {
                                episodiList[i] = episodi.get(i).toString();
                            }
                        }
                        JSONArray linguaAudio = (JSONArray) stagione.get("linguaAudio");
                        List<String> lingueAudio = new ArrayList<>();
                        if (linguaAudio != null) {
                            for (Object la : linguaAudio) {
                                lingueAudio.add(la.toString());
                            }
                        }
                        JSONArray linguaSottotitoli = (JSONArray) stagione.get("linguaSottotitoli");
                        List<String> lingueSottotitoli = new ArrayList<>();
                        if (linguaSottotitoli != null) {
                            for (Object ls : linguaSottotitoli) {
                                lingueSottotitoli.add(ls.toString());
                            }
                        }
                        String trama = (String) stagione.get("trama");
                        String locandinaStagione = (String) stagione.get("locandina");

                        Stagione newStagione = new Stagione();
                        newStagione.setSerie_id(serie_idStagione);
                        newStagione.setNome_serie(nome_serie);
                        newStagione.setNumeroStagione(numeroStagione);
                        newStagione.setFormato(formato);
                        newStagione.setAnno(anno);
                        newStagione.setNumeroEpisodi(numeroEpisodi);
                        newStagione.setEpisodi(episodiList);
                        newStagione.setLinguaAudio(lingueAudio);
                        newStagione.setLinguaSottotitoli(lingueSottotitoli);
                        newStagione.setTrama(trama);
                        newStagione.setLocandina(locandinaStagione);

                        stagioniList.add(newStagione);
                    }
                }
                Long dataCreazione = (Long) serie.get("dataCreazione");

                Serie newSerie = new Serie();
                newSerie.set_id(serie_id);
                newSerie.setNome(nome);
                newSerie.setLocandina(locandina);
                newSerie.setStagioni(stagioniList);
                newSerie.setDataCreazione(dataCreazione);

                this.addSerie(newSerie);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/

}
