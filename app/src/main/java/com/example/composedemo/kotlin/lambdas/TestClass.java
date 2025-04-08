package com.example.composedemo.kotlin.lambdas;

public class TestClass {

    public void main() {
        SamInterfacesKt.postponeComputation(
                100,
                () -> {

                }
        );
    }
}
