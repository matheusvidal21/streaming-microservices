package com.codeflix.catalog.admin.infrastructure.infrastructure.api;

import com.codeflix.catalog.admin.application.category.create.CreateCategoryOutput;
import com.codeflix.catalog.admin.application.category.create.CreateCategoryUseCase;
import com.codeflix.catalog.admin.application.category.delete.DeleteCategoryUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.get.CategoryOutput;
import com.codeflix.catalog.admin.application.category.retrieve.get.GetCategoryByIdUseCase;
import com.codeflix.catalog.admin.application.category.retrieve.list.CategoryListOutput;
import com.codeflix.catalog.admin.application.category.retrieve.list.ListCategoriesUseCase;
import com.codeflix.catalog.admin.application.category.update.UpdateCategoryOutput;
import com.codeflix.catalog.admin.application.category.update.UpdateCategoryUseCase;
import com.codeflix.catalog.admin.domain.category.Category;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.exceptions.DomainException;
import com.codeflix.catalog.admin.domain.exceptions.NotFoundException;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.validation.Error;
import com.codeflix.catalog.admin.domain.validation.handler.Notification;
import com.codeflix.catalog.admin.infrastructure.ControllerTest;
import com.codeflix.catalog.admin.infrastructure.api.CategoryApi;
import com.codeflix.catalog.admin.infrastructure.category.models.CreateCategoryRequest;
import com.codeflix.catalog.admin.infrastructure.category.models.UpdateCategoryRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.API;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;
import java.util.Objects;

@ControllerTest(controllers = CategoryApi.class)
public class CategoryApiTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CreateCategoryUseCase createCategoryUseCase;

    @MockitoBean
    private GetCategoryByIdUseCase getCategoryByIdUseCase;

    @MockitoBean
    private UpdateCategoryUseCase updateCategoryUseCase;

    @MockitoBean
    private DeleteCategoryUseCase deleteCategoryUseCase;

    @MockitoBean
    private ListCategoriesUseCase listCategoriesUseCase;

    @Test
    public void givenAValidCommand_whenCallsCreateCategory_thenShouldReturnCategoryId() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var anInput = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(new CreateCategoryOutput("123")));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                        MockMvcResultMatchers.status().isCreated(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.header().string("Location", "/categories/123"),
                        MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo("123"))
        );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                        Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
                ));
    }

    @Test
    public void givenAnInvalidName_whenCallsCreateCategory_thenShouldReturnNotification() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var anInput = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),

                        MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
        );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    public void givenAnInvalidCommand_whenCallsCreateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final String expectedName = null;
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        final var anInput = new CreateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(createCategoryUseCase.execute(Mockito.any()))
                .thenThrow(DomainException.with(new Error(expectedErrorMessage)));

        // when
        final var request = MockMvcRequestBuilders.post("/categories")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                        MockMvcResultMatchers.status().isUnprocessableEntity(),
                        MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                        MockMvcResultMatchers.header().string("Location", Matchers.nullValue()),
                        MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)),
                        MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
        );

        Mockito.verify(createCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    public void givenAValidId_whenCallsGetCategoryById_thenShouldReturnCategory() throws Exception {
        // given
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        final var aCategory = Category.newCategory(expectedName, expectedDescription, expectedIsActive);
        final var expectedId = aCategory.getId().getValue();

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                        .thenReturn(CategoryOutput.from(aCategory));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId)),
                MockMvcResultMatchers.jsonPath("$.name", Matchers.equalTo(expectedName)),
                MockMvcResultMatchers.jsonPath("$.description", Matchers.equalTo(expectedDescription)),
                MockMvcResultMatchers.jsonPath("$.is_active", Matchers.equalTo(expectedIsActive)),
                MockMvcResultMatchers.jsonPath("$.created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())),
                MockMvcResultMatchers.jsonPath("$.updated_at", Matchers.equalTo(aCategory.getUpdatedAt().toString())),
                MockMvcResultMatchers.jsonPath("$.deleted_at", Matchers.equalTo(aCategory.getDeletedAt()))
        );

        Mockito.verify(getCategoryByIdUseCase, Mockito.times(1)).execute(expectedId);
    }

    @Test
    public void givenAnInvalidId_whenCallsGetCategoryById_thenShouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = CategoryID.from("123");
        final var expectedErrorMessage = "Category with ID 123 was not found";

        Mockito.when(getCategoryByIdUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, expectedId));

        // when
        final var request = MockMvcRequestBuilders.get("/categories/{id}", expectedId.getValue())
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage))
        );
    }

    @Test
    public void givenAValidCommand_whenCallsUpdateCategory_thenShouldReturnCategoryId() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var anInput = new UpdateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Right(UpdateCategoryOutput.from(expectedId)));

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.id", Matchers.equalTo(expectedId))
        );

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    public void givenACommandWithInvalidId_whenCallsUpdateCategory_thenShouldReturnNotFoundException() throws Exception {
        // given
        final var expectedId = "not-found";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var anInput = new UpdateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );
        final var expectedErrorMessage = "Category with ID not-found was not found";

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenThrow(NotFoundException.with(Category.class, CategoryID.from(expectedId)));

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isNotFound(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.message", Matchers.equalTo(expectedErrorMessage))
        );

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                Objects.equals(expectedId, cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    public void givenAnInvalidName_whenCallsUpdateCategory_thenShouldReturnDomainException() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;
        final var anInput = new UpdateCategoryRequest(
                expectedName,
                expectedDescription,
                expectedIsActive
        );
        final var expectedErrorMessage = "'name' should not be null";
        final var expectedErrorCount = 1;

        Mockito.when(updateCategoryUseCase.execute(Mockito.any()))
                .thenReturn(API.Left(Notification.create(new Error(expectedErrorMessage))));

        // when
        final var request = MockMvcRequestBuilders.put("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(anInput));

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isUnprocessableEntity(),
                MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON),
                MockMvcResultMatchers.jsonPath("$.errors", Matchers.hasSize(expectedErrorCount)),
                MockMvcResultMatchers.jsonPath("$.errors[0].message", Matchers.equalTo(expectedErrorMessage))
        );

        Mockito.verify(updateCategoryUseCase, Mockito.times(1)).execute(Mockito.argThat(cmd ->
                        Objects.equals(expectedId, cmd.id())
                        && Objects.equals(expectedName, cmd.name())
                        && Objects.equals(expectedDescription, cmd.description())
                        && Objects.equals(expectedIsActive, cmd.active())
        ));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCategory_thenShouldReturnNoContent() throws Exception {
        // given
        final var expectedId = "123";
        final var expectedName = "Filmes";
        final var expectedDescription = "A categoria mais assistida";
        final var expectedIsActive = true;

        Mockito.doNothing()
                .when(deleteCategoryUseCase).execute(Mockito.any());

        // when
        final var request = MockMvcRequestBuilders.delete("/categories/{id}", expectedId)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isNoContent()
        );

        Mockito.verify(deleteCategoryUseCase, Mockito.times(1)).execute(expectedId);
    }

    @Test
    public void givenValidParams_whenCallsListCategories_thenShouldReturnCategories() throws Exception {
        // given
        final var aCategory = Category.newCategory("Movies", "A categoria mais assistida", true);
        final var expectedPage = 0;
        final var expectedPerPage = 10;
        final var expectedTerms = "movies";
        final var expectedSort = "description";
        final var expectedDirection = "desc";
        final var expectedTotal = 1;
        final var expectedItems = List.of(
                CategoryListOutput.from(aCategory)
        );

        Mockito.when(listCategoriesUseCase.execute(Mockito.any()))
                .thenReturn(new Pagination<>(expectedPage, expectedPerPage, expectedTotal, expectedItems));

        // when
        final var request = MockMvcRequestBuilders.get("/categories")
                .queryParam("page", String.valueOf(expectedPage))
                .queryParam("perPage", String.valueOf(expectedPerPage))
                .queryParam("sort", expectedSort)
                .queryParam("dir", expectedDirection)
                .queryParam("search", expectedTerms)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON);

        final var response = this.mvc.perform(request)
                .andDo(MockMvcResultHandlers.print());

        // then
        response.andExpectAll(
                MockMvcResultMatchers.status().isOk(),
                MockMvcResultMatchers.jsonPath("$.current_page", Matchers.equalTo(expectedPage)),
                MockMvcResultMatchers.jsonPath("$.per_page", Matchers.equalTo(expectedPerPage)),
                MockMvcResultMatchers.jsonPath("$.total", Matchers.equalTo(expectedTotal)),
                MockMvcResultMatchers.jsonPath("$.items", Matchers.hasSize(expectedItems.size())),
                MockMvcResultMatchers.jsonPath("$.items[0].id", Matchers.equalTo(aCategory.getId().getValue())),
                MockMvcResultMatchers.jsonPath("$.items[0].name", Matchers.equalTo(aCategory.getName())),
                MockMvcResultMatchers.jsonPath("$.items[0].description", Matchers.equalTo(aCategory.getDescription())),
                MockMvcResultMatchers.jsonPath("$.items[0].is_active", Matchers.equalTo(aCategory.isActive())),
                MockMvcResultMatchers.jsonPath("$.items[0].created_at", Matchers.equalTo(aCategory.getCreatedAt().toString())),
                MockMvcResultMatchers.jsonPath("$.items[0].deleted_at", Matchers.equalTo(aCategory.getDeletedAt()))
        );

        Mockito.verify(listCategoriesUseCase, Mockito.times(1)).execute(Mockito.argThat(query ->
                    Objects.equals(expectedPage, query.page())
                    && Objects.equals(expectedPerPage, query.perPage())
                    && Objects.equals(expectedTerms, query.terms())
                    && Objects.equals(expectedSort, query.sort())
                    && Objects.equals(expectedDirection, query.direction())
        ));
    }

}
