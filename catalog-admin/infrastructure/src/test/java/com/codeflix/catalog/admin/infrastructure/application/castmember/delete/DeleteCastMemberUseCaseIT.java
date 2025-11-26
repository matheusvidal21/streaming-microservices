package com.codeflix.catalog.admin.infrastructure.application.castmember.delete;

import com.codeflix.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.codeflix.catalog.admin.domain.castmember.CastMember;
import com.codeflix.catalog.admin.domain.castmember.CastMemberGateway;
import com.codeflix.catalog.admin.domain.castmember.CastMemberID;
import com.codeflix.catalog.admin.infrastructure.Fixture;
import com.codeflix.catalog.admin.infrastructure.IntegrationTest;
import com.codeflix.catalog.admin.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.codeflix.catalog.admin.infrastructure.castmember.persistence.CastMemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;


@IntegrationTest
public class DeleteCastMemberUseCaseIT {


    @Autowired
    private DeleteCastMemberUseCase useCase;

    @Autowired
    private CastMemberRepository castMemberRepository;

    @MockitoSpyBean
    private CastMemberGateway castMemberGateway;

    @Test
    public void givenAValidId_whenCallsDeleteCastMember_shouldDeleteIt() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());
        final var aMemberTwo = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        final var expectedId = aMember.getId();

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));
        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMemberTwo));

        Assertions.assertEquals(2, this.castMemberRepository.count());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(1, this.castMemberRepository.count());
        Assertions.assertFalse(this.castMemberRepository.existsById(expectedId.getValue()));
        Assertions.assertTrue(this.castMemberRepository.existsById(aMemberTwo.getId().getValue()));
    }

    @Test
    public void givenAnInvalidId_whenCallsDeleteCastMember_shouldBeOk() {
        // given
        this.castMemberRepository.saveAndFlush(
                CastMemberJpaEntity.from(
                        CastMember.newMember(Fixture.name(), Fixture.CastMembers.type())
                )
        );

        final var expectedId = CastMemberID.from("123");

        Assertions.assertEquals(1, this.castMemberRepository.count());

        // when
        Assertions.assertDoesNotThrow(() -> useCase.execute(expectedId.getValue()));

        // then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(1, this.castMemberRepository.count());
    }

    @Test
    public void givenAValidId_whenCallsDeleteCastMemberAndGatewayThrowsException_shouldReceiveException() {
        // given
        final var aMember = CastMember.newMember(Fixture.name(), Fixture.CastMembers.type());

        this.castMemberRepository.saveAndFlush(CastMemberJpaEntity.from(aMember));

        final var expectedId = aMember.getId();

        Assertions.assertEquals(1, this.castMemberRepository.count());

        Mockito.doThrow(new IllegalStateException("Gateway error"))
                .when(castMemberGateway).deleteById(Mockito.any());

        // when
        Assertions.assertThrows(IllegalStateException.class, () -> useCase.execute(expectedId.getValue()));

        // then
        Mockito.verify(castMemberGateway).deleteById(Mockito.eq(expectedId));

        Assertions.assertEquals(1, this.castMemberRepository.count());
    }

}
