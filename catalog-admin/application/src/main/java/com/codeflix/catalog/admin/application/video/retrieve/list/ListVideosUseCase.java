package com.codeflix.catalog.admin.application.video.retrieve.list;

import com.codeflix.catalog.admin.application.base.UseCase;
import com.codeflix.catalog.admin.domain.pagination.Pagination;
import com.codeflix.catalog.admin.domain.video.VideoSearchQuery;

public abstract class ListVideosUseCase
        extends UseCase<VideoSearchQuery, Pagination<VideoListOutput>> {
}