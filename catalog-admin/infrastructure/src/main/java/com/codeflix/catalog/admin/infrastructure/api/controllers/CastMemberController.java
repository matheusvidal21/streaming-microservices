package com.codeflix.catalog.admin.infrastructure.api.controllers;


import com.codeflix.catalog.admin.application.castmember.create.CreateCastMemberCommand;
import com.codeflix.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.codeflix.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.codeflix.catalog.admin.application.castmember.retrivie.get.GetCastMemberByIdUseCase;
import com.codeflix.catalog.admin.application.castmember.retrivie.list.ListCastMembersUseCase;
import com.codeflix.catalog.admin.application.castmember.update.UpdateCastMemberCommand;
import com.codeflix.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import com.codeflix.catalog.admin.infrastructure.api.CastMemberApi;
import com.codeflix.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import com.codeflix.catalog.admin.infrastructure.castmember.models.CastMemberResponse;
import com.codeflix.catalog.admin.infrastructure.castmember.models.CreateCastMemberRequest;
import com.codeflix.catalog.admin.infrastructure.castmember.models.UpdateCastMemberRequest;
import com.codeflix.catalog.admin.infrastructure.castmember.presenters.CastMemberApiPresenter;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;

@RestController
public class CastMemberController implements CastMemberApi {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
            final CreateCastMemberUseCase createCastMemberUseCase,
            final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
            final UpdateCastMemberUseCase updateCastMemberUseCase,
            final DeleteCastMemberUseCase deleteCastMemberUseCase,
            final ListCastMembersUseCase listCastMembersUseCase
    ) {
        this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
        this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
        this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
        this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
        this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
    }

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand =
                CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listCastMembersUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberApiPresenter::present);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberApiPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
        final var aCommand =
                UpdateCastMemberCommand.with(id, aBody.name(), aBody.type());

        final var output = this.updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }

}