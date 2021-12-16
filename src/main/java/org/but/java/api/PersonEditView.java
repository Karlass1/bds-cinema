package org.but.java.api;

public class PersonEditView {

    private Long id;
    private String email;
    private String firstName;
    private String age;
    private String surname;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "PersonEditView{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", age='" + age + '\'' +
                ", surname='" + surname + '\'' +
                '}';
    }
}
