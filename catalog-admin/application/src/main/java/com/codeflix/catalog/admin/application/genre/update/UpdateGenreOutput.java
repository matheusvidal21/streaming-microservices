package com.codeflix.catalog.admin.application.genre.update;

import com.codeflix.catalog.admin.domain.genre.Genre;

public record UpdateGenreOutput(
        String id
) {

    public static UpdateGenreOutput from(final String anId) {
        return new UpdateGenreOutput(anId);
    }

    public static UpdateGenreOutput from(final Genre aGenre) {
        return new UpdateGenreOutput(aGenre.getId().getValue());
    }

}
