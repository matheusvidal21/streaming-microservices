package com.codeflix.catalog.admin.application.genre.update;

import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.exceptions.DomainException;
import com.codeflix.catalog.admin.domain.exceptions.NotFoundException;
import com.codeflix.catalog.admin.domain.exceptions.NotificationException;
import com.codeflix.catalog.admin.domain.genre.Genre;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import com.codeflix.catalog.admin.domain.genre.GenreID;
import com.codeflix.catalog.admin.domain.validation.Error;
import com.codeflix.catalog.admin.domain.validation.ValidationHandler;
import com.codeflix.catalog.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class DefaultUpdateGenreUseCase extends UpdateGenreUseCase {

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    public DefaultUpdateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public UpdateGenreOutput execute(final UpdateGenreCommand aCommand) {
        final var anId = GenreID.from(aCommand.id());
        final var aName = aCommand.name();
        final var isActive = aCommand.active();
        final var categoryIds = toCategoryIds(aCommand.categories());

        final var aGenre = this.genreGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.append(validateCategories((categoryIds)));

        notification.validate(() -> aGenre.update(aName, isActive, categoryIds));

        if (notification.hasErrors()) {
            throw new NotificationException("Could not update Aggregate Genre %s".formatted(aCommand.id()), notification);
        }

        return UpdateGenreOutput.from(this.genreGateway.update(aGenre));
    }

    private Supplier<DomainException> notFound(final GenreID anId) {
        return () -> NotFoundException.with(Genre.class, anId);
    }

    private ValidationHandler validateCategories(final List<CategoryID> categoryIds) {
        final var notification = Notification.create();
        if (categoryIds == null || categoryIds.isEmpty()) {
            return notification;
        }

        final var retrievedIds = categoryGateway.existsByIds(categoryIds);

        if (categoryIds.size() != retrievedIds.size()) {
            final var missingIds = new ArrayList<>(categoryIds);
            missingIds.removeAll(retrievedIds);

            final var missingIdsMessage = missingIds.stream()
                    .map(CategoryID::getValue)
                    .collect(Collectors.joining(", "));

            notification.append(new Error("Some categories could not be found: " + missingIdsMessage));
        }

        return notification;
    }

    private List<CategoryID> toCategoryIds(final List<String> categoryIdsStr) {
        return categoryIdsStr.stream()
                .map(CategoryID::from)
                .toList();
    }

}
