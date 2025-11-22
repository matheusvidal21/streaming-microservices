package com.codeflix.catalog.admin.application.category.create;

import com.codeflix.catalog.admin.application.UseCaseTest;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;


public class CreateCategoryUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultCreateCategoryUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(categoryGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() {
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCommand = CreateCategoryCommand.with(
            expectedName,
            expectedDescription,
            expectedIsActive
        );

        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName())
                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                    && Objects.nonNull(aCategory.getId())
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.isNull(aCategory.getDeletedAt());
                        }
                ));
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

        final var actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());

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

        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        final var actualOutput = useCase.execute(aCommand).get();

        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName())
                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                    && Objects.nonNull(aCategory.getId())
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.nonNull(aCategory.getDeletedAt());
                        }
                ));
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

        Mockito.when(categoryGateway.create(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        final var actualNotification = useCase.execute(aCommand).getLeft();

        Assertions.assertEquals(expectedErrorCount, actualNotification.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualNotification.firstError().message());

        Mockito.verify(categoryGateway, Mockito.times(1))
                .create(Mockito.argThat(aCategory -> {
                            return Objects.equals(expectedName, aCategory.getName())
                                    && Objects.equals(expectedDescription, aCategory.getDescription())
                                    && Objects.equals(expectedIsActive, aCategory.isActive())
                                    && Objects.nonNull(aCategory.getId())
                                    && Objects.nonNull(aCategory.getCreatedAt())
                                    && Objects.nonNull(aCategory.getUpdatedAt())
                                    && Objects.nonNull(aCategory.getDeletedAt());
                        }
                ));
    }

}
