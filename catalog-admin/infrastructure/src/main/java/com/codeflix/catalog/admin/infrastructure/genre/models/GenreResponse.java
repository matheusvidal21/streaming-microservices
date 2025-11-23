package com.codeflix.catalog.admin.infrastructure.genre.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record GenreResponse(
        String id,
        String name,
        String description,
        @JsonProperty("is_active") Boolean active,
        Instant createdAt,
        Instant updatedAt,
        Instant deletedAt
        ) {
}
