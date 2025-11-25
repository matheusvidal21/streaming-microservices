package com.codeflix.catalog.admin.application.castmember.retrivie.get;

import com.codeflix.catalog.admin.domain.castmember.CastMember;
import com.codeflix.catalog.admin.domain.castmember.CastMemberGateway;
import com.codeflix.catalog.admin.domain.castmember.CastMemberID;
import com.codeflix.catalog.admin.domain.exceptions.NotFoundException;

import java.util.Objects;
import java.util.function.Supplier;

public final class DefaultGetCastMemberByIdUseCase extends GetCastMemberByIdUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultGetCastMemberByIdUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CastMemberOutput execute(String anId) {
        final var aMemberId = CastMemberID.from(anId);
        return this.castMemberGateway.findById(aMemberId)
                .map(CastMemberOutput::from)
                .orElseThrow(notFound(aMemberId));
    }

    private static Supplier<NotFoundException> notFound(final CastMemberID anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }

}
