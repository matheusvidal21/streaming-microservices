package com.codeflix.catalog.admin.infrastructure.api.controllers;

import com.codeflix.catalog.admin.application.genre.create.CreateGenreCommand;
import com.codeflix.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.codeflix.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.codeflix.catalog.admin.application.genre.retrivie.get.GetGenreByIdUseCase;
import com.codeflix.catalog.admin.application.genre.retrivie.list.ListGenresUseCase;
import com.codeflix.catalog.admin.application.genre.update.UpdateGenreCommand;
import com.codeflix.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import com.codeflix.catalog.admin.infrastructure.api.GenreApi;
import com.codeflix.catalog.admin.infrastructure.genre.models.CreateGenreRequest;
import com.codeflix.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.codeflix.catalog.admin.infrastructure.genre.models.GenreResponse;
import com.codeflix.catalog.admin.infrastructure.genre.models.UpdateGenreRequest;
import com.codeflix.catalog.admin.infrastructure.genre.presenters.GenreApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class GenreController implements GenreApi {

    private final CreateGenreUseCase createGenreUseCase;

    private final GetGenreByIdUseCase getGenreByIdUseCase;

    private final UpdateGenreUseCase updateGenreUseCase;

    private final DeleteGenreUseCase deleteGenreUseCase;

    private final ListGenresUseCase listGenresUseCase;

    public GenreController(
            final CreateGenreUseCase createGenreUseCase,
            final GetGenreByIdUseCase getGenreByIdUseCase,
            final UpdateGenreUseCase updateGenreUseCase,
            final DeleteGenreUseCase deleteGenreUseCase,
            final ListGenresUseCase listGenresUseCase
    ) {
        this.createGenreUseCase = Objects.requireNonNull(createGenreUseCase);
        this.getGenreByIdUseCase = Objects.requireNonNull(getGenreByIdUseCase);
        this.updateGenreUseCase = Objects.requireNonNull(updateGenreUseCase);
        this.deleteGenreUseCase = Objects.requireNonNull(deleteGenreUseCase);
        this.listGenresUseCase = Objects.requireNonNull(listGenresUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateGenreRequest anInput) {
        final var aCommand = CreateGenreCommand.with(
                anInput.name(),
                anInput.isActive(),
                anInput.categories()
        );

        final var output = this.createGenreUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/genres/" + output.id())).body(output);
    }

    @Override
    public Pagination<GenreListResponse> list(final int page, final int perPage, final String search, final String sort, final String direction) {
        return this.listGenresUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(GenreApiPresenter::present);
    }

    @Override
    public GenreResponse getById(final String anId) {
        return GenreApiPresenter.present(this.getGenreByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> updateById(final String anId, final UpdateGenreRequest anInput) {
        final var aCommand = UpdateGenreCommand.with(
                anId,
                anInput.name(),
                anInput.isActive(),
                anInput.categories()
        );

        final var output = this.updateGenreUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteGenreUseCase.execute(anId);
    }

}
