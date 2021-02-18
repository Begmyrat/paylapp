package com.mobiloby.paylapp;

public class MenuObject {
    String name, numberOfQuestions;

    public MenuObject(String name, String numberOfQuestions) {
        this.name = name;
        this.numberOfQuestions = numberOfQuestions;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumberOfQuestions() {
        return numberOfQuestions;
    }

    public void setNumberOfQuestions(String numberOfQuestions) {
        this.numberOfQuestions = numberOfQuestions;
    }
}
