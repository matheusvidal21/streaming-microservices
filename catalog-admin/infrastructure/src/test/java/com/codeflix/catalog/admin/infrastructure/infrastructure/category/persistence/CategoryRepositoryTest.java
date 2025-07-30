package com.codeflix.catalog.admin.infrastructure.infrastructure.category.persistence;

import com.codeflix.catalog.admin.domain.category.Category;
import com.codeflix.catalog.admin.infrastructure.MySQLGatewayTest;
import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryJpaEntity;
import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenAnInvalidNullName_whenCallsSave_thenShouldReturnException() {
        final var expectedExceptionMessage = "NULL not allowed for column \"NAME\"; SQL statement:\n" +
                "insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?) [23502-232]";
        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setName(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.saveAndFlush(anEntity));

        final var actualCause = Assertions.assertInstanceOf(ConstraintViolationException.class, actualException.getCause());

        Assertions.assertEquals(expectedExceptionMessage, actualCause.getCause().getMessage());
    }

    @Test
    public void givenAnInvalidNullCreatedAt_whenCallsSave_thenShouldReturnException() {
        final var expectedExceptionMessage = "NULL not allowed for column \"CREATED_AT\"; SQL statement:\n" +
                "insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?) [23502-232]";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setCreatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.saveAndFlush(anEntity));

        final var actualCause = Assertions.assertInstanceOf(ConstraintViolationException.class, actualException.getCause());

        Assertions.assertEquals(expectedExceptionMessage, actualCause.getCause().getMessage());
    }

    @Test
    public void givenAnInvalidNullUpdatedAt_whenCallsSave_thenShouldReturnException() {
        final var expectedExceptionMessage = "NULL not allowed for column \"UPDATED_AT\"; SQL statement:\n" +
                "insert into category (active,created_at,deleted_at,description,name,updated_at,id) values (?,?,?,?,?,?,?) [23502-232]";

        final var aCategory = Category.newCategory("Filmes", "A categoria mais assistida", true);

        final var anEntity = CategoryJpaEntity.from(aCategory);
        anEntity.setUpdatedAt(null);

        final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.saveAndFlush(anEntity));

        final var actualCause = Assertions.assertInstanceOf(ConstraintViolationException.class, actualException.getCause());

        Assertions.assertEquals(expectedExceptionMessage, actualCause.getCause().getMessage());

    }

}
