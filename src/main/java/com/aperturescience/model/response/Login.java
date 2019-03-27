package com.aperturescience.model.response;

//Response model class
public class Login{

    private Result result;

    public Login(){}

    public Login(Result result) {
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Login{" +
                "result=" + result +
                '}';
    }
}


