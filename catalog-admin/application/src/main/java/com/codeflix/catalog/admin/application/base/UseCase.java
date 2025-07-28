package com.codeflix.catalog.admin.application.base;



public abstract class UseCase<IN, OUT> {

    public abstract OUT execute(IN anIn);

}