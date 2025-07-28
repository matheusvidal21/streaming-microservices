package com.codeflix.catalog.admin.domain.validation;

import java.util.List;

public interface ValidationHandler {

    ValidationHandler append(Error anError);

    ValidationHandler append(ValidationHandler anHandler);

    ValidationHandler validate(Validation aValidation);

    List<Error> getErrors();

    default boolean hasErrors(){
        return getErrors() != null && !(getErrors().isEmpty());
    }

    default Error firstError(){
        if (!hasErrors()) {
            return null;
        }
        return getErrors().getFirst();
    }

    public interface Validation {
        void validate();
    }

}
