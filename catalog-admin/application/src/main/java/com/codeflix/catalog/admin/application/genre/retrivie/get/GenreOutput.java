package com.codeflix.catalog.admin.application.genre.retrivie.get;

import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreOutput(
        String id,
        String name,
        List<String> categories,
        boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
) {

    public static GenreOutput from(final Genre aGenre) {
        return new GenreOutput(
                aGenre.getId().getValue(),
                aGenre.getName(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getUpdatedAt(),
                aGenre.getDeletedAt()
        );
    }

}
