package com.codeflix.catalog.admin.domain.castmember;

import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;

import java.util.List;
import java.util.Optional;

public interface CastMemberGateway {

    Optional<CastMember> findById(CastMemberID anId);

    Pagination<CastMember> findAll(SearchQuery aQuery);

    CastMember create(CastMember aMember);

    CastMember update(CastMember aMember);

    void deleteById(CastMemberID anId);

    List<CastMemberID> existsByIds(Iterable<CastMemberID> castMemberIDs);

}
