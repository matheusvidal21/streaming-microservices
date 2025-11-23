package com.codeflix.catalog.admin.infrastructure;

import com.codeflix.catalog.admin.infrastructure.category.persistence.CategoryRepository;
import com.codeflix.catalog.admin.infrastructure.genre.persistence.GenreRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class MySQLCleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        final var appContext = SpringExtension.getApplicationContext(context);
        cleanUp(List.of(
                appContext.getBean(GenreRepository.class),
                appContext.getBean(CategoryRepository.class)
        ));

        final var em = appContext.getBean(EntityManager.class);
        em.clear();
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }

}