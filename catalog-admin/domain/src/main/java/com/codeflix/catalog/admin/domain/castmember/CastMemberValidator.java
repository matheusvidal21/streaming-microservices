package com.codeflix.catalog.admin.domain.castmember;

import com.codeflix.catalog.admin.domain.validation.Error;
import com.codeflix.catalog.admin.domain.validation.ValidationHandler;
import com.codeflix.catalog.admin.domain.validation.Validator;

public class CastMemberValidator extends Validator {

    private final CastMember castMember;

    private static final int NAME_MIN_LENGTH = 3;
    private static final int NAME_MAX_LENGTH = 255;

    protected CastMemberValidator(final CastMember aMember, final ValidationHandler aHandler) {
        super(aHandler);
        this.castMember = aMember;
    }

    @Override
    public void validate() {
        checkNameConstraints();
        checkTypeConstraints();
    }

    private void checkNameConstraints() {
        final String name = this.castMember.getName();

        if (name == null) {
            this.validationHandler().append(new com.codeflix.catalog.admin.domain.validation.Error("'name' should not be null"));
            return;
        }
        if (name.isBlank()) {
            this.validationHandler().append(new com.codeflix.catalog.admin.domain.validation.Error("'name' should not be empty"));
            return;
        }
        final int length = name.trim().length();
        if (length > NAME_MAX_LENGTH || length < NAME_MIN_LENGTH) {
            this.validationHandler().append(new Error("'name' must be between 3 and 255 characters"));
            return;
        }
    }

    private void checkTypeConstraints() {
        final var type = this.castMember.getType();

        if (type == null) {
            this.validationHandler().append(new com.codeflix.catalog.admin.domain.validation.Error("'type' should not be null"));
            return;
        }
    }

}