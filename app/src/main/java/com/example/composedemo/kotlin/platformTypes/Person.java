package com.example.composedemo.kotlin.platformTypes;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class Person {

    private final String name;

    public Person(String name) {
        this.name = name;
    }

    public @Nullable String getName() {
        return name;
    }
}
