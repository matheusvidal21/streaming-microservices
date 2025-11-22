package com.codeflix.catalog.admin.application.genre.update;

import com.codeflix.catalog.admin.application.UseCaseTest;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.exceptions.NotificationException;
import com.codeflix.catalog.admin.domain.genre.Genre;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.mockito.AdditionalAnswers.returnsFirstArg;

public class UpdateGenreUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultUpdateGenreUseCase useCase;

    @Mock
    private CategoryGateway categoryGateway;

    @Mock
    private GenreGateway genreGateway;

    protected List<Object> getMocks() {
        return List.of(
                categoryGateway,
                genreGateway
        );
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("filme", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(aUpdatedGenre ->
            Objects.equals(expectedId, aUpdatedGenre.getId())
                    && Objects.equals(expectedName, aUpdatedGenre.getName())
                    && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                    && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                    && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                    && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                    && Objects.isNull(aUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithCategories_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("filme", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(
                CategoryID.from("123"),
                CategoryID.from("456")
        );

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(expectedCategories);

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.isNull(aUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAValidCommandWithInactiveGenre_whenCallsUpdateGenre_thenShouldReturnGenreId() {
        // given
        final var aGenre = Genre.newGenre("filme", true);
        final var expectedId = aGenre.getId();
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = new ArrayList<CategoryID>();

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito.when(genreGateway.update(Mockito.any()))
                .thenAnswer(returnsFirstArg());

        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        // when
        final var actualOutput = useCase.execute(aCommand);

        // then
        Assertions.assertNotNull(actualOutput);
        Assertions.assertEquals(expectedId.getValue(), actualOutput.id());

        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(1)).update(Mockito.argThat(aUpdatedGenre ->
                Objects.equals(expectedId, aUpdatedGenre.getId())
                        && Objects.equals(expectedName, aUpdatedGenre.getName())
                        && Objects.equals(expectedIsActive, aUpdatedGenre.isActive())
                        && Objects.equals(expectedCategories, aUpdatedGenre.getCategories())
                        && Objects.equals(aGenre.getCreatedAt(), aUpdatedGenre.getCreatedAt())
                        && aGenre.getUpdatedAt().isBefore(aUpdatedGenre.getUpdatedAt())
                        && Objects.nonNull(aUpdatedGenre.getDeletedAt())
        ));
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateGenre_thenShouldReturnNotificationException() {
        // given
        final var aGenre = Genre.newGenre("filme", true);
        final var expectedId = aGenre.getId();
        final String expectedName = null;
        final var expectedIsActive = true;
        final var expectedCategories = new ArrayList<CategoryID>();
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        // when
        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessage, actualException.getErrors().get(0).message());
        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    @Test
    public void givenAInvalidName_whenCallsUpdateGenreAndSomeCategoriesDoesNotExists_thenShouldReturnNotificationException() {
        // given
        final var aGenre = Genre.newGenre("filme", true);
        final var expectedId = aGenre.getId();

        final String expectedName = null;
        final var expectedIsActive = true;
        final var filmes = CategoryID.from("123");
        final var series = CategoryID.from("456");
        final var documentarios = CategoryID.from("789");
        final var expectedCategories = List.of(
                filmes,
                series,
                documentarios
        );

        final var expectedErrorMessageOne = "Some categories could not be found: 456, 789";
        final var expectedErrorMessageTwo = "'name' should not be null";
        final var expectedErrorCount = 2;

        final var aCommand = UpdateGenreCommand.with(
                expectedId.getValue(),
                expectedName,
                expectedIsActive,
                asString(expectedCategories)
        );

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        Mockito.when(categoryGateway.existsByIds(Mockito.any()))
                .thenReturn(List.of(filmes));

        // when
        final var actualException = Assertions.assertThrows(
                NotificationException.class,
                () -> useCase.execute(aCommand)
        );

        // then
        Assertions.assertNotNull(actualException);
        Assertions.assertEquals(expectedErrorCount, actualException.getErrors().size());
        Assertions.assertEquals(expectedErrorMessageOne, actualException.getErrors().get(0).message());
        Assertions.assertEquals(expectedErrorMessageTwo, actualException.getErrors().get(1).message());
        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.any());
        Mockito.verify(categoryGateway, Mockito.times(1)).existsByIds(Mockito.eq(expectedCategories));
        Mockito.verify(genreGateway, Mockito.times(0)).update(Mockito.any());
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
