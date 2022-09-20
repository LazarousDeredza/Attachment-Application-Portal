package com.example.attachmentapplicationportal;

public class JobAdvertsClass {
    String companyName,location,jobTittle,email
            ,qualifications,jobSummary,applicationsStartDate,
            applicationsEndDate,phone;

    public JobAdvertsClass() {
    }

    public JobAdvertsClass(String companyName, String location, String jobTittle, String email, String qualifications, String jobSummary, String applicationsStartDate, String applicationsEndDate, String phone) {
        this.companyName = companyName;
        this.location = location;
        this.jobTittle = jobTittle;
        this.email = email;
        this.qualifications = qualifications;
        this.jobSummary = jobSummary;
        this.applicationsStartDate = applicationsStartDate;
        this.applicationsEndDate = applicationsEndDate;
        this.phone = phone;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getJobTittle() {
        return jobTittle;
    }

    public void setJobTittle(String jobTittle) {
        this.jobTittle = jobTittle;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getQualifications() {
        return qualifications;
    }

    public void setQualifications(String qualifications) {
        this.qualifications = qualifications;
    }

    public String getJobSummary() {
        return jobSummary;
    }

    public void setJobSummary(String jobSummary) {
        this.jobSummary = jobSummary;
    }

    public String getApplicationsStartDate() {
        return applicationsStartDate;
    }

    public void setApplicationsStartDate(String applicationsStartDate) {
        this.applicationsStartDate = applicationsStartDate;
    }

    public String getApplicationsEndDate() {
        return applicationsEndDate;
    }

    public void setApplicationsEndDate(String applicationsEndDate) {
        this.applicationsEndDate = applicationsEndDate;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
