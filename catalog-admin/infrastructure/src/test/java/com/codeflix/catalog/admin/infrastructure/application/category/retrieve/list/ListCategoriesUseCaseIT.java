package com.codeflix.catalog.admin.infrastructure.application.category.retrieve.list;

import com.codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.catalog.admin.domain.category.Category;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import com.codeflix.catalog.admin.infrastructure.IntegrationTest;
import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

import java.util.Arrays;
import java.util.stream.Stream;

@IntegrationTest
public class ListCategoriesUseCaseIT {

    @Autowired
    private ListCategoriesUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @BeforeEach
    void mockUp() {
        final var filmes = Category.newCategory("Filmes", "A categoria mais assistida", true);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final var series = Category.newCategory("Séries", "Faça maratona de séries aqui", true);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final var animes = Category.newCategory("Animes", "A categoria mais popular", true);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final var desenhos = Category.newCategory("Desenhos", "Para a criançada", true);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final var documentarios = Category.newCategory("Documentários", "Os melhores documentários", true);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final var novelas = Category.newCategory("Novelas", "As melhores novelas", true);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        final var esportes = Category.newCategory("Esportes", "Veja seus esportes favortiso", true);

        final var categories = Stream.of(filmes, series, animes, desenhos, documentarios, novelas, esportes)
                .map(CategoryJpaEntity::from)
                .toList();

        categoryRepository.saveAllAndFlush(categories);
    }

    @Test
    public void givenAValidTerms_shenTermDoesntMatchsPrePersisted_thenShouldReturnEmptyPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "nao existente";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;
        final var expectedItemsCount = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
    }

    @ParameterizedTest
    @CsvSource({
        "fil,0,10,1,1,Filmes",
        "doc,0,10,1,1,Documentários",
        "ANI,0,10,1,1,Animes",
        "nove,0,10,1,1,Novelas",
        "criançada,0,10,1,1,Desenhos",
        "mais assistida,0,10,1,1,Filmes",
    })
    public void givenAValidTerm_whenCallsListCategories_thenShouldReturnCategoriesFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedSort = "name";
        final var expectedDirection = "asc";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,7,7,Animes",
            "name,desc,0,10,7,7,Séries",
            "createdAt,asc,0,10,7,7,Filmes",
            "createdAt,desc,0,10,7,7,Esportes",
            "description,asc,0,10,7,7,Filmes",
    })
    public void givenAValidSortAndDirection_whenCallsListCategories_thenShouldReturnCategoriesSorted(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoryName
    ) {
        final var expectedTerms = "";

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());
        Assertions.assertEquals(expectedCategoryName, actualResult.items().get(0).name());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,7,Animes;Desenhos",
            "1,2,2,7,Documentários;Esportes",
            "2,2,2,7,Filmes;Novelas",
            "3,2,1,7,Séries",
    })
    public void givenAValidPage_whenCallsListCategories_thenShouldReturnCategoriesPaginated(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedCategoriesName
    ) {
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedCategoriesNameArray = Arrays.asList(expectedCategoriesName.split(";"));

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        final var actualResult = useCase.execute(aQuery);

        Assertions.assertEquals(expectedItemsCount, actualResult.items().size());
        Assertions.assertEquals(expectedPage, actualResult.currentPage());
        Assertions.assertEquals(expectedPerPage, actualResult.perPage());
        Assertions.assertEquals(expectedTotal, actualResult.total());

        expectedCategoriesNameArray.forEach(categoryName -> {
            Assertions.assertTrue(actualResult.items().stream()
                    .anyMatch(category -> category.name().equals(categoryName)),
                    "Category with name " + categoryName + " not found in the result");
        });
    }

}
