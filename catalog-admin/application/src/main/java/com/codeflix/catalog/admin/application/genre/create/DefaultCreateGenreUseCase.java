package com.codeflix.catalog.admin.application.genre.create;

import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.exceptions.NotificationException;
import com.codeflix.catalog.admin.domain.genre.Genre;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import com.codeflix.catalog.admin.domain.validation.Error;
import com.codeflix.catalog.admin.domain.validation.ValidationHandler;
import com.codeflix.catalog.admin.domain.validation.handler.Notification;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class DefaultCreateGenreUseCase extends CreateGenreUseCase{

    private final CategoryGateway categoryGateway;

    private final GenreGateway genreGateway;

    public DefaultCreateGenreUseCase(final CategoryGateway categoryGateway, final GenreGateway genreGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
        this.genreGateway = Objects.requireNonNull(genreGateway);
    }

    @Override
    public CreateGenreOutput execute(final CreateGenreCommand aCommand) {
        final var aName = aCommand.name();
        final var isActive = aCommand.active();
        final var categoryIds = toCategoryIds(aCommand.categories());

        final var notification = Notification.create();
        notification.append(validateCategories((categoryIds)));

        final var aGenre = notification.validate(() -> Genre.newGenre(aName, isActive));

        if (notification.hasErrors()) {
            throw new NotificationException("Could not create Aggregate Genre", notification);
        }

        return CreateGenreOutput.from(this.genreGateway.create(aGenre));
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
