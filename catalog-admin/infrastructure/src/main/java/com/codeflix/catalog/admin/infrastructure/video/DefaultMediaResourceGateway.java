package com.codeflix.catalog.admin.infrastructure.video;

import com.codeflix.catalog.admin.domain.resource.Resource;
import com.codeflix.catalog.admin.domain.video.*;
import com.codeflix.catalog.admin.infrastructure.configuration.properties.StorageProperties;
import com.codeflix.catalog.admin.infrastructure.services.StorageService;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DefaultMediaResourceGateway implements MediaResourceGateway {

    private final String filenamePattern;

    private final String locationPattern;

    private final StorageService storageService;

    public DefaultMediaResourceGateway(final StorageProperties props, final StorageService storageService) {
        this.filenamePattern = props.getFilenamePattern();
        this.locationPattern = props.getLocationPattern();
        this.storageService = storageService;
    }

    @Override
    public AudioVideoMedia storeAudioVideo(final VideoID anId, final VideoResource videoResource) {
        final var filepath = this.filepath(anId, videoResource.type());
        final var aResource = videoResource.resource();

        this.store(filepath, aResource);

        return AudioVideoMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public ImageMedia storeImage(final VideoID anId, final VideoResource videoResource) {
        final var filepath = this.filepath(anId, videoResource.type());
        final var aResource = videoResource.resource();

        this.store(filepath, aResource);

        return ImageMedia.with(aResource.checksum(), aResource.name(), filepath);
    }

    @Override
    public Optional<Resource> getResource(final VideoID anId, final VideoMediaType aType) {
        return this.storageService.get(this.filepath(anId, aType));
    }

    @Override
    public void clearResources(final VideoID anId) {
        final var ids = this.storageService.list(this.folder(anId));
        this.storageService.deleteAll(ids);
    }

    private String filename(final VideoMediaType aType) {
        return this.filenamePattern.replace("{type}", aType.name());
    }

    private String folder(final VideoID anId) {
        return this.locationPattern.replace("{videoId}", anId.getValue());
    }

    private String filepath(final VideoID anId, final VideoMediaType aType) {
        return this.folder(anId)
                .concat("/")
                .concat(this.filename(aType));
    }

    private void store(final String filepath, final Resource aResource) {
        this.storageService.store(filepath, aResource);
    }

}
