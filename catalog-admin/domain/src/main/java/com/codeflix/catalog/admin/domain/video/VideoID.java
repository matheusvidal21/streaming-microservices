package com.codeflix.catalog.admin.domain.video;

import com.codeflix.catalog.admin.domain.base.Identifier;

import java.util.Objects;
import java.util.UUID;

public class VideoID extends Identifier {

    private final String value;

    private VideoID(final String value) {
        Objects.requireNonNull(value, "Video ID must not be null");
        this.value = value;
    }

    public static VideoID unique() {
        return VideoID.from(UUID.randomUUID());
    }

    public static VideoID from(final String anId) {
        return new VideoID(anId.toLowerCase());
    }

    public static VideoID from(final UUID anId) {
        return VideoID.from(anId.toString());
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final VideoID that = (VideoID) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getValue());
    }

}
