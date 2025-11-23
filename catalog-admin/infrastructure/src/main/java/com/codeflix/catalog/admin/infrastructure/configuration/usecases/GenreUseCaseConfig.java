package com.codeflix.catalog.admin.infrastructure.configuration.usecases;

import com.codeflix.catalog.admin.application.genre.create.CreateGenreUseCase;
import com.codeflix.catalog.admin.application.genre.create.DefaultCreateGenreUseCase;
import com.codeflix.catalog.admin.application.genre.delete.DefaultDeleteGenreUseCase;
import com.codeflix.catalog.admin.application.genre.delete.DeleteGenreUseCase;
import com.codeflix.catalog.admin.application.genre.retrivie.get.DefaultGetGenreByIdUseCase;
import com.codeflix.catalog.admin.application.genre.retrivie.get.GetGenreByIdUseCase;
import com.codeflix.catalog.admin.application.genre.retrivie.list.DefaultListGenresUseCase;
import com.codeflix.catalog.admin.application.genre.retrivie.list.ListGenresUseCase;
import com.codeflix.catalog.admin.application.genre.update.DefaultUpdateGenreUseCase;
import com.codeflix.catalog.admin.application.genre.update.UpdateGenreUseCase;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class GenreUseCaseConfig {

    private final GenreGateway genreGateway;

    private final CategoryGateway categoryGateway;

    public GenreUseCaseConfig(final GenreGateway genreGateway, final CategoryGateway categoryGateway) {
        this.genreGateway = Objects.requireNonNull(genreGateway);
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public GetGenreByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetGenreByIdUseCase(genreGateway);
    }

    @Bean
    public ListGenresUseCase listGenresUseCase() {
        return new DefaultListGenresUseCase(genreGateway);
    }

    @Bean
    public CreateGenreUseCase createGenreUseCase() {
        return new DefaultCreateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public UpdateGenreUseCase updateGenreUseCase() {
        return new DefaultUpdateGenreUseCase(genreGateway, categoryGateway);
    }

    @Bean
    public DeleteGenreUseCase deleteGenreUseCase() {
        return new DefaultDeleteGenreUseCase(genreGateway);
    }

}
