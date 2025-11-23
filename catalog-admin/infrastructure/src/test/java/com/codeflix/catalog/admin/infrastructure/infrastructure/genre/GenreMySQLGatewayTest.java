package com.codeflix.catalog.admin.infrastructure.infrastructure.genre;

import com.codeflix.catalog.admin.domain.category.Category;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.genre.Genre;
import com.codeflix.catalog.admin.domain.genre.GenreID;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import com.codeflix.catalog.admin.infrastructure.MySQLGatewayTest;
import com.codeflix.catalog.admin.infrastructure.category.CategoryMySQLGateway;
import com.codeflix.catalog.admin.infrastructure.genre.GenreMySQLGateway;
import com.codeflix.catalog.admin.infrastructure.genre.persistence.GenreJpaEntity;
import com.codeflix.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Import;

import java.util.Comparator;
import java.util.List;

@MySQLGatewayTest
@Import({GenreMySQLGateway.class, CategoryMySQLGateway.class})
public class GenreMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private GenreMySQLGateway genreGateway;

    @Autowired
    private GenreRepository genreRepository;

    @Test
    public void testDependenciesInjected() {
        Assertions.assertNotNull(categoryGateway);
        Assertions.assertNotNull(genreGateway);
        Assertions.assertNotNull(genreRepository);
    }

    @Test
    public void givenAValidGenre_whenCallsCreateGenre_thenShouldPersistGenre() {
        // given
        final var filmes = this.categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();
        aGenre.addCategories(expectedCategories);

        Assertions.assertEquals(0, genreRepository.count());
        // when
        final var actualGenre = this.genreGateway.create(aGenre);

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(persitedGenre);
        Assertions.assertEquals(expectedId.getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persitedGenre.getCategoryIDs());
        Assertions.assertEquals(expectedCategories.size(), persitedGenre.getCategoryIDs().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persitedGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), persitedGenre.getDeletedAt());
        Assertions.assertNull(persitedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsCreateGenre_thenShouldPersistGenre() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        final var expectedId = aGenre.getId();

        Assertions.assertEquals(0, genreRepository.count());
        // when
        final var actualGenre = this.genreGateway.create(aGenre);

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(persitedGenre);
        Assertions.assertEquals(expectedId.getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persitedGenre.getCategoryIDs());
        Assertions.assertEquals(expectedCategories.size(), persitedGenre.getCategoryIDs().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), persitedGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), persitedGenre.getDeletedAt());
        Assertions.assertNull(persitedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithoutCategories_whenCallsUpdateGenreWithCategories_thenShouldPersistGenre() {
        // given
        final var filmes = this.categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = this.categoryGateway.create(Category.newCategory("Séries", null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(0, aGenre.getCategories().size());

        // when
        final var actualGenre = this.genreGateway.update(
                aGenre.clone()
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(persitedGenre);
        Assertions.assertEquals(expectedId.getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(persitedGenre.getCategoryIDs()));
        Assertions.assertEquals(expectedCategories.size(), persitedGenre.getCategoryIDs().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persitedGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), persitedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreWithCategories_whenCallsUpdateGenreCleaningCategories_thenShouldPersistGenre() {
        // given
        final var filmes = this.categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = this.categoryGateway.create(Category.newCategory("Séries", null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre("ac", expectedIsActive);
        aGenre.addCategories(List.of(filmes.getId(), series.getId()));
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertEquals("ac", aGenre.getName());
        Assertions.assertEquals(2, aGenre.getCategories().size());

        // when
        final var actualGenre = this.genreGateway.update(
                aGenre.clone()
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(persitedGenre);
        Assertions.assertEquals(expectedId.getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persitedGenre.getCategoryIDs());
        Assertions.assertEquals(expectedCategories.size(), persitedGenre.getCategoryIDs().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persitedGenre.getUpdatedAt()));
        Assertions.assertEquals(aGenre.getDeletedAt(), persitedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreInactive_whenCallsUpdateGenreActivating_thenShouldPersistGenre() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, false);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertFalse(aGenre.isActive());
        Assertions.assertNotNull(aGenre.getDeletedAt());

        // when
        final var actualGenre = this.genreGateway.update(
                aGenre.clone()
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNull(actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(persitedGenre);
        Assertions.assertEquals(expectedId.getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persitedGenre.getCategoryIDs());
        Assertions.assertEquals(expectedCategories.size(), persitedGenre.getCategoryIDs().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persitedGenre.getUpdatedAt()));
        Assertions.assertNull(persitedGenre.getDeletedAt());
    }

    @Test
    public void givenAValidGenreActive_whenCallsUpdateGenreInactivating_thenShouldPersistGenre() {
        // given
        final var expectedName = "Ação";
        final var expectedIsActive = false;
        final var expectedCategories = List.<CategoryID>of();

        final var aGenre = Genre.newGenre(expectedName, true);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals(1, genreRepository.count());
        Assertions.assertTrue(aGenre.isActive());
        Assertions.assertNull(aGenre.getDeletedAt());

        // when
        final var actualGenre = this.genreGateway.update(
                aGenre.clone()
                        .update(expectedName, expectedIsActive, expectedCategories)
        );

        // then
        Assertions.assertEquals(1, genreRepository.count());

        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(expectedCategories, actualGenre.getCategories());
        Assertions.assertEquals(expectedCategories.size(), actualGenre.getCategories().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(actualGenre.getUpdatedAt()));
        Assertions.assertNotNull(actualGenre.getDeletedAt());

        final var persitedGenre = genreRepository.findById(expectedId.getValue()).get();

        Assertions.assertNotNull(persitedGenre);
        Assertions.assertEquals(expectedId.getValue(), persitedGenre.getId());
        Assertions.assertEquals(expectedName, persitedGenre.getName());
        Assertions.assertEquals(expectedIsActive, persitedGenre.isActive());
        Assertions.assertEquals(expectedCategories, persitedGenre.getCategoryIDs());
        Assertions.assertEquals(expectedCategories.size(), persitedGenre.getCategoryIDs().size());
        Assertions.assertEquals(aGenre.getCreatedAt(), persitedGenre.getCreatedAt());
        Assertions.assertTrue(aGenre.getUpdatedAt().isBefore(persitedGenre.getUpdatedAt()));
        Assertions.assertNotNull(persitedGenre.getDeletedAt());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsDeleteById_thenShouldDeleteGenre() {
        // given
        final var aGenre = Genre.newGenre("Ação", true);

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals(1, genreRepository.count());

        // when
        this.genreGateway.deleteById(aGenre.getId());

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAnInvalidGenre_whenCallsDeleteById_thenShouldOk() {
        // given
        Assertions.assertEquals(0, genreRepository.count());

        // when
        this.genreGateway.deleteById(GenreID.from("123"));

        // then
        Assertions.assertEquals(0, genreRepository.count());
    }

    @Test
    public void givenAPrePersistedGenre_whenCallsFindById_thenShouldReturnGenre() {
        // given
        final var filmes = this.categoryGateway.create(Category.newCategory("Filmes", null, true));
        final var series = this.categoryGateway.create(Category.newCategory("Séries", null, true));
        final var expectedName = "Ação";
        final var expectedIsActive = true;
        final var expectedCategories = List.of(filmes.getId(), series.getId());

        final var aGenre = Genre.newGenre(expectedName, expectedIsActive);
        aGenre.addCategories(expectedCategories);
        final var expectedId = aGenre.getId();

        genreRepository.saveAndFlush(GenreJpaEntity.from(aGenre));
        Assertions.assertEquals(1, genreRepository.count());

        // when
        final var actualGenre = this.genreGateway.findById(expectedId).get();

        // then
        Assertions.assertEquals(expectedId, actualGenre.getId());
        Assertions.assertEquals(expectedName, actualGenre.getName());
        Assertions.assertEquals(expectedIsActive, actualGenre.isActive());
        Assertions.assertEquals(sorted(expectedCategories), sorted(actualGenre.getCategories()));
        Assertions.assertEquals(aGenre.getCreatedAt(), actualGenre.getCreatedAt());
        Assertions.assertEquals(aGenre.getUpdatedAt(), actualGenre.getUpdatedAt());
        Assertions.assertEquals(aGenre.getDeletedAt(), actualGenre.getDeletedAt());
        Assertions.assertNull(actualGenre.getDeletedAt());
    }

    @Test
    public void givenAInvalidGenreId_whenCallsFindById_thenShouldReturnEmpty() {
        // given
        final var expectedId = GenreID.from("123");

        Assertions.assertEquals(0, genreRepository.count());

        // when
        final var actualGenre = this.genreGateway.findById(expectedId);

        // then
        Assertions.assertTrue(actualGenre.isEmpty());
    }

    @Test
    public void givenEmptyGenres_whenCallsFindAll_thenShouldReturnEmptyList() {
        // given
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var expectedTotal = 0;

        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Assertions.assertEquals(0, genreRepository.count());

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedTotal, actualPage.items().size());
        Assertions.assertTrue(actualPage.items().isEmpty());
    }

    @ParameterizedTest
    @CsvSource({
            "aç,0,10,1,1,Ação",
            "dr,0,10,1,1,Drama",
            "co,0,10,1,1,Comédia Romântica",
            "cien,0,10,1,1,Ficção Científica",
            "terr,0,10,1,1,Terror",
    })
    public void givenAValidTerm_whenCallsFindAll_thenShouldReturnFiltered(
            final String expectedTerms,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        // given
        mockGenres();
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Assertions.assertEquals(5, genreRepository.count());

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "name,asc,0,10,5,5,Ação",
            "name,desc,0,10,5,5,Terror",
            "createdAt,asc,0,10,5,5,Comédia Romântica",
            "createdAt,desc,0,10,5,5,Ficção Científica",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_thenShouldReturnFiltered(
            final String expectedSort,
            final String expectedDirection,
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenreName
    ) {
        // given
        mockGenres();
        final var expectedTerms = "";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Assertions.assertEquals(5, genreRepository.count());

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());
        Assertions.assertEquals(expectedGenreName, actualPage.items().get(0).getName());
    }

    @ParameterizedTest
    @CsvSource({
            "0,2,2,5,Ação;Comédia Romântica",
            "1,2,2,5,Drama;Ficção Científica",
            "2,2,1,5,Terror",
    })
    public void givenAValidSortAndDirection_whenCallsFindAll_thenShouldReturnFiltered(
            final int expectedPage,
            final int expectedPerPage,
            final int expectedItemsCount,
            final long expectedTotal,
            final String expectedGenres
    ) {
        // given
        mockGenres();
        final var expectedTerms = "";
        final var expectedSort = "name";
        final var expectedDirection = "asc";
        final var aQuery = new SearchQuery(expectedPage, expectedPerPage, expectedTerms, expectedSort, expectedDirection);

        Assertions.assertEquals(5, genreRepository.count());

        // when
        final var actualPage = genreGateway.findAll(aQuery);

        // then
        Assertions.assertEquals(expectedPage, actualPage.currentPage());
        Assertions.assertEquals(expectedPerPage, actualPage.perPage());
        Assertions.assertEquals(expectedTotal, actualPage.total());
        Assertions.assertEquals(expectedItemsCount, actualPage.items().size());

        int index = 0;
        for (String expectedGenreName : expectedGenres.split(";")) {
            final var actualName = actualPage.items().get(index).getName();
            Assertions.assertEquals(expectedGenreName, actualName);
            index++;
        }
    }

    private void mockGenres() {
        final var genres = List.of(
                GenreJpaEntity.from(Genre.newGenre("Comédia Romântica", true)),
                GenreJpaEntity.from(Genre.newGenre("Ação", true)),
                GenreJpaEntity.from(Genre.newGenre("Drama", true)),
                GenreJpaEntity.from(Genre.newGenre("Terror", true)),
                GenreJpaEntity.from(Genre.newGenre("Ficção Científica", true))
        );
        genreRepository.saveAllAndFlush(genres);
    }

    private List<CategoryID> sorted(List<CategoryID> categories) {
        return categories.stream()
                .sorted(Comparator.comparing(CategoryID::getValue))
                .toList();
    }

}
