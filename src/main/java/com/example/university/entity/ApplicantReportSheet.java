package com.example.university.entity;

public class ApplicantReportSheet extends Entity {

    private int facultyId;
    private String firstName;
    private String lastName;
    private String email;
    private boolean isBlocked;
    private int preliminarySum;
    private int diplomaSum;
    private int totalSum;

    private boolean entered;
    private boolean enteredOnBudget;

    public ApplicantReportSheet() {
    }

    public ApplicantReportSheet(int facultyId, String firstName, String lastName,
                                String email, boolean isBlocked, short preliminarySum,
                                short diplomaSum, short totalSum) {
        super();
        this.facultyId = facultyId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.isBlocked = isBlocked;
        this.preliminarySum = preliminarySum;
        this.diplomaSum = diplomaSum;
        this.totalSum = totalSum;
    }

    public int getFacultyId() {
        return facultyId;
    }

    public void setFacultyId(int facultyId) {
        this.facultyId = facultyId;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean getBlockedStatus() {
        return isBlocked;
    }

    public void setBlockedStatus(boolean isBlocked) {
        this.isBlocked = isBlocked;
    }

    public int getPreliminarySum() {
        return preliminarySum;
    }

    public void setPreliminarySum(int preliminarySum) {
        this.preliminarySum = preliminarySum;
    }

    public int getDiplomaSum() {
        return diplomaSum;
    }

    public void setDiplomaSum(int diplomaSum) {
        this.diplomaSum = diplomaSum;
    }

    public int getTotalSum() {
        return totalSum;
    }

    public void setTotalSum(int totalSum) {
        this.totalSum = totalSum;
    }

    public boolean getEntered() {
        return entered;
    }

    public void setEntered(boolean entered) {
        this.entered = entered;
    }

    public boolean getEnteredOnBudget() {
        return enteredOnBudget;
    }

    public void setEnteredOnBudget(boolean enteredOnBudget) {
        this.enteredOnBudget = enteredOnBudget;
    }

    @Override
    public String toString() {
        return "ReportSheet [facultyId=" + facultyId + ", firstName="
                + firstName + ", lastName=" + lastName + ", email=" + email
                + ", isBlocked=" + isBlocked + ", preliminarySum="
                + preliminarySum + ", diplomaSum=" + diplomaSum + ", totalSum="
                + totalSum + "]";
    }

}