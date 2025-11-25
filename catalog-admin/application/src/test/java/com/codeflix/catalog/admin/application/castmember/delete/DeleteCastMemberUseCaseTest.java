package com.codeflix.catalog.admin.application.castmember.delete;

import com.codeflix.catalog.admin.application.Fixture;
import com.codeflix.catalog.admin.application.UseCaseTest;
import com.codeflix.catalog.admin.domain.castmember.CastMember;
import com.codeflix.catalog.admin.domain.castmember.CastMemberGateway;
import com.codeflix.catalog.admin.domain.castmember.CastMemberID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.List;

public class DeleteCastMemberUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteCastMemberUseCase useCase;

    @Mock
    private CastMemberGateway castMemberGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(castMemberGateway);
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = aMember.getId();

        Mockito.doNothing()
                .when(castMemberGateway).deleteById(Mockito.any());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        final var expectedId = CastMemberID.from("123");

        Mockito.doNothing()
                .when(castMemberGateway).deleteById(Mockito.any());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = aMember.getId();

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(Mockito.any());

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));
    }

}
