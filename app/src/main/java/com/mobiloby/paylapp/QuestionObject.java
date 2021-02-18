package com.mobiloby.paylapp;

public class QuestionObject {
    String A1,A2,A3,A4,Answer,Question, key, category, confirmation;

    public QuestionObject(){

    }

    public String getA1() {
        return A1;
    }

    public void setA1(String a1) {
        A1 = a1;
    }

    public String getA2() {
        return A2;
    }

    public void setA2(String a2) {
        A2 = a2;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getA3() {
        return A3;
    }

    public void setA3(String a3) {
        A3 = a3;
    }

    public String getA4() {
        return A4;
    }

    public void setA4(String a4) {
        A4 = a4;
    }

    public String getAnswer() {
        return Answer;
    }

    public String getConfirmation() {
        return confirmation;
    }

    public void setConfirmation(String confirmation) {
        this.confirmation = confirmation;
    }

    public void setAnswer(String answer) {
        Answer = answer;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String question) {
        Question = question;
    }

    public QuestionObject(String a1, String a2, String a3, String a4, String answer, String question, String confirmation) {
        A1 = a1;
        A2 = a2;
        A3 = a3;
        A4 = a4;
        Answer = answer;
        Question = question;
        this.confirmation = confirmation;
    }
}
