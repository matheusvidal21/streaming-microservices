package com.codeflix.catalog.admin.application.castmember.create;

import com.codeflix.catalog.admin.domain.castmember.CastMember;
import com.codeflix.catalog.admin.domain.castmember.CastMemberGateway;
import com.codeflix.catalog.admin.domain.exceptions.NotificationException;
import com.codeflix.catalog.admin.domain.validation.handler.Notification;

import java.util.Objects;

public final class DefaultCreateCastMemberUseCase extends CreateCastMemberUseCase {

    private final CastMemberGateway castMemberGateway;

    public DefaultCreateCastMemberUseCase(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Override
    public CreateCastMemberOutput execute(final CreateCastMemberCommand aCommand) {
        final var aName = aCommand.name();
        final var aType = aCommand.type();

        final var notification = Notification.create();
        final var aMember = notification.validate(() -> CastMember.newMember(aName, aType));

        if (notification.hasErrors()) {
            notify(notification);
        }

        return CreateCastMemberOutput.from(this.castMemberGateway.create(aMember));
    }

    private void notify(Notification notification) {
        throw new NotificationException("Could not create Aggregate CastMember", notification);
    }

}
