package com.codeflix.catalog.admin.infrastructure.configuration.usecases;

import com.codeflix.catalog.admin.application.castmember.create.CreateCastMemberUseCase;
import com.codeflix.catalog.admin.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.codeflix.catalog.admin.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.codeflix.catalog.admin.application.castmember.delete.DeleteCastMemberUseCase;
import com.codeflix.catalog.admin.application.castmember.retrivie.get.DefaultGetCastMemberByIdUseCase;
import com.codeflix.catalog.admin.application.castmember.retrivie.get.GetCastMemberByIdUseCase;
import com.codeflix.catalog.admin.application.castmember.retrivie.list.DefaultListCastMembersUseCase;
import com.codeflix.catalog.admin.application.castmember.retrivie.list.ListCastMembersUseCase;
import com.codeflix.catalog.admin.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.codeflix.catalog.admin.application.castmember.update.UpdateCastMemberUseCase;
import com.codeflix.catalog.admin.domain.castmember.CastMemberGateway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Objects;

@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
        this.castMemberGateway = Objects.requireNonNull(castMemberGateway);
    }

    @Bean
    public GetCastMemberByIdUseCase getGenreByIdUseCase() {
        return new DefaultGetCastMemberByIdUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMembersUseCase listGenresUseCase() {
        return new DefaultListCastMembersUseCase(castMemberGateway);
    }

    @Bean
    public CreateCastMemberUseCase createGenreUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateGenreUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteGenreUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }

}
