package com.codeflix.catalog.admin.domain.video;

import com.codeflix.catalog.admin.domain.pagination.Pagination;

import java.util.Optional;

public interface VideoGateway {

    Optional<Video> findById(VideoID anId);

    Pagination<Video> findAll(VideoSearchQuery aQuery);

    Video create(Video aVideo);

    Video update(Video aVideo);

    void deleteById(VideoID anId);

}
