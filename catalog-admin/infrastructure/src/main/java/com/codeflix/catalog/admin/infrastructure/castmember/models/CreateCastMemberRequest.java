package com.codeflix.catalog.admin.infrastructure.castmember.models;

import com.codeflix.catalog.admin.domain.castmember.CastMemberType;

public record CreateCastMemberRequest(
        String name,
        CastMemberType type
) {
}
