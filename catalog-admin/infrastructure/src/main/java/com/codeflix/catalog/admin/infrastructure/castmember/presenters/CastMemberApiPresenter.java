package com.codeflix.catalog.admin.infrastructure.castmember.presenters;

import com.codeflix.catalog.admin.application.castmember.retrivie.get.CastMemberOutput;
import com.codeflix.catalog.admin.application.castmember.retrivie.list.CastMemberListOutput;
import com.codeflix.catalog.admin.infrastructure.castmember.models.CastMemberListResponse;
import com.codeflix.catalog.admin.infrastructure.castmember.models.CastMemberResponse;

public interface CastMemberApiPresenter {

    static CastMemberResponse present(final CastMemberOutput aMember) {
        return new CastMemberResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString(),
                aMember.updatedAt().toString()
        );
    }

    static CastMemberListResponse present(final CastMemberListOutput aMember) {
        return new CastMemberListResponse(
                aMember.id(),
                aMember.name(),
                aMember.type().name(),
                aMember.createdAt().toString()
        );
    }

}