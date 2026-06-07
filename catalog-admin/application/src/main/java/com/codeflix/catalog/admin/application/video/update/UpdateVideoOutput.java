package com.codeflix.catalog.admin.application.video.update;

import com.codeflix.catalog.admin.domain.video.Video;

public record UpdateVideoOutput(String id) {

    public static UpdateVideoOutput from(final Video aVideo) {
        return new UpdateVideoOutput(aVideo.getId().getValue());
    }
}