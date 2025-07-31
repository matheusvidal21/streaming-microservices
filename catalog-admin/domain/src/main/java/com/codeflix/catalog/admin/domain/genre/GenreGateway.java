package com.codeflix.catalog.admin.domain.genre;

import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;

import java.util.Optional;

public interface GenreGateway {

    Optional<Genre> findById(GenreID anId);

    Pagination<Genre> findAll(SearchQuery aQuery);

    Genre create(Genre aGenre);

    Genre update(Genre aGenre);

    void deleteById(GenreID anId);

}
