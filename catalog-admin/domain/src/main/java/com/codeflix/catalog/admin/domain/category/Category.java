package com.codeflix.catalog.admin.domain.category;


import com.codeflix.catalog.admin.domain.base.AggregateRoot;
import com.codeflix.catalog.admin.domain.validation.ValidationHandler;

import java.time.Instant;
import java.util.Objects;

public class Category extends AggregateRoot<CategoryID> implements Cloneable {

    private String name;
    private String description;
    private boolean active;
    private Instant createdAt;
    private Instant updatedAt;
    private Instant deletedAt;

    private Category(final CategoryID anId, final String aName, final String aDescription, final boolean isActive, final Instant aCreationDate, final Instant aUpdateDate, final Instant aDeleteDate) {
        super(anId);
        this.name = aName;
        this.description = aDescription;
        this.active = isActive;
        this.createdAt = Objects.requireNonNull(aCreationDate, "'createdAt' should not be null");
        this.updatedAt = Objects.requireNonNull(aUpdateDate, "'updatedAt' should not be null");
        this.deletedAt = aDeleteDate;
    }

    public static Category newCategory(final String aName, final String aDescription, final boolean isActive) {
        CategoryID id = CategoryID.unique();
        Instant now = Instant.now();
        final Instant deletedAt = isActive ? null : now;
        return new Category(id, aName, aDescription, isActive, now, now, deletedAt);
    }

    public static Category with(final CategoryID anId, final String aName, final String aDescription, final boolean isActive, final Instant aCreationDate, final Instant aUpdateDate, final Instant aDeleteDate) {
        return new Category(
                anId,
                aName,
                aDescription,
                isActive,
                aCreationDate,
                aUpdateDate,
                aDeleteDate
        );
    }

    @Override
    public void validate(final ValidationHandler handler) {
        new CategoryValidator(this, handler).validate();
    }

    public Category activate() {
        this.active = true;
        this.deletedAt = null;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category deactivate() {
        if (getDeletedAt() == null) {
            this.deletedAt = Instant.now();
        }

        this.active = false;
        this.updatedAt = Instant.now();
        return this;
    }

    public Category update(final String aName, final String aDescription, final boolean isActive) {
        if (isActive) {
            activate();
        } else {
            deactivate();
        }

        this.name = aName;
        this.description = aDescription;
        this.updatedAt = Instant.now();
        return this;
    }

    public CategoryID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isActive() {
        return active;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public Instant getDeletedAt() {
        return deletedAt;
    }

    @Override
    public Category clone() {
        try {
            return (Category) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}