package com.codeflix.catalog.admin.infrastructure;

import com.codeflix.catalog.admin.application.base.UseCase;


public class Main {
    public static void main(String[] args) {
        System.out.println("Hello World");
        System.out.println(new UseCase().execute());
    }
}