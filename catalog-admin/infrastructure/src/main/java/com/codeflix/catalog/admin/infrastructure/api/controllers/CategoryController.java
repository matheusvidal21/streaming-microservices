package com.codeflix.catalog.admin.infrastructure.api.controllers;

import com.codeflix.catalog.admin.application.category.create.CreateCategoryCommand;
import com.codeflix.catalog.admin.application.category.create.CreateCategoryOutput;
import com.codeflix.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.catalog.admin.application.category.update.UpdateCategoryCommand;
import com.codeflix.catalog.admin.application.category.update.UpdateCategoryOutput;
import com.codeflix.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.validation.handler.Notification;
import com.codeflix.catalog.admin.infrastructure.api.CategoryApi;
import com.codeflix.catalog.admin.infrastructure.category.models.CategoryListResponse;
import com.codeflix.catalog.admin.infrastructure.category.models.CategoryResponse;
import com.codeflix.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.codeflix.catalog.admin.infrastructure.category.presenters.CategoryApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import java.util.function.Function;

@RestController
public class CategoryController implements CategoryApi {

    private final CreateCategoryUseCase createCategoryUseCase;

    private final GetCategoryByIdUseCase getCategoryByIdUseCase;

    private final UpdateCategoryUseCase updateCategoryUseCase;

    private final DeleteCategoryUseCase deleteCategoryUseCase;

    private final ListCategoriesUseCase listCategoriesUseCase;

    public CategoryController(
            final CreateCategoryUseCase createCategoryUseCase,
            final GetCategoryByIdUseCase getCategoryByIdUseCase,
            final UpdateCategoryUseCase updateCategoryUseCase,
            final DeleteCategoryUseCase deleteCategoryUseCase,
            final ListCategoriesUseCase listCategoriesUseCase
    ) {
        this.createCategoryUseCase = Objects.requireNonNull(createCategoryUseCase);
        this.getCategoryByIdUseCase = Objects.requireNonNull(getCategoryByIdUseCase);
        this.updateCategoryUseCase = Objects.requireNonNull(updateCategoryUseCase);
        this.deleteCategoryUseCase = Objects.requireNonNull(deleteCategoryUseCase);
        this.listCategoriesUseCase = Objects.requireNonNull(listCategoriesUseCase);
    }

    @Override
    public ResponseEntity<?> createCategory(final CreateCategoryRequest anInput) {
        final var aCommand = CreateCategoryCommand.with(
                anInput.name(),
                anInput.description(),
                anInput.active()
        );

        final Function<Notification, ResponseEntity<?>> anError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<CreateCategoryOutput, ResponseEntity<?>> onSuccess = output ->
                ResponseEntity.created(URI.create("/categories/" + output.id()))
                        .body(output);

        return this.createCategoryUseCase.execute(aCommand)
                .fold(anError, onSuccess);
    }

    @Override
    public Pagination<CategoryListResponse> listCategories(final int page, final int perPage, final String search, final String sort, final String direction) {
        return this.listCategoriesUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CategoryApiPresenter::present);
    }

    @Override
    public CategoryResponse getById(@PathVariable("id") final String anId) {
        return CategoryApiPresenter.present(this.getCategoryByIdUseCase.execute(anId));
    }

    @Override
    public ResponseEntity<?> updateById(@PathVariable("id") final String anId, @RequestBody final UpdateCategoryRequest anInput) {
        final var aCommand = UpdateCategoryCommand.with(
                anId,
                anInput.name(),
                anInput.description(),
                anInput.active() != null ? anInput.active() : true
        );

        final Function<Notification, ResponseEntity<?>> anError = notification ->
                ResponseEntity.unprocessableEntity().body(notification);

        final Function<UpdateCategoryOutput, ResponseEntity<?>> onSuccess = ResponseEntity::ok;

        return this.updateCategoryUseCase.execute(aCommand)
                .fold(anError, onSuccess);
    }

    @Override
    public void deleteById(final String anId) {
        this.deleteCategoryUseCase.execute(anId);
    }

}
