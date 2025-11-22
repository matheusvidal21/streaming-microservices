package com.codeflix.catalog.admin.application.genre.retrieve.list;

import com.codeflix.catalog.admin.application.UseCaseTest;
import com.codeflix.catalog.admin.application.genre.retrivie.list.DefaultListGenresUseCase;
import com.codeflix.catalog.admin.application.genre.retrivie.list.GenreListOutput;
import com.codeflix.catalog.admin.domain.genre.Genre;
import com.codeflix.catalog.admin.domain.genre.GenreGateway;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class ListGenresUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultListGenresUseCase useCase;

    @Mock
    private GenreGateway genreGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(genreGateway);
    }

    @Test
    public void givenAValidQuery_whenCallsListGenre_thenShouldReturnGenres() {
        // given
        final var genres = List.of(
                Genre.newGenre("Ação", true),
                Genre.newGenre("Drama", true)
        );
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 2;
        final var expectedItems = genres.stream()
                .map(GenreListOutput::from)
                .toList();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        Mockito.when(genreGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());
        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndResultIsEmpty_thenShouldReturnGenres() {
        // given
        final var genres = List.<Genre>of();
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItems = List.<GenreListOutput>of();

        final var expectedPagination = new Pagination<>(
                expectedPage,
                expectedPerPage,
                expectedTotal,
                genres
        );

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        Mockito.when(genreGateway.findAll(Mockito.any()))
                .thenReturn(expectedPagination);

        // when
        final var actualOutput = useCase.execute(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualOutput.currentPage());
        Assertions.assertEquals(expectedPerPage, actualOutput.perPage());
        Assertions.assertEquals(expectedTotal, actualOutput.total());
        Assertions.assertEquals(expectedItems, actualOutput.items());
        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
    }

    @Test
    public void givenAValidQuery_whenCallsListGenreAndGatewayThrowsError_thenShouldReturnException() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "A";
        final var expectedSort = "createdAt";
        final var expectedDirection = "asc";
        final var expectedErrorMessage = "Gateway error";

        final var aQuery = new SearchQuery(
                expectedPage,
                expectedPerPage,
                expectedTerms,
                expectedSort,
                expectedDirection
        );

        Mockito.when(genreGateway.findAll(Mockito.any()))
                .thenThrow(new IllegalStateException(expectedErrorMessage));

        // when
        final var actualOutput = Assertions.assertThrows(
                IllegalStateException.class,
                () -> useCase.execute(aQuery)
        );

        // then
        Assertions.assertEquals(expectedErrorMessage, actualOutput.getMessage());
        Mockito.verify(genreGateway, Mockito.times(1)).findAll(Mockito.eq(aQuery));
    }

}
