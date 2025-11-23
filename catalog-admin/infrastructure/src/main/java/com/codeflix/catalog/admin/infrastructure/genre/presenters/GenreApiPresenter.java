package com.codeflix.catalog.admin.infrastructure.genre.presenters;

import com.codeflix.catalog.admin.application.category.retrieve.get.CategoryOutput;
import com.codeflix.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import com.codeflix.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.codeflix.catalog.admin.infrastructure.genre.models.GenreResponse;

public interface GenreApiPresenter {

    static GenreResponse present(final CategoryOutput output) {
        return new GenreResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final CategoryListOutput output) {
        return new GenreListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.deletedAt()
        );
    }

}
