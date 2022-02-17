package it.enzo.me.FilmStore.backend.Voto.service;

import it.enzo.me.FilmStore.backend.Customer.service.CustomerServiceImpl;
import it.enzo.me.FilmStore.backend.Exception.AlreadyExistException;
import it.enzo.me.FilmStore.backend.Exception.NotFoundException;
import it.enzo.me.FilmStore.backend.Voto.model.Voto;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import net.minidev.json.parser.JSONParser;
import net.minidev.json.parser.ParseException;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.List;
import java.util.logging.Logger;

@Component("VotoService")
public class VotoServiceImpl implements VotoService{

    private static final Logger LOGGER = Logger.getLogger( CustomerServiceImpl.class.getName() );

    final
    MongoTemplate mongoTemplate;

    public VotoServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<Voto> getAllVoti() {
        List<Voto> voti = mongoTemplate.findAll(Voto.class);
        StringBuilder listVotes = new StringBuilder();
        listVotes.append("\nRichiesta elenco totale Voti:\n");
        listVotes.append("\nTrovati " + voti.size() + " Voti\n");
        LOGGER.info(listVotes.toString());
        return voti;
    }

    @Override
    public Voto getVotoByFilmId_Customer(String idFilm, String idCustomer) {
        Voto votoFilm = new Voto();
        for (Voto v : this.getAllVoti()) {
            if ((v.getIdFilm().equals(idFilm)) && (v.getIdCustomer().equals(idCustomer))) {
                votoFilm = v;
            }
        }
        if (votoFilm.getNomeFilm() != null ) {
            StringBuilder votoByIdFilmCustomer = new StringBuilder();
            votoByIdFilmCustomer.append("\nRichiesta Film filtrando Per Id Film '" + idFilm + "' e Id Customer '" + idCustomer + ":\n");
            votoByIdFilmCustomer.append("Nome Film: " + votoFilm.getNomeFilm() + " Nome Customer: " + votoFilm.getFirstNameCustomer() + " Votazione: " + votoFilm.getVotazione() + "\n");
            LOGGER.info(votoByIdFilmCustomer.toString());
        } else {
            return null;
        }
        return votoFilm;
    }

    @Override
    public Voto getVotoById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        Voto v =  mongoTemplate.findOne(query, Voto.class);
        if (v == null)
            throw new NotFoundException("Voto con id: " + id + " NON Trovato");
        StringBuilder votoById = new StringBuilder();
        votoById.append("\nRichiesta Voto filtrando Per Id '" + id + "':\n");
        votoById.append("Nome Film: " + v.getNomeFilm() + " Nome Customer: " + v.getFirstNameCustomer() + " Votazione: " + v.getVotazione() + "\n");
        LOGGER.info(votoById.toString());
        return v;
    }

    @Override
    public Voto addVoto(Voto v) {
        for (Voto voto : this.getAllVoti()) {
            if (voto.getId().equals(v.getId()))
                throw new AlreadyExistException("Il Voto con Id: " + v.getId() + " è già presente, Cambia l'ID se vuoi inserire il Voto");
        }
        StringBuilder savevoto = new StringBuilder();
        savevoto.append("\nRichiesta Salvataggio Voto:\n");
        savevoto.append("Nome Film: " + v.getNomeFilm() + " Nome Customer: " + v.getFirstNameCustomer() + " Votazione: " + v.getVotazione() + "\n");
        LOGGER.info(savevoto.toString());
        return mongoTemplate.save(v);
    }

    @Override
    public Voto updateVoto(Voto nuovoVoto, String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Voto.class)) {
            Update update = new Update();
            update.set("idFilm", nuovoVoto.getIdFilm());
            update.set("nomeFilm", nuovoVoto.getNomeFilm());
            update.set("idCustomer", nuovoVoto.getIdCustomer());
            update.set("firstNameCustomer", nuovoVoto.getFirstNameCustomer());
            update.set("lastNameCustomer", nuovoVoto.getLastNameCustomer());
            update.set("dataCreazioneVoto", nuovoVoto.getDataCreazioneVoto());
            update.set("votazione", nuovoVoto.getVotazione());
            update.set("like", nuovoVoto.getLike());
            mongoTemplate.updateFirst(query, update, Voto.class);
            Voto votoAggiornato = mongoTemplate.findOne(query, Voto.class);
            StringBuilder updateVoto = new StringBuilder();
            updateVoto.append("\nRichiesta Aggiornamento Voto:\n");
            updateVoto.append("\nVoto Aggiornato:\n");
            updateVoto.append("Nome Film: " + votoAggiornato.getNomeFilm() + " Nome Customer: " + votoAggiornato.getFirstNameCustomer() + " Votazione: " + votoAggiornato.getVotazione() + "\n");
            LOGGER.info(updateVoto.toString());
            return votoAggiornato;
        } else {
            throw new NotFoundException("Voto con id: " + id + " NON Trovato");
        }
    }

    @Override
    public Voto deleteVotoById(String id) {
        Query query = new Query(Criteria.where("_id").is(id));
        if (mongoTemplate.exists(query, Voto.class)) {
            Voto v = this.getVotoById(id);
            mongoTemplate.findAndRemove(query, Voto.class);
            StringBuilder deletedVoto = new StringBuilder();
            deletedVoto.append("\nRichiesta Eliminazione Voto:\n");
            deletedVoto.append("\nVoto Eliminato:\n");
            deletedVoto.append("Nome Film: " + v.getNomeFilm() + " Nome Customer: " + v.getFirstNameCustomer() + " Votazione: " + v.getVotazione() + "\n");
            LOGGER.info(deletedVoto.toString());
            return v;
        } else {
            throw new NotFoundException("Voto con id: " + id + " NON Trovato");
        }
    }

    @Override
    public void deleteVotoByFilmId_CustomerId(String filmId, String customerId) {
        Voto v = this.getVotoByFilmId_Customer(filmId, customerId);
        if (v != null) {
            Query query = new Query(Criteria.where("_id").is(v.getId()));
            mongoTemplate.findAndRemove(query, Voto.class);
            StringBuilder deletedVoto = new StringBuilder();
            deletedVoto.append("\nRichiesta Eliminazione Voto:\n");
            deletedVoto.append("\nVoto Eliminato:\n");
            deletedVoto.append("Nome Film: " + v.getNomeFilm() + " Nome Customer: " + v.getFirstNameCustomer() + " Votazione: " + v.getVotazione() + "\n");
            LOGGER.info(deletedVoto.toString());
        } else {
            throw new NotFoundException("Voto con Film Id: " + filmId + " e con Customer Id: " + customerId + " NON Trovato");
        }
    }

    // Riempie il DB di MONGO Con i Voti nel JSON di riferimento
    /*@Override
    public void addListaVoti() {

        JSONParser parser = new JSONParser();

        JSONArray a = null;
        try {
            a = (JSONArray) parser.parse(new FileReader("C:\\Users\\Enzo\\Desktop\\responseVoti.json"));

            for (Object o : a) {
                JSONObject richiesta = (JSONObject) o;

                String voto_id = (String) richiesta.get("voto_id");
                String idFilm = (String) richiesta.get("idFilm");
                String nomeFilm = (String) richiesta.get("nomeFilm");
                String idCustomer = (String) richiesta.get("idCustomer");
                String firstNameCustomer = (String) richiesta.get("firstNameCustomer");
                String lastNameCustomer = (String) richiesta.get("lastNameCustomer");
                String dataCreazioneVoto = (String) richiesta.get("dataCreazioneVoto");
                Integer votazione = (Integer) richiesta.get("votazione");
                Boolean like = (Boolean) richiesta.get("like");

                Voto newVoto = new Voto();
                newVoto.setId(voto_id);
                newVoto.setIdFilm(idFilm);
                newVoto.setNomeFilm(nomeFilm);
                newVoto.setIdCustomer(idCustomer);
                newVoto.setFirstNameCustomer(firstNameCustomer);
                newVoto.setLastNameCustomer(lastNameCustomer);
                newVoto.setDataCreazioneVoto(dataCreazioneVoto);
                newVoto.setVotazione(votazione);
                newVoto.setLike(like);

                this.addVoto(newVoto);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }*/
}
