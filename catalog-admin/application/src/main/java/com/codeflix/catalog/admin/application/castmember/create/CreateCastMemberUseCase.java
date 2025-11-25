package com.codeflix.catalog.admin.application.castmember.create;

import com.codeflix.catalog.admin.application.base.UseCase;

public sealed abstract class CreateCastMemberUseCase
        extends UseCase<CreateCastMemberCommand, CreateCastMemberOutput>
        permits DefaultCreateCastMemberUseCase
{
}
