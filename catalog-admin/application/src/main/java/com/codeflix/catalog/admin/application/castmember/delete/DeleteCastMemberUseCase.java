package com.codeflix.catalog.admin.application.castmember.delete;

import com.codeflix.catalog.admin.application.base.UnitUseCase;

public sealed abstract class DeleteCastMemberUseCase
        extends UnitUseCase<String>
        permits DefaultDeleteCastMemberUseCase
{
}
