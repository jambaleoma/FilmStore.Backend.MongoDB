package it.enzo.me.FilmStore.backend.Film.repository;

import it.enzo.me.FilmStore.backend.Film.model.Film;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FilmRepository extends PagingAndSortingRepository<Film, String> {
    @Aggregation(pipeline = { "{$group: { _id: '', filtroAnno: {$max:  $anno }}}" })
    public Integer max();

    @Aggregation(pipeline = { "{$group: {_id:  '', filtroAnno: {$min:  $anno }}}" })
    public Integer min();
}