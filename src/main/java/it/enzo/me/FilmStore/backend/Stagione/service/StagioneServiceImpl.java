package it.enzo.me.FilmStore.backend.Stagione.service;

import it.enzo.me.FilmStore.backend.Customer.service.CustomerServiceImpl;
import it.enzo.me.FilmStore.backend.Exception.AlreadyExistException;
import it.enzo.me.FilmStore.backend.Exception.NotFoundException;
import it.enzo.me.FilmStore.backend.Serie.model.Serie;
import it.enzo.me.FilmStore.backend.Stagione.model.Stagione;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Logger;

@Controller
public class StagioneServiceImpl implements StagioneService {

    private static final Logger LOGGER = Logger.getLogger( CustomerServiceImpl.class.getName() );

    final
    MongoTemplate mongoTemplate;

    public StagioneServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Stagione> getAllStagioni() {
        List<Stagione> stagioni = (List<Stagione>) mongoTemplate.findAll(Stagione.class);
        if (stagioni.size() == 0) {
            throw new NotFoundException("Nessuna Stagione Trovata");
        }
        StringBuilder listStagioni = new StringBuilder();
        listStagioni.append("\nRichiesta elenco totale Stagioni:\n");
        listStagioni.append("\nTrovate " + stagioni.size() + " Stagioni\n");
        LOGGER.info(listStagioni.toString());
        return stagioni;
    }

    @Override
    public List<Stagione> getAllStagioniByIdSerie(String serie_id) {
        List<Stagione> stagioni = new ArrayList<>();
        for (Stagione s : mongoTemplate.findAll(Stagione.class)) {
            if (s.getSerie_id().equals(serie_id)) {
                stagioni.add(s);
            }
        }
        StringBuilder listStagioniByName = new StringBuilder();
        listStagioniByName.append("\nRichiesta elenco Stagioni filtrate per Serie Id '" + serie_id + "':\n");
        listStagioniByName.append("\nTrovate " + stagioni.size() + " Stagioni\n");
        listStagioniByName.append("\nElenco Stagioni filtrate per Serie Id '" + serie_id + "':\n\n");
        for (Stagione s : stagioni) {
            listStagioniByName.append("Nome: " + s.getNome_serie() + " Numero Stagione: " + s.getNumeroStagione() + "\n");
        }
        LOGGER.info(listStagioniByName.toString());
        return stagioni;
    }

    @Override
    public Stagione getStagioneById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Stagione s =  mongoTemplate.findOne(query, Stagione.class);
        if (s == null)
            throw new NotFoundException("Stagione con id: " + id + " NON Trovata");
        StringBuilder stagioneById = new StringBuilder();
        stagioneById.append("\nRichiesta Stagione filtrando Per Id '" + id + "':\n");
        stagioneById.append("Nome: " + s.getNome_serie() + " Numero Stagione: " + s.getNumeroStagione() + "\n");
        LOGGER.info(stagioneById.toString());
        return s;
    }

    @Override
    public Stagione addStagione(Stagione s) {
        StringBuilder saveStagione = new StringBuilder();
        saveStagione.append("\nRichiesta Salvataggio Serie:\n");
        saveStagione.append("Nome: " + s.getNome_serie() + " Numero Stagione: " + s.getNumeroStagione() + "\n");
        LOGGER.info(saveStagione.toString());
        return mongoTemplate.save(s);
    }

    @Override
    public Stagione updateStagione(Stagione nuovaStagione, String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Stagione.class)) {
            Update update = new Update();
            update.set("numeroStagione", nuovaStagione.getNumeroStagione());
            update.set("formato", nuovaStagione.getFormato());
            update.set("anno", nuovaStagione.getAnno());
            update.set("numeroEpisodi", nuovaStagione.getNumeroEpisodi());
            update.set("episodi", nuovaStagione.getEpisodi());
            update.set("linguaAudio", nuovaStagione.getLinguaAudio());
            update.set("linguaSottotitoli", nuovaStagione.getLinguaSottotitoli());
            update.set("trama", nuovaStagione.getTrama());
            update.set("locandina", nuovaStagione.getLocandina());
            mongoTemplate.updateFirst(query, update, Stagione.class);
            Stagione stagioneAggiornata = mongoTemplate.findOne(query, Stagione.class);
            StringBuilder updateStagione = new StringBuilder();
            updateStagione.append("\nRichiesta Aggiornamento Serie:\n");
            updateStagione.append("\nSerie Aggiornata:\n");
            updateStagione.append("Nome: " + stagioneAggiornata.getNome_serie() + " Numero Stagione: " + stagioneAggiornata.getNumeroStagione() + "\n");
            return stagioneAggiornata;
        } else {
            throw new NotFoundException("Stagione con id: " + id + " NON Trovata");
        }
    }

    @Override
    public Stagione deleteStagioneById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Stagione s = mongoTemplate.findOne(query, Stagione.class);
        mongoTemplate.findAndRemove(query, Serie.class);
        StringBuilder deletedStagione = new StringBuilder();
        deletedStagione.append("\nRichiesta Eliminazione Serie:\n");
        deletedStagione.append("\nSerie Eliminato:\n");
        deletedStagione.append("Nome: " + s.getNome_serie() + " Numero Stagione: " + s.getNumeroStagione() + "\n");
        LOGGER.info(deletedStagione.toString());
        return s;
    }

    @Override
    public void deleteStagioniBySerieId(String id) {
        List<Stagione> stagioni = getAllStagioniByIdSerie(id);
        for (Stagione s : stagioni) {
            this.deleteStagioneById(s.getId());
        }
    }

    // Riempie il DB Couchbase Con le Stagioni nel JSON di riferimento
    /*@Override
    public void addListaStagioni() {

        JSONParser parser = new JSONParser();

        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(new FileReader("C:\\Users\\Enzo\\Desktop\\responseStagioni.json"));

            for (Object o : a) {
                JSONObject stagione = (JSONObject) o;

                String serie_id = (String) stagione.get("serie_id");
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
                String locandina = (String) stagione.get("locandina");

                Stagione newStagione = new Stagione();
                newStagione.setSerie_id(serie_id);
                newStagione.setNome_serie(nome_serie);
                newStagione.setNumeroStagione(numeroStagione);
                newStagione.setFormato(formato);
                newStagione.setAnno(anno);
                newStagione.setNumeroEpisodi(numeroEpisodi);
                newStagione.setEpisodi(episodiList);
                newStagione.setLinguaAudio(lingueAudio);
                newStagione.setLinguaSottotitoli(lingueSottotitoli);
                newStagione.setTrama(trama);
                newStagione.setLocandina(locandina);

                this.addStagione(newStagione);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
}
