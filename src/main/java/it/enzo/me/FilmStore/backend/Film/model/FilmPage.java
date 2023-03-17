package it.enzo.me.FilmStore.backend.Film.model;

import lombok.Data;
import org.springframework.data.domain.Sort;

@Data
public class FilmPage {
    private int pageNumber = 0;
    private int pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortBy = "dataCreazione";
}
