package com.example.attachmentapplicationportal;

public class StudentClass {
    String firstName,lastName,regNumber,email,
            password,contact,institution,
            program,results,letter,ID,cv,
            startDate,endDate,img;


    public StudentClass() {
    }

    public StudentClass(String firstName, String lastName, String regNumber,
                        String email, String password, String contact, String institution,
                        String program, String results, String letter, String ID, String cv,
                        String startDate, String endDate, String img) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.regNumber = regNumber;
        this.email = email;
        this.password = password;
        this.contact = contact;
        this.institution = institution;
        this.program = program;
        this.results = results;
        this.letter = letter;
        this.ID = ID;
        this.cv = cv;
        this.startDate = startDate;
        this.endDate = endDate;
        this.img = img;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getProgram() {
        return program;
    }

    public void setProgram(String program) {
        this.program = program;
    }

    public String getResults() {
        return results;
    }

    public void setResults(String results) {
        this.results = results;
    }

    public String getLetter() {
        return letter;
    }

    public void setLetter(String letter) {
        this.letter = letter;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
