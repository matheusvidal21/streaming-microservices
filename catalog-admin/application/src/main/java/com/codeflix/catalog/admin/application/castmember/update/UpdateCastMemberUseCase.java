package com.codeflix.catalog.admin.application.castmember.update;

import com.codeflix.catalog.admin.application.base.UseCase;

public sealed abstract class UpdateCastMemberUseCase
        extends UseCase<UpdateCastMemberCommand, UpdateCastMemberOutput>
        permits DefaultUpdateCastMemberUseCase
{
}
