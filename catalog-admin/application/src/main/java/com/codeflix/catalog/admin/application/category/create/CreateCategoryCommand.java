package com.codeflix.catalog.admin.application.category.create;

public record CreateCategoryCommand(
        String name,
        String description,
        boolean active
) {

    public static CreateCategoryCommand with(
            final String name,
            final String description,
            final Boolean isActive
    ) {
        return new CreateCategoryCommand(name, description, isActive != null ? isActive : true);
    }

}
