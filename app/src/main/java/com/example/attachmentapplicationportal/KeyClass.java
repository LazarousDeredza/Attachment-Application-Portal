package com.example.attachmentapplicationportal;

public class KeyClass {
    String code,key,company,email;

    public KeyClass() {

    }


    public KeyClass(String code, String key, String company, String email) {
        this.code = code;
        this.key = key;
        this.company = company;
        this.email = email;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
