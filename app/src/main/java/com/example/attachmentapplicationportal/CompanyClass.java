package com.example.attachmentapplicationportal;

public class CompanyClass {
    String name,email,phone,logo,password,servicesProvided,address;

    public CompanyClass() {

    }

    public CompanyClass(String name, String email, String phone, String logo, String password, String servicesProvided, String address) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.logo = logo;
        this.password = password;
        this.servicesProvided = servicesProvided;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getServicesProvided() {
        return servicesProvided;
    }

    public void setServicesProvided(String servicesProvided) {
        this.servicesProvided = servicesProvided;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
