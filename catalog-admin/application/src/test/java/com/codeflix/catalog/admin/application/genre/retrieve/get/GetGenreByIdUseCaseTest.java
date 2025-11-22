package com.codeflix.catalog.admin.application.genre.retrieve.get;

import com.codeflix.catalog.admin.application.UseCaseTest;
import com.codeflix.catalog.admin.application.genre.retrivie.get.DefaultGetGenreByIdUseCase;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.exceptions.NotFoundException;
import com.codeflix.catalog.admin.domain.genre.Genre;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import com.codeflix.catalog.admin.domain.genre.GenreID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;
import java.util.Optional;

public class GetGenreByIdUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultGetGenreByIdUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidId_whenCallsGetGenreById_thenShouldReturnGenre() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(CategoryID.from("123"), CategoryID.from("456"));
        final var aGenre = Genre.newGenre(expectedName, expectedIsActive).addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        Mockito.when(genreGateway.findById(Mockito.any()))
                .thenReturn(Optional.of(aGenre.clone()));

        // when
        final var actualGenre = useCase.execute(expectedId.getValue());

        // then
        Assertions.assertEquals(expectedId.getValue(), actualGenre.id());
        Assertions.assertEquals(expectedName, actualGenre.name());
        Assertions.assertEquals(expectedIsActive, actualGenre.active());
        Assertions.assertEquals(asString(expectedCategories), actualGenre.categories());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.createdAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.updatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.deletedAt());
        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsGetGenreAndDoesNotExists_thenShouldReturnGenre() {
        // given
        final var expectedId = GenreID.from("123");
        final var expectedErrorMessage = "Genre with ID 123 was not found";

        Mockito.when(genreGateway.findById(Mockito.eq(expectedId)))
                .thenReturn(Optional.empty());

        // when
        final var actualException = Assertions.assertThrows(
                NotFoundException.class,
                () -> useCase.execute(expectedId.getValue())
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualException.getMessage());
        Mockito.verify(genreGateway, Mockito.times(1)).findById(Mockito.eq(expectedId));
    }

    private List<String> asString(final List<CategoryID> categories) {
        return categories.stream()
                .map(CategoryID::getValue)
                .toList();
    }

}
