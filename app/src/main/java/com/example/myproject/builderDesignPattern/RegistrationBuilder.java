package com.example.myproject.builderDesignPattern;

public class RegistrationBuilder {
    private String username;
    private String password;
    private String email;
    private String phone;


    public RegistrationBuilder(String password, String email) {
        this.password = password;
        this.email = email;
    }

    public RegistrationBuilder setUsername(String username) {
        this.username = username;
        return this;
    }

    public RegistrationBuilder setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public Registration Build() {
        Registration registration = new Registration(username, password, email, phone);
        return registration;
    }
}
