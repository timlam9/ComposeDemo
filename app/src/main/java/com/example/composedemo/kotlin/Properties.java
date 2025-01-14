package com.example.composedemo.kotlin;

// 2.2.1
public class Properties {

    // final field
    private final String status = "Opened";

    // field
    private int numberOfUnemployed = 6;

    // getter accessor
    public String getStatus() {
        return status;
    }

    // getter accessor
    public int getNumberOfUnemployed() {
        return numberOfUnemployed;
    }

    // setter accessor
    // numberOfUnemployed is a parameter
    public void setNumberOfUnemployed(int numberOfUnemployed) {
        this.numberOfUnemployed = numberOfUnemployed;
    }

    public void callKotlinClassWithProperties() {
        Oaed oaed = new Oaed();

        oaed.getStatus();
        oaed.getNumberOfUnemployed();
        // 5 is an argument
        oaed.setNumberOfUnemployed(5);
    }

    public void callPersonClassWithProperties() {
        Person person = new Person("John Pap", true);

        person.getName();

        // naming exception: property name starts with 'is'
        person.isUnemployed();
        person.setUnemployed(false);
    }

    // Backing fields 4.2.4
    public void callClassWithBackingProperties() {

    }
}
