package org.but.java.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.but.java.api.PersonDetailView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PersonsDetailViewController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsDetailViewController.class);

    @FXML
    private TextField idTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField firstNameTextField;

    @FXML
    private TextField lastNameTextField;

    @FXML
    private TextField ageTextField;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField roleTextField;

    // used to reference the stage and to get passed data through it
    public Stage stage;

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        idTextField.setEditable(false);
        emailTextField.setEditable(false);
        firstNameTextField.setEditable(false);
        lastNameTextField.setEditable(false);
        ageTextField.setEditable(false);
        cityTextField.setEditable(false);
        roleTextField.setEditable(false);

        loadPersonsData();

        logger.info("PersonsDetailViewController initialized");
    }

    private void loadPersonsData() {
        Stage stage = this.stage;
        if (stage.getUserData() instanceof PersonDetailView) {
            PersonDetailView personBasicView = (PersonDetailView) stage.getUserData();
            idTextField.setText(String.valueOf(personBasicView.getId()));
            emailTextField.setText(personBasicView.getEmail());
            firstNameTextField.setText(personBasicView.getGivenName());
            lastNameTextField.setText(personBasicView.getFamilyName());
            ageTextField.setText(personBasicView.getAge());
            cityTextField.setText(personBasicView.getCity());
            roleTextField.setText(personBasicView.getRole());
        }
    }

}
