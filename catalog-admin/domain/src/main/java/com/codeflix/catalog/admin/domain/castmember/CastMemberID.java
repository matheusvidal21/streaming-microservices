package com.codeflix.catalog.admin.domain.castmember;

import com.codeflix.catalog.admin.domain.base.Identifier;

import java.util.Objects;
import java.util.UUID;

public class CastMemberID extends Identifier {

    private final String value;

    private CastMemberID(final String value) {
        Objects.requireNonNull(value, "CastMember ID must not be null");
        this.value = value;
    }

    public static CastMemberID unique() {
        return CastMemberID.from(UUID.randomUUID());
    }

    public static CastMemberID from(final String anId) {
        return new CastMemberID(anId.toLowerCase());
    }

    public static CastMemberID from(final UUID anId) {
        return CastMemberID.from(anId.toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final CastMemberID that = (CastMemberID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

}
