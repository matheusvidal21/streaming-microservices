package com.codeflix.catalog.admin.infrastructure.configuration.usecases;

import com.codeflix.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codeflix.catalog.admin.application.category.create.DefaultCreateCategoryUseCase;
import com.codeflix.catalog.admin.application.category.delete.DefaultDeleteCategoryUseCase;
import com.codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.get.DefaultGetCategoryByIdUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.list.DefaultListCategoriesUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.catalog.admin.application.category.update.DefaultUpdateCategoryUseCase;
import com.codeflix.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CategoryUseCaseConfig {

    private final CategoryGateway categoryGateway;

    public CategoryUseCaseConfig(final CategoryGateway categoryGateway) {
        this.categoryGateway = Objects.requireNonNull(categoryGateway);
    }

    @Bean
    public GetCategoryByIdUseCase getCategoryByIdUseCase() {
        return new DefaultGetCategoryByIdUseCase(categoryGateway);
    }

    @Bean
    public ListCategoriesUseCase listCategoriesUseCase() {
        return new DefaultListCategoriesUseCase(categoryGateway);
    }

    @Bean
    public CreateCategoryUseCase createCategoryUseCase() {
        return new DefaultCreateCategoryUseCase(categoryGateway);
    }

    @Bean
    public UpdateCategoryUseCase updateCategoryUseCase() {
        return new DefaultUpdateCategoryUseCase(categoryGateway);
    }

    @Bean
    public DeleteCategoryUseCase deleteCategoryUseCase() {
        return new DefaultDeleteCategoryUseCase(categoryGateway);
    }

}
