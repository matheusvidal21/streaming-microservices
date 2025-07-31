package com.codeflix.catalog.admin.domain.category;

import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import com.codeflix.catalog.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface CategoryGateway {

    Optional<Category> findById(CategoryID anId);

    Pagination<Category> findAll(SearchQuery aQuery);

    Category create(Category aCategory);

    Category update(Category aCategory);

    void deleteById(CategoryID anId);

}
