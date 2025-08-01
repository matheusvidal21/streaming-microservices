package com.codeflix.catalog.admin.infrastructure.category.presenters;

import com.codeflix.catalog.admin.application.category.retrieve.get.CategoryOutput;
import com.codeflix.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import com.codeflix.catalog.admin.infrastructure.category.models.CategoryResponse;
import com.codeflix.catalog.admin.infrastructure.category.models.CategoryListResponse;

public interface CategoryApiPresenter {

    static CategoryResponse present(final CategoryOutput output) {
        return new CategoryResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static CategoryListResponse present(final CategoryListOutput output) {
        return new CategoryListResponse(
                output.id().getValue(),
                output.name(),
                output.description(),
                output.active(),
                output.createdAt(),
                output.deletedAt()
        );
    }

}
