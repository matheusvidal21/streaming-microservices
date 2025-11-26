package com.codeflix.catalog.admin.infrastructure.castmember;

import com.codeflix.catalog.admin.domain.castmember.CastMember;
import com.codeflix.catalog.admin.domain.castmember.CastMemberGateway;
import com.codeflix.catalog.admin.domain.castmember.CastMemberID;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.pagination.SearchQuery;
import com.codeflix.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import com.codeflix.catalog.admin.infrastructure.utils.SpecificationUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    public CastMemberMySQLGateway(final CastMemberRepository castMemberRepository) {
        this.castMemberRepository = Objects.requireNonNull(castMemberRepository);
    }

    @Override
    public Optional<CastMember> findById(final CastMemberID anId) {
        return this.castMemberRepository.findById(anId.getValue())
                .map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CastMember> findAll(final SearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final Specification<CastMemberJpaEntity> where = Optional.ofNullable(aQuery.terms())
                .filter(str -> !str.isBlank())
                .map(this::assembleSpecification)
                .orElse(null);

        final var pageResult = this.castMemberRepository.findAll(where, page);

        return new Pagination<>(
                pageResult.getNumber(),
                pageResult.getSize(),
                pageResult.getTotalElements(),
                pageResult.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public CastMember create(final CastMember aMember) {
        return save(aMember);
    }

    @Override
    public CastMember update(final CastMember aMember) {
        return save(aMember);
    }

    @Override
    public void deleteById(final CastMemberID anId) {
        final var aMemberId = anId.getValue();
        if (this.castMemberRepository.existsById(aMemberId)) {
            this.castMemberRepository.deleteById(aMemberId);
        }
    }

    @Override
    public List<CastMemberID> existsByIds(final Iterable<CastMemberID> castMemberIDs) {
        final var ids = StreamSupport.stream(castMemberIDs.spliterator(), false)
                .map(CastMemberID::getValue)
                .toList();
        return this.castMemberRepository.existsByIds(ids).stream()
                .map(CastMemberID::from)
                .toList();
    }

    private CastMember save(final CastMember aMember) {
        return this.castMemberRepository
                .save(CastMemberJpaEntity.from(aMember))
                .toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }

}
