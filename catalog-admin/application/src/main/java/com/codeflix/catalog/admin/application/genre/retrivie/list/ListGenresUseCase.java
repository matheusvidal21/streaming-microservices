package com.codeflix.catalog.admin.application.genre.retrivie.list;

import com.codeflix.catalog.admin.application.base.UseCase;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;

public abstract class ListGenresUseCase extends UseCase<SearchQuery, Pagination<GenreListOutput>> {

}
