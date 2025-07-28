package com.codeflix.catalog.admin.infrastructure.utils;

import org.springframework.data.jpa.domain.Specification;

public final class SpecificationUtils {

    private SpecificationUtils() {}

    public static <T> Specification<T> like(final String prop, final String terms) {
        return (root, query, cb) ->
                cb.like(cb.upper(root.get(prop)), like_terms(terms));
    }

    private static String like_terms(String terms) {
        return "%" + terms.toUpperCase() + "%";
    }

}
