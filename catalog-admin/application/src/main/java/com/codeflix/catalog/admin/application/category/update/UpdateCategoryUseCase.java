package com.codeflix.catalog.admin.application.category.update;

import com.codeflix.catalog.admin.application.base.UseCase;
import com.codeflix.catalog.admin.domain.validation.handler.Notification;
import io.vavr.control.Either;

public abstract class UpdateCategoryUseCase extends UseCase<UpdateCategoryCommand, Either<Notification, UpdateCategoryOutput>> {

}
