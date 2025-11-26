package com.codeflix.catalog.admin.infrastructure.castmember.models;

import com.codeflix.catalog.admin.domain.castmember.CastMemberType;

import java.time.Instant;

public record CastMemberResponse(
        String id,
        String name,
        CastMemberType type,
        Instant createdAt,
        Instant updatedAt
) {
}
