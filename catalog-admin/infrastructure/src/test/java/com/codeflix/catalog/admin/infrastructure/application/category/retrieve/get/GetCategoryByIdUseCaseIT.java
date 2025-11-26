package com.codeflix.catalog.admin.infrastructure.application.category.retrieve.get;

import com.codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.catalog.admin.domain.category.Category;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.exceptions.DomainException;
import com.codeflix.catalog.admin.infrastructure.IntegrationTest;
import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Arrays;

@IntegrationTest
public class GetCategoryByIdUseCaseIT {

    @Autowired
    private GetCategoryByIdUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsGetCategoryById_thenShouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        save(aCategory);

        final var actualCategory = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualCategory.id());
        Assertions.assertEquals(expectedName, actualCategory.name());
        Assertions.assertEquals(expectedDescription, actualCategory.description());
        Assertions.assertEquals(expectedIsActive, actualCategory.active());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualCategory.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualCategory.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualCategory.deletedAt());
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategoryById_thenShouldReturnNotFoundException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        final var actualException = Assertions.assertThrows(
                DomainException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Gateway error";

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway)
                .findById(expectedId);

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
    }

    private void save(final Category... categories) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(categories)
                .map(CategoryJpaEntity::from)
                .toList());
    }

}
