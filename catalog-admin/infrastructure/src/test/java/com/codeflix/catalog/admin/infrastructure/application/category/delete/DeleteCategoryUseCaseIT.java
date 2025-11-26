package com.codeflix.catalog.admin.infrastructure.application.category.delete;

import com.codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.catalog.admin.domain.category.Category;
import com.codeflix.catalog.admin.domain.category.CategoryGateway;
import com.codeflix.catalog.admin.domain.category.CategoryID;
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
public class DeleteCategoryUseCaseIT {

    @Autowired
    private DeleteCategoryUseCase useCase;

    @Autowired
    private CategoryRepository categoryRepository;

    @MockitoSpyBean
    private CategoryGateway categoryGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCategory_thenShouldBeOK() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        save(aCategory);

        Assertions.assertEquals(1, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(aCategory.getId().getValue()));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCategory_thenShouldBeOK() {
        final var expectedId = CategoryID.from("123");

        Assertions.assertEquals(0, categoryRepository.count());

        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        Assertions.assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenAValidId_whenGatewayThrowsException_thenShouldReturnException() {
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);
        final var expectedId = aCategory.getId();
        final var expectedErrorMessage = "Gateway error";

        Mockito.doThrow(new IllegalStateException(expectedErrorMessage))
                .when(categoryGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));
    }

    private void save(final Category... categories) {
        categoryRepository.saveAllAndFlush(
                Arrays.stream(categories)
                        .map(CategoryJpaEntity::from)
                        .toList());
    }

}
