package com.codeflix.catalog.admin.infrastructure.application.category.create;

import com.codeflix.catalog.admin.application.category.create.CreateCategoryCommand;
import com.codeflix.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.infrastructure.IntegrationTest;
import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@IntegrationTest
public class CreateCategoryUseCaseIT {

    @Autowired
    private CreateCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        Assertions.assertEquals(0, categoryRepository.count());

        final var aCommand = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAInvalidName_whenCallsCreateCategory_thenShouldReturnDomainException() {
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());
        Assertions.assertEquals(0, categoryRepository.count());

        Mockito.verify(categoryGateway, Mockito.times(0)).create(Mockito.any());
    }

    @Test
    public void givenAValidCommandWithInactiveCategory_whenCallsCreateCategory_shouldReturnInactiveCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;

        final var aCommand = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());
        Assertions.assertEquals(1, categoryRepository.count());

        final var actualCategory = categoryRepository.findById(actualOutput.id()).get();

        Assertions.assertEquals(expectedName, actualCategory.getName());
        Assertions.assertEquals(expectedDescription, actualCategory.getDescription());
        Assertions.assertEquals(expectedIsActive, actualCategory.isActive());
        Assertions.assertNotNull(actualCategory.getCreatedAt());
        Assertions.assertNotNull(actualCategory.getUpdatedAt());
        Assertions.assertNotNull(actualCategory.getDeletedAt());
    }

    @Test
    public void givenAValidCommand_whenGatewayThrowsException_shouldReturnAException() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final boolean expectedIsActive = false;
        final var expectedErrorMessage = "Gateway error";
        final var expectedErrorCount = 1;

        final var aCommand = CreateCategoryCommand.with(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                        .when(categoryGateway).create(Mockito.any());

        Assertions.assertEquals(0, categoryRepository.count());

        final var actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(0, categoryRepository.count());
        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());
    }

}
