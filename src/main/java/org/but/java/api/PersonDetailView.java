package org.but.java.api;

import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PersonDetailView {
    private LongProperty id = new SimpleLongProperty();
    private StringProperty email = new SimpleStringProperty();
    private StringProperty givenName = new SimpleStringProperty();
    private StringProperty familyName = new SimpleStringProperty();
    private StringProperty age = new SimpleStringProperty();
    private StringProperty city = new SimpleStringProperty();
    private StringProperty role = new SimpleStringProperty();

    public Long getId() {
        return idProperty().get();
    }

    public void setId(Long id) {
        this.idProperty().setValue(id);
    }

    public String getEmail() {
        return emailProperty().get();
    }

    public void setEmail(String email) {
        this.emailProperty().setValue(email);
    }

    public String getGivenName() {
        return givenNameProperty().get();
    }

    public void setGivenName(String givenName) {
        this.givenNameProperty().setValue(givenName);
    }

    public String getFamilyName() {
        return familyNameProperty().get();
    }

    public void setFamilyName(String familyName) {
        this.familyNameProperty().setValue(familyName);
    }

    public String getAge() {
        return ageProperty().get();
    }

    public void setAge(String age) {
        this.ageProperty().set(age);
    }

    public String getCity() {
        return cityProperty().get();
    }

    public void setCity(String city) {
        this.cityProperty().setValue(city);
    }

    public String getRole() {
        return roleProperty().get();
    }

    public void setRole(String role) {
        this.roleProperty().setValue(role);
    }

    public LongProperty idProperty() {
        return id;
    }

    public StringProperty emailProperty() {
        return email;
    }

    public StringProperty givenNameProperty() {
        return givenName;
    }

    public StringProperty familyNameProperty() {
        return familyName;
    }

    public StringProperty ageProperty() {
        return age;
    }

    public StringProperty cityProperty() {
        return city;
    }

    public StringProperty roleProperty() { return role; }


}