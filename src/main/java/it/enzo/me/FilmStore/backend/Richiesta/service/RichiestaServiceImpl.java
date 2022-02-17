package it.enzo.me.FilmStore.backend.Richiesta.service;

import it.enzo.me.FilmStore.backend.Exception.NotFoundException;
import it.enzo.me.FilmStore.backend.Film.model.Film;
import it.enzo.me.FilmStore.backend.Richiesta.model.Richiesta;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import it.enzo.me.FilmStore.backend.Richiesta.repository.RichiestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Controller
public class RichiestaServiceImpl implements RichiestaService {

    private static final Logger LOGGER = Logger.getLogger(RichiestaServiceImpl.class.getName());

    final
    MongoTemplate mongoTemplate;

    public RichiestaServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Richiesta> getAllRichieste() {
        List<Richiesta> richieste = (List<Richiesta>) mongoTemplate.findAll(Richiesta.class);
        if (richieste == null) {
            throw new NotFoundException("Nessuna Richiesta Trovata");
        }
        StringBuilder listRichieste = new StringBuilder();
        listRichieste.append("\nRichiesta elenco totale Richieste:\n");
        listRichieste.append("\nTrovate " + richieste.size() + " Richieste\n");
        for (Richiesta r : richieste) {
            listRichieste.append("Titolo Film Richiesto: " + r.getTitoloFilmRichiesto() + " Nome del Richiedente: " + r.getNomeCliente() + "\n");
        }
        LOGGER.info(listRichieste.toString());
        return richieste;
    }

    @Override
    public List<Richiesta> getAllRichiesteByNomeCliente(String nomeCliente) {
        List<Richiesta> richiesteByNomeCliente = new ArrayList<>();
        for (Richiesta r : this.getAllRichieste()) {
            if (r.getNomeCliente().equals(nomeCliente)) {
                richiesteByNomeCliente.add(r);
            }
        }
        if (richiesteByNomeCliente.size() == 0) {
            throw new NotFoundException("Nessuna Richiesta Trovata");
        }
        StringBuilder listRichiesteByName = new StringBuilder();
        listRichiesteByName.append("\nRichiesta elenco Richieste filtrate per Nome Cliente '" + nomeCliente + "':\n");
        listRichiesteByName.append("\nTrovate " + richiesteByNomeCliente.size() + " Richieste\n");
        listRichiesteByName.append("\nElenco Richieste filtrate per Nome Cliente '" + nomeCliente + "':\n\n");
        for (Richiesta r : richiesteByNomeCliente) {
            listRichiesteByName.append("Titolo Film Richiesto: " + r.getTitoloFilmRichiesto() + " Nome del Richiedente: " + r.getNomeCliente() + "\n");
        }
        LOGGER.info(listRichiesteByName.toString());
        return richiesteByNomeCliente;
    }

    @Override
    public Richiesta getRichiestaById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Richiesta r = mongoTemplate.findOne(query, Richiesta.class);
        if (r == null)
            throw new NotFoundException("Richiesta con id: " + id + " NON Trovata");
        StringBuilder richiestaById = new StringBuilder();
        richiestaById.append("\nRichiesta Richiesta filtrando Per Id '" + id + "':\n");
        richiestaById.append("Titolo Film Richiesto: " + r.getTitoloFilmRichiesto() + " Nome del Richiedente: " + r.getNomeCliente() + "\n");
        LOGGER.info(richiestaById.toString());
        return r;
    }

    @Override
    public Richiesta addRichiesta(Richiesta r) {
        StringBuilder saveRichiesta = new StringBuilder();
        saveRichiesta.append("\nRichiesta Salvataggio Richiesta:\n");
        saveRichiesta.append("Titolo Film Richiesto: " + r.getTitoloFilmRichiesto() + " Nome del Richiedente: " + r.getNomeCliente() + "\n");
        LOGGER.info(saveRichiesta.toString());
        return mongoTemplate.save(r);
    }

    @Override
    public Richiesta updateRichiesta(Richiesta nuovaRichiesta, String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Richiesta.class)) {
            Update update = new Update();
            update.set("titoloFilmRichiesto", nuovaRichiesta.getTitoloFilmRichiesto());
            update.set("formatoFilmRichiesto", nuovaRichiesta.getFormatoFilmRichiesto());
            update.set("dataInserimento", nuovaRichiesta.getDataInserimento());
            update.set("nomeCliente", nuovaRichiesta.getNomeCliente());
            update.set("stato", nuovaRichiesta.getStato());
            update.set("note", nuovaRichiesta.getNote());
            mongoTemplate.updateFirst(query, update, Richiesta.class);
            Richiesta richiestaAggiornata = this.getRichiestaById(id);
            StringBuilder updateRichiesta = new StringBuilder();
            updateRichiesta.append("\nRichiesta Aggiornamento Richiesta:\n");
            updateRichiesta.append("\nRichiesta Aggiornata:\n");
            updateRichiesta.append("Titolo Film Richiesto: " + richiestaAggiornata.getTitoloFilmRichiesto() + " Nome del Richiedente: " + richiestaAggiornata.getNomeCliente() + "\n");
            LOGGER.info(updateRichiesta.toString());
            return richiestaAggiornata;
        } else {
            throw new NotFoundException("Richiesta con id: " + id + " NON Trovata");
        }
    }

    @Override
    public Richiesta deleteRichiestaById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Richiesta r = mongoTemplate.findOne(query, Richiesta.class);
        mongoTemplate.findAndRemove(query, Richiesta.class);
        StringBuilder deletedRichiesta = new StringBuilder();
        deletedRichiesta.append("\nRichiesta Eliminazione Richiesta:\n");
        deletedRichiesta.append("\nRichiesta Eliminata:\n");
        deletedRichiesta.append("Titolo Film Richiesto: " + r.getTitoloFilmRichiesto() + " Nome del Richiedente: " + r.getNomeCliente() + "\n");
        LOGGER.info(deletedRichiesta.toString());
        return r;
    }

    @Override
    public ArrayList getRichiesteYearForStatistiche() {
        return new ArrayList<>(getRichiesteForStatisticsMethod().keySet());
    }

    @Override
    public ArrayList getRichiesteForStatistiche(String year) {
        return new ArrayList<>(getRichiesteForStatisticsMethod().get(year).values());
    }

    @Override
    public Map<String, Map<String, Integer>> getRichiesteForStatisticsMethod() {
        Map<String, Map<String, Integer>> anno2_mese2richieste = new LinkedHashMap<>();
        for (Richiesta r : this.getAllRichieste()) {
            String[] dataRichiesta = r.getDataInserimento().split(" ");
            String anno = dataRichiesta[3];
            String mese = dataRichiesta[2];
            if (!anno2_mese2richieste.containsKey(anno)) {
                anno2_mese2richieste.put(anno, createYear());
            } else {
                anno2_mese2richieste.put(anno, anno2_mese2richieste.get(anno));
            }
            anno2_mese2richieste.get(anno).put(mese, anno2_mese2richieste.get(anno).get(mese) + 1);
        }
        return anno2_mese2richieste;
    }

    private Map<String, Integer> createYear() {
        Map<String, Integer> mese2Richieste = new LinkedHashMap<>();
        String[] mesiAnno = new String[12];
        mesiAnno[0] = "gennaio";
        mesiAnno[1] = "febbraio";
        mesiAnno[2] = "marzo";
        mesiAnno[3] = "aprile";
        mesiAnno[4] = "maggio";
        mesiAnno[5] = "giugno";
        mesiAnno[6] = "luglio";
        mesiAnno[7] = "agosto";
        mesiAnno[8] = "settembre";
        mesiAnno[9] = "ottobre";
        mesiAnno[10] = "novembre";
        mesiAnno[11] = "dicembre";
        for (int i = 0; i < mesiAnno.length; i++) {
            mese2Richieste.put(mesiAnno[i], 0);
        }
        return mese2Richieste;
    }

    // Riempie il DB di MONGO Con le Richieste nel JSON di riferimento
    /*@Override
    public void addListaRichieste() {

        JSONParser parser = new JSONParser();

        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(new FileReader("C:\\Users\\Enzo\\Desktop\\responseRichieste.json"));

            for (Object o : a) {
                JSONObject richiesta = (JSONObject) o;

                String titoloFilmRichiesto = (String) richiesta.get("titoloFilmRichiesto");
                String formatoFilmRichiesto = (String) richiesta.get("formatoFilmRichiesto");
                String dataInserimento = (String) richiesta.get("dataInserimento");
                String nomeCliente = (String) richiesta.get("nomeCliente");
                String stato = (String) richiesta.get("stato");
                String note = (String) richiesta.get("note");

                Richiesta newRichiesta = new Richiesta();
                newRichiesta.setTitoloFilmRichiesto(titoloFilmRichiesto);
                newRichiesta.setFormatoFilmRichiesto(formatoFilmRichiesto);
                newRichiesta.setDataInserimento(dataInserimento);
                newRichiesta.setNomeCliente(nomeCliente);
                newRichiesta.setStato(stato);
                newRichiesta.setNote(note);

                this.addRichiesta(newRichiesta);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
}
