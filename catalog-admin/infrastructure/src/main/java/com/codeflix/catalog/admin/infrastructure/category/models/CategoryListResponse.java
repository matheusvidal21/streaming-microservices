package com.codeflix.catalog.admin.infrastructure.category.models;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;

public record CategoryListResponse(
        String id,
        String name,
        String description,
        @JsonProperty("is_active") Boolean active,
        Instant createdAt,
        Instant deletedAt
        ) {
}
