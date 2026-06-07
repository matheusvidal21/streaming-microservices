package com.codeflix.catalog.admin.application.video.create;

import com.codeflix.catalog.admin.domain.video.Video;

public record CreateVideoOutput(String id) {

    public static CreateVideoOutput from(final Video aVideo) {
        return new CreateVideoOutput(aVideo.getId().getValue());
    }
}