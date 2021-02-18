package com.mobiloby.paylapp;

public class UserObject implements Comparable<UserObject>{
    String Username, Phone, Password;
    String Money, OnScore, TotalQuestion, TrueAnswer, WrongAnswer;
    String OffScore, PrivateNumber="";

    public UserObject(){

    }

//    public String getSsername() {
//        return Username;
//    }


    public String getPrivateNumber() {
        return PrivateNumber;
    }

    public void setPrivateNumber(String privateNumber) {
        PrivateNumber = privateNumber;
    }

    public UserObject(String username, String phone, String password, String money, String offScore, String onScore, String TotalQuestion, String TrueAnswer, String WrongAnswer, String PrivateNumber) {
        Username = username;
        Phone = phone;
        Password = password;
        Money = money;
        OffScore = offScore;
        OnScore = onScore;
        this.TotalQuestion = TotalQuestion;
        this.TrueAnswer = TrueAnswer;
        this.WrongAnswer = WrongAnswer;
        this.PrivateNumber = PrivateNumber;
    }

    public void setUsername(String username) {
        Username = username;
    }
    public String getUsername() {
        return this.Username;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getPassword() {
        return Password;
    }


    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }

    public String getOnScore() {
        return OnScore;
    }

    public void setOnScore(String onScore) {
        OnScore = onScore;
    }

    public String getTotalQuestion() {
        return TotalQuestion;
    }

    public void setTotalQuestion(String totalQuestion) {
        TotalQuestion = totalQuestion;
    }

    public String getTrueAnswer() {
        return TrueAnswer;
    }

    public void setTrueAnswer(String trueAnswer) {
        TrueAnswer = trueAnswer;
    }

    public String getWrongAnswer() {
        return WrongAnswer;
    }

    public void setWrongAnswer(String wrongAnswer) {
        WrongAnswer = wrongAnswer;
    }

    public String getOffScore() {
        return OffScore;
    }

    public void setOffScore(String offScore) {
        OffScore = offScore;
    }

    public void setPassword(String password) {
        Password = password;
    }



    @Override
    public int compareTo(UserObject o) {

        if(Integer.parseInt(this.getOffScore())>Integer.parseInt(o.getOffScore()))  return 1;
        else if(Integer.parseInt(this.getOffScore())<Integer.parseInt(o.getOffScore())) return -1;

        return 0;
    }
}
