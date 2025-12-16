package com.codeflix.catalog.admin.domain.genre;

import com.codeflix.catalog.admin.domain.base.Identifier;

import java.util.Objects;
import java.util.UUID;

public class GenreID extends Identifier {

    private final String value;

    private GenreID(final String value) {
        Objects.requireNonNull(value, "Genre ID must not be null");
        this.value = value;
    }

    public static GenreID unique() {
        return GenreID.from(UUID.randomUUID());
    }

    public static GenreID from(final String anId) {
        return new GenreID(anId.toLowerCase());
    }

    public static GenreID from(final UUID anId) {
        return GenreID.from(anId.toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final GenreID that = (GenreID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

}
