package com.codeflix.catalog.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public record CreateGenreRequest(
        String name,
        String description,
        @JsonProperty("is_active") Boolean active
) {
}
