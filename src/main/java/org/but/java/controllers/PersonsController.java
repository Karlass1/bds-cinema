package org.but.java.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.but.java.App;
import org.but.java.api.PersonBasicView;
import org.but.java.api.PersonDetailView;
import org.but.java.config.DataSourceConfig;
import org.but.java.data.PersonRepository;
import org.but.java.exceptions.DataAccessException;
import org.but.java.exceptions.ExceptionHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import org.but.java.services.PersonService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class PersonsController {

    private static final Logger logger = LoggerFactory.getLogger(PersonsController.class);

    @FXML
    public Button addPersonButton;
    @FXML
    public Button refreshButton;
    @FXML
    private TableColumn<PersonBasicView, Long> personsId;
    @FXML
    private TableColumn<PersonBasicView, String> personsEmail;
    @FXML
    private TableColumn<PersonBasicView, String> personsFamilyName;
    @FXML
    private TableColumn<PersonBasicView, String> personsGivenName;
    @FXML
    private TableColumn<PersonBasicView, String> personsAge;
    @FXML
    private TableView<PersonBasicView> systemPersonsTableView;


    private PersonService personService;
    private PersonRepository personRepository;

    public PersonsController() {
    }

    @FXML
    private void initialize() {
        personRepository = new PersonRepository();
        personService = new PersonService(personRepository);

        personsId.setCellValueFactory(new PropertyValueFactory<PersonBasicView, Long>("id"));
        personsEmail.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("email"));
        personsFamilyName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("familyName"));
        personsGivenName.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("givenName"));
        personsAge.setCellValueFactory(new PropertyValueFactory<PersonBasicView, String>("age"));


        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonsList);

        systemPersonsTableView.getSortOrder().add(personsId);

        initializeTableViewSelection();

        logger.info("PersonsController initialized");
    }

    private void initializeTableViewSelection() {
        MenuItem edit = new MenuItem("Edit person");
        MenuItem detailedView = new MenuItem("Detailed person view");
        MenuItem deletePerson = new MenuItem("Delete person");
        edit.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/PersonEdit.fxml"));
                Stage stage = new Stage();
                stage.setUserData(personView);
                stage.setTitle("BDS JavaFX Edit Person");

                PersonsEditController controller = new PersonsEditController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });

        detailedView.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try {
                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(App.class.getResource("fxml/PersonsDetailView.fxml"));
                Stage stage = new Stage();

                Long personId = personView.getId();
                PersonDetailView personDetailView = personService.getPersonDetailView(personId);

                stage.setUserData(personDetailView);
                stage.setTitle("BDS JavaFX Persons Detailed View");

                PersonsDetailViewController controller = new PersonsDetailViewController();
                controller.setStage(stage);
                fxmlLoader.setController(controller);

                Scene scene = new Scene(fxmlLoader.load(), 600, 500);

                stage.setScene(scene);

                stage.show();
            } catch (IOException ex) {
                ExceptionHandler.handleException(ex);
            }
        });
        deletePerson.setOnAction((ActionEvent event) -> {
            PersonBasicView personView = systemPersonsTableView.getSelectionModel().getSelectedItem();
            try (Connection connection = DataSourceConfig.getConnection();
                 PreparedStatement preparedStatement = connection.prepareStatement(
                         "DELETE FROM cinema.person p WHERE p.person_id = ?")
            ) {
                preparedStatement.setLong(1, personView.getId());
                preparedStatement.execute();

            } catch (SQLException e) {
                throw new DataAccessException("Find person by ID with addresses failed.", e);
            }
        });


        ContextMenu menu = new ContextMenu();
        menu.getItems().add(edit);
        menu.getItems().add(deletePerson);
        menu.getItems().addAll(detailedView);

        systemPersonsTableView.setContextMenu(menu);
    }

    private ObservableList<PersonBasicView> initializePersonsData() {
        List<PersonBasicView> persons = personService.getPersonsBasicView();
        return FXCollections.observableArrayList(persons);
    }


    public void handleExitMenuItem(ActionEvent event) {
        System.exit(0);
    }

    public void handleAddPersonButton(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(App.class.getResource("fxml/PersonsCreate.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 600, 500);
            Stage stage = new Stage();
            stage.setTitle("BDS JavaFX Create Person");
            stage.setScene(scene);

//            Stage stageOld = (Stage) signInButton.getScene().getWindow();
//            stageOld.close();
//
//            stage.getIcons().add(new Image(App.class.getResourceAsStream("logos/vut.jpg")));
//            authConfirmDialog();

            stage.show();
        } catch (IOException ex) {
            ExceptionHandler.handleException(ex);
        }
    }

    public void handleRefreshButton(ActionEvent actionEvent) {
        ObservableList<PersonBasicView> observablePersonsList = initializePersonsData();
        systemPersonsTableView.setItems(observablePersonsList);
        systemPersonsTableView.refresh();
        systemPersonsTableView.sort();
    }

}
