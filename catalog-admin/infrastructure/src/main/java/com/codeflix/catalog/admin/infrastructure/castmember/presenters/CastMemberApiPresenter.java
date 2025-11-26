package com.codeflix.catalog.admin.infrastructure.castmember.presenters;

import com.codeflix.catalog.admin.application.genre.retrivie.get.GenreOutput;
import com.codeflix.catalog.admin.application.genre.retrivie.list.GenreListOutput;
import com.codeflix.catalog.admin.infrastructure.genre.models.GenreListResponse;
import com.codeflix.catalog.admin.infrastructure.genre.models.GenreResponse;

public interface CastMemberApiPresenter {

    static GenreResponse present(final GenreOutput output) {
        return new GenreResponse(
                output.id(),
                output.name(),
                output.categories(),
                output.active(),
                output.createdAt(),
                output.updatedAt(),
                output.deletedAt()
        );
    }

    static GenreListResponse present(final GenreListOutput output) {
        return new GenreListResponse(
                output.id(),
                output.name(),
                output.active(),
                output.createdAt(),
                output.deletedAt()
        );
    }

}
