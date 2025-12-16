package com.codeflix.catalog.admin.domain.video;

import com.codeflix.catalog.admin.domain.base.AggregateRoot;
import com.codeflix.catalog.admin.domain.castmember.CastMemberID;
import com.codeflix.catalog.admin.domain.category.CategoryID;
import com.codeflix.catalog.admin.domain.genre.GenreID;
import com.codeflix.catalog.admin.domain.utils.InstantUtils;
import com.codeflix.catalog.admin.domain.validation.ValidationHandler;

import java.time.Instant;
import java.time.Year;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class Video extends AggregateRoot<VideoID> implements Cloneable {

    private String title;
    private String description;
    private Year launchedAt;
    private Double duration;
    private Rating rating;

    private boolean opened;
    private boolean published;

    private Instant createdAt;
    private Instant updatedAt;

    private ImageMedia banner;
    private ImageMedia thumbnail;
    private ImageMedia thumbnailHalf;

    private AudioVideoMedia trailer;
    private AudioVideoMedia video;

    private Set<CategoryID> categories;
    private Set<GenreID> genres;
    private Set<CastMemberID> members;

    protected Video(
            final VideoID anId,
            final String aTitle,
            final String aDescription,
            final Year aLaunchedYear,
            final Double aDuration,
            final Rating aRating,
            final boolean wasOpened,
            final boolean wasPublished,
            final Instant aCreationDate,
            final Instant anUpdateDate,
            final ImageMedia aBanner,
            final ImageMedia aThumbnail,
            final ImageMedia aThumbnailHalf,
            final AudioVideoMedia aTrailer,
            final AudioVideoMedia aVideo,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        super(anId);
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedYear;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.createdAt = aCreationDate;
        this.updatedAt = anUpdateDate;
        this.banner = aBanner;
        this.thumbnail = aThumbnail;
        this.thumbnailHalf = aThumbnailHalf;
        this.trailer = aTrailer;
        this.video = aVideo;
        this.categories = categories;
        this.genres = genres;
        this.members = members;
    }

    public static Video newVideo(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedYear,
            final Double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        final var now = InstantUtils.now();
        final var anId = VideoID.unique();
        return new Video(
                anId,
                aTitle,
                aDescription,
                aLaunchedYear,
                aDuration,
                aRating,
                wasOpened,
                wasPublished,
                now,
                now,
                null,
                null,
                null,
                null,
                null,
                categories,
                genres,
                members
        );
    }

    @Override
    public void validate(ValidationHandler handler) {
        new VideoValidator(this, handler).validate();
    }

    @Override
    public Video clone() {
        try {
            Video clone = (Video) super.clone();
            clone.categories = new HashSet<>(this.getCategories());
            clone.genres = new HashSet<>(this.getGenres());
            clone.members = new HashSet<>(this.getMembers());
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    public Video update(
            final String aTitle,
            final String aDescription,
            final Year aLaunchedYear,
            final Double aDuration,
            final boolean wasOpened,
            final boolean wasPublished,
            final Rating aRating,
            final Set<CategoryID> categories,
            final Set<GenreID> genres,
            final Set<CastMemberID> members
    ) {
        this.title = aTitle;
        this.description = aDescription;
        this.launchedAt = aLaunchedYear;
        this.duration = aDuration;
        this.rating = aRating;
        this.opened = wasOpened;
        this.published = wasPublished;
        this.setCategories(categories);
        this.setGenres(genres);
        this.setMembers(members);
        this.updatedAt = InstantUtils.now();
        return this;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Year getLaunchedAt() {
        return launchedAt;
    }

    public Double getDuration() {
        return duration;
    }

    public Rating getRating() {
        return rating;
    }

    public boolean getOpened() {
        return opened;
    }

    public boolean getPublished() {
        return published;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Optional<ImageMedia> getBanner() {
        return Optional.ofNullable(banner);
    }

    public Optional<ImageMedia> getThumbnail() {
        return Optional.ofNullable(thumbnail);
    }

    public Optional<ImageMedia> getThumbnailHalf() {
        return Optional.ofNullable(thumbnailHalf);
    }

    public Optional<AudioVideoMedia> getTrailer() {
        return Optional.ofNullable(trailer);
    }

    public Optional<AudioVideoMedia> getVideo() {
        return Optional.ofNullable(video);
    }

    public Set<CategoryID> getCategories() {
        return categories != null ? Collections.unmodifiableSet(categories) : Collections.emptySet();
    }

    public Set<GenreID> getGenres() {
        return genres != null ? Collections.unmodifiableSet(genres) : Collections.emptySet();
    }

    public Set<CastMemberID> getMembers() {
        return members != null ? Collections.unmodifiableSet(members) : Collections.emptySet();
    }

    public void setBanner(final ImageMedia banner) {
        this.banner = banner;
        this.updatedAt = InstantUtils.now();
    }

    public void setThumbnail(final ImageMedia thumbnail) {
        this.thumbnail = thumbnail;
        this.updatedAt = InstantUtils.now();
    }

    public void setThumbnailHalf(final ImageMedia thumbnailHalf) {
        this.thumbnailHalf = thumbnailHalf;
        this.updatedAt = InstantUtils.now();
    }

    public void setTrailer(final AudioVideoMedia trailer) {
        this.trailer = trailer;
        this.updatedAt = InstantUtils.now();
    }

    public void setVideo(final AudioVideoMedia video) {
        this.video = video;
        this.updatedAt = InstantUtils.now();
    }

    private void setCategories(final Set<CategoryID> categories) {
        this.categories = categories != null ? new HashSet<>(categories) : Collections.emptySet();
    }

    private void setGenres(final Set<GenreID> genres) {
        this.genres = genres != null ? new HashSet<>(genres) : Collections.emptySet();
    }

    private void setMembers(Set<CastMemberID> members) {
        this.members = members != null ? new HashSet<>(members) : Collections.emptySet();
    }



}
