package com.codeflix.catalog.admin.infrastructure.video;

import com.codeflix.catalog.admin.domain.base.Identifier;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.utils.CollectionUtils;
import com.codeflix.catalog.admin.domain.video.*;
import com.codeflix.catalog.admin.infrastructure.configuration.annotations.VideoCreatedQueue;
import com.codeflix.catalog.admin.infrastructure.services.EventService;
import com.codeflix.catalog.admin.infrastructure.utils.SqlUtils;
import com.codeflix.catalog.admin.infrastructure.video.persistence.VideoJpaEntity;
import com.codeflix.catalog.admin.infrastructure.video.persistence.VideoRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

public class DefaultVideoGateway implements VideoGateway {

    public final VideoRepository videoRepository;

    public final EventService eventService;

    public DefaultVideoGateway(final VideoRepository videoRepository, @VideoCreatedQueue final EventService eventService) {
        this.videoRepository = Objects.requireNonNull(videoRepository);
        this.eventService = Objects.requireNonNull(eventService);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Video> findById(final VideoID anId) {
        return this.videoRepository.findById(anId.getValue())
                .map(VideoJpaEntity::toAggregate);
    }

    @Override
    public Pagination<VideoPreview> findAll(final VideoSearchQuery aQuery) {
        final var page = PageRequest.of(
                aQuery.page(),
                aQuery.perPage(),
                Sort.by(Sort.Direction.fromString(aQuery.direction()), aQuery.sort())
        );

        final var actualPage = this.videoRepository.findAll(
                SqlUtils.like(aQuery.terms()),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(aQuery.castMembers(), Identifier::getValue)),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(aQuery.categories(), Identifier::getValue)),
                CollectionUtils.nullIfEmpty(CollectionUtils.mapTo(aQuery.genres(), Identifier::getValue)),
                page
        );

        return new Pagination<>(
                actualPage.getNumber(),
                actualPage.getSize(),
                actualPage.getTotalElements(),
                actualPage.toList()
        );
    }

    @Override
    @Transactional
    public Video create(final Video aVideo) {
        return this.save(aVideo);
    }

    @Override
    @Transactional
    public Video update(final Video aVideo) {
        return this.save(aVideo);
    }

    @Override
    public void deleteById(final VideoID anId) {
        final var aVideoId = anId.getValue();
        if (this.videoRepository.existsById(aVideoId)) {
            this.videoRepository.deleteById(aVideoId);
        }
    }

    private Video save(final Video aVideo) {
        final var result = this.videoRepository.save(VideoJpaEntity.from(aVideo)).toAggregate();

        aVideo.publishDomainEvents(this.eventService::send);

        return result;
    }

}
