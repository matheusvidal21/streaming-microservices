package com.codeflix.catalog.admin.application.castmember.update;

import com.codeflix.catalog.admin.domain.castmember.CastMember;
import com.codeflix.catalog.admin.domain.castmember.CastMemberGateway;
import com.codeflix.catalog.admin.domain.castmember.CastMemberID;
import com.codeflix.catalog.admin.domain.exceptions.NotFoundException;
import com.codeflix.catalog.admin.domain.exceptions.NotificationException;
import com.codeflix.catalog.admin.domain.validation.handler.Notification;

import java.util.Objects;
import java.util.function.Supplier;

public final class DefaultUpdateCastMemberUseCase extends UpdateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultUpdateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public UpdateCastMemberOutput execute(final UpdateCastMemberCommand aCommand) {
        final var anId = CastMemberID.from(aCommand.id());
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var aMember = this.castMemberGateway.findById(anId)
                .orElseThrow(notFound(anId));

        final var notification = Notification.create();
        notification.validate(() -> aMember.update(aName, aType));

        if (notification.hasErrors()) {
            notify(anId, notification);
        }

        return UpdateCastMemberOutput.from(this.castMemberGateway.update(aMember));
    }

    private static void notify(CastMemberID anId, Notification notification) {
        throw new NotificationException("Could not update Aggregate CastMember %s".formatted(anId), notification);
    }

    private Supplier<NotFoundException> notFound(final CastMemberID anId) {
        return () -> NotFoundException.with(CastMember.class, anId);
    }

}
