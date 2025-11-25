package com.codeflix.catalog.admin.application.castmember.retrivie.get;

import com.codeflix.catalog.admin.application.base.UseCase;

public sealed abstract class GetCastMemberByIdUseCase
        extends UseCase<String, CastMemberOutput>
        permits DefaultGetCastMemberByIdUseCase
{
}
