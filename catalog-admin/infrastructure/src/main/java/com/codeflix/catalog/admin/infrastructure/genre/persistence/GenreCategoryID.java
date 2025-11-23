package com.codeflix.catalog.admin.infrastructure.genre.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class GenreCategoryID implements Serializable {

    @Column(nullable = false)
    private String genreId;

    @Column(nullable = false)
    private String categoryId;

    public GenreCategoryID() {
    }

    private GenreCategoryID(final String aGenreId, final String aCategoryId) {
        this.genreId = aGenreId;
        this.categoryId = aCategoryId;
    }

    public static GenreCategoryID from(final String aGenreId, final String aCategoryId) {
        return new GenreCategoryID(aGenreId, aCategoryId);
    }

    public String getGenreId() {
        return genreId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        GenreCategoryID that = (GenreCategoryID) o;
        return Objects.equals(genreId, that.genreId) && Objects.equals(categoryId, that.categoryId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(genreId, categoryId);
    }

}
