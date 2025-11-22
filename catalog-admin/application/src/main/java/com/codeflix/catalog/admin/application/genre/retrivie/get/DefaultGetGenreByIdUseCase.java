package com.codeflix.catalog.admin.application.genre.retrivie.get;

import com.codeflix.catalog.admin.domain.exceptions.NotFoundException;
import com.codeflix.catalog.admin.domain.genre.Genre;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import com.codeflix.catalog.admin.domain.genre.GenreID;

import java.util.Objects;
import java.util.function.Supplier;

public class DefaultGetGenreByIdUseCase extends GetGenreByIdUseCase {

    private final GenreGateway genreGateway;

    public DefaultGetGenreByIdUseCase(final GenreGateway genreGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public GenreOutput execute(String anId) {
        final var anGenreId = GenreID.from(anId);
        return this.genreGateway.findById(anGenreId)
                .map(GenreOutput::from)
                .orElseThrow(notFound(anGenreId));
    }

    private static Supplier<NotFoundException> notFound(final GenreID anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

}
