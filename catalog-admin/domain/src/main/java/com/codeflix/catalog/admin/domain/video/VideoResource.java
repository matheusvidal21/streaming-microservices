package com.codeflix.catalog.admin.domain.video;

import com.codeflix.catalog.admin.domain.resource.Resource;

public record VideoResource(
        VideoMediaType type,
        Resource resource
) {

    public static VideoResource with(final VideoMediaType type, final Resource resource) {
        return new VideoResource(type, resource);
    }
}