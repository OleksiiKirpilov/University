package com.example.university.entities;

/**
 * User entity. This transfer object characterized by id, first and last names,
 * email, password, role in system, site language. Email should be unique.
 * Every field must be filled.
 */
public class User extends Entity {

    private static final long serialVersionUID = -6889036256149495388L;

    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String role;
    private String lang;
    private String salt;

    public User() {
    }

    public User(String email, String password, String salt, String firstName,
                String lastName, Role role, String lang) {
        this.email = email;
        this.password = password;
        this.salt = salt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = role.getName();
        this.lang = lang;
    }

    public User(String email, String password, String firstName,
                String lastName, Role role, String lang) {
        this(email, password, null, firstName, lastName, role, lang);
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String login) {
        this.email = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
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

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }


    @Override
    public String toString() {
        return "User [email=" + email + ", password=***"
                + ", firstName=" + firstName + ", lastName=" + lastName
                + ", role=" + role + ", lang=" + lang + "]";
    }

}
