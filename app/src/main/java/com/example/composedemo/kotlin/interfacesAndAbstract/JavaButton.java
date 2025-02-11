package com.example.composedemo.kotlin.interfacesAndAbstract;

public class JavaButton implements Clickable {

    @Override
    public void click() {
        System.out.println("I was clicked");
    }

    @Override
    public void showOff() {
//        Clickable.super.showOff();
        System.out.println("I'm showing off");
    }
}

// 3. Why to use interface with default implementation and not abstract class? -->
// i) Because you cannot call super in Java
// ii) Because you can only extend one class
// iii) Interfaces can't contain any state

// System.out.println("I broke the Android project using Java");
