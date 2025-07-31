package com.codeflix.catalog.admin.application.genre.create;

import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

@ExtendWith(MockitoExtension.class)
public class CreateGenreUseCaseTest {

    @InjectMocks
    private DefaultCreateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    @BeforeEach
    void cleanUp() {
        Mockito.reset(categoryGateway);
        Mockito.reset(genreGateway);
    }

    @Test
    public void givenAValidCommand_whenCallsCreateGenre_thenShouldReturnGenreId() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var aCommand = CreateGenreCommand.with(
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.create(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertNotNull(actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).create(Mockito.argThat(aGenre ->
            Objects.equals(expectedName, aGenre.getName())
                    && Objects.equals(expectedIsActive, aGenre.isActive())
                    && Objects.equals(expectedCategories, aGenre.getCategories())
                    && Objects.nonNull(aGenre.getId())
                    && Objects.nonNull(aGenre.getCreatedAt())
                    && Objects.nonNull(aGenre.getUpdatedAt())
                    && Objects.isNull(aGenre.getDeletedAt())
        ));
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
