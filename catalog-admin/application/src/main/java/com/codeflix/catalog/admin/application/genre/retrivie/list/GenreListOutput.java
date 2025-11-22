package com.codeflix.catalog.admin.application.genre.retrivie.list;

import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.genre.Genre;

import java.time.Instant;
import java.util.List;

public record GenreListOutput(
        String name,
        List<String> categories,
        boolean active,
        Instant createdAt,
        Instant deletedAt
) {

    public static GenreListOutput from(final Genre aGenre) {
        return new GenreListOutput(
                aGenre.getName(),
                aGenre.getCategories().stream().map(CategoryID::getValue).toList(),
                aGenre.isActive(),
                aGenre.getCreatedAt(),
                aGenre.getDeletedAt()
        );
    }

}
