package com.codeflix.catalog.admin.application.category.retrieve.get;

import com.codeflix.catalog.admin.application.UseCaseTest;
import com.codeflix.catalog.admin.domain.category.Category;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.exceptions.NotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetCategoryByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetCategoryByIdUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetCategoryById_thenShouldReturnCategory() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId();

        Mockito.when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.of(aCategory.clone()));

        final var actualOutput = useCase.execute(expectedId.getValue());

        Assertions.assertEquals(expectedId, actualOutput.id());
        Assertions.assertEquals(expectedName, actualOutput.name());
        Assertions.assertEquals(expectedDescription, actualOutput.description());
        Assertions.assertEquals(expectedIsActive, actualOutput.active());
        Assertions.assertEquals(aCategory.getCreatedAt(), actualOutput.createdAt());
        Assertions.assertEquals(aCategory.getUpdatedAt(), actualOutput.updatedAt());
        Assertions.assertEquals(aCategory.getDeletedAt(), actualOutput.deletedAt());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategoryById_thenShouldReturnNotFoundException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        Mockito.when(categoryGateway.findById(expectedId))
                .thenReturn(Optional.empty());

        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_thenShouldReturnException() {
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Gateway error";

        Mockito.when(categoryGateway.findById(expectedId))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualException = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());

        Mockito.verify(categoryGateway, Mockito.times(1)).findById(expectedId);
    }

}
