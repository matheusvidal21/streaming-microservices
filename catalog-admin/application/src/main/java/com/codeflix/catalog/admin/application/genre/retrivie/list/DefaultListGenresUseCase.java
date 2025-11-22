package com.codeflix.catalog.admin.application.genre.retrivie.list;

import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;

import java.util.Objects;

public class DefaultListGenresUseCase extends ListGenresUseCase {

    private final GenreGateway genreGateway;

    public DefaultListGenresUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }


    @Override
    public Pagination<GenreListOutput> execute(final SearchQuery aQuery) {
        return this.genreGateway.findAll(aQuery)
                .map(GenreListOutput::from);
    }

}
