package com.aperturescience.model.response;

//Inner object in class repsonse model class Login
public class Result {

    private String email;
    private String auth_token;

    Result() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuth_token() {
        return auth_token;
    }

    public void setAuth_token(String auth_token) {
        this.auth_token = auth_token;
    }

    @Override
    public String toString() {
        return "Result{" +
                "email='" + email + '\'' +
                ", auth_token='" + auth_token + '\'' +
                '}';
    }
}
