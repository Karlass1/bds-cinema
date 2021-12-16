package org.but.java.api;

import java.util.Arrays;

public class PersonCreateView {

    private String email;
    private String firstName;
    private String age;
    private String surname;
    private char[] pwd;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public char[] getPwd() {
        return pwd;
    }

    public void setPwd(char[] pwd) {
        this.pwd = pwd;
    }

    @Override
    public String toString() {
        return "PersonCreateView{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surname='" + surname + '\'' +
                ", age='" + age + '\'' +
                ", pwd=" + Arrays.toString(pwd) +
                '}';
    }
}
