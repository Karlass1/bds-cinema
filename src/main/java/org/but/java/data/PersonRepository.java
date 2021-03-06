package org.but.java.data;

import org.but.java.api.*;
import org.but.java.config.DataSourceConfig;
import org.but.java.exceptions.DataAccessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.but.java.controllers.PersonsController;
public class PersonRepository {

    private static final Logger logger = LoggerFactory.getLogger(PersonRepository.class);

    public PersonAuthView findPersonByEmail(String email) {
        logger.debug("findPersonByEmail: " + email);
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT email, pwd" +
                             " FROM cinema.person p" +
                             " WHERE p.email = ?")
        ) {
            preparedStatement.setString(1, email);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToPersonAuth(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }
        return null;
    }

    public PersonDetailView findPersonDetailedView(Long personId) {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT p.person_id, first_name, last_name, email, age, \"role\", city" +
                             " FROM  cinema.role r RIGHT JOIN cinema.person_has_role pr ON pr.role_id = r.role_id" +
                             " RIGHT JOIN cinema.person p ON p.person_id = pr.person_id" +
                             " LEFT JOIN cinema.person_has_address pa ON p.person_id = pa.person_id" +
                             " LEFT JOIN cinema.address a ON pa.address_id = a.address_id" +
                             " WHERE p.person_id = ?")
        ) {
            preparedStatement.setLong(1, personId);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return mapToPersonDetailView(resultSet);
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Find person by ID with addresses failed.", e);
        }
        return null;
    }


    public List<PersonBasicView> getPersonsBasicView() {
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT person_id, email, first_name, last_name, age" +
                             " FROM cinema.person");
             ResultSet resultSet = preparedStatement.executeQuery();) {
            List<PersonBasicView> personBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                personBasicViews.add(mapToPersonBasicView(resultSet));
            }
            return personBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Persons basic view could not be loaded.", e);
        }
    }
    public List<PersonBasicView> getFirstFilterView() {
        String filterSQL = "SELECT person_id, email, first_name, last_name, age FROM cinema.person WHERE %s LIKE ?";
        filterSQL = String.format(filterSQL, PersonsController.choice);
        System.out.println(filterSQL);
        try (Connection connection = DataSourceConfig.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(filterSQL);) {
             preparedStatement.setString(1, "%"+PersonsController.input+"%");
             ResultSet resultSet = preparedStatement.executeQuery();
            List<PersonBasicView> personBasicViews = new ArrayList<>();
            while (resultSet.next()) {
                personBasicViews.add(mapToPersonBasicView(resultSet));
            }
            return personBasicViews;
        } catch (SQLException e) {
            throw new DataAccessException("Persons basic view could not be loaded.", e);
        }
    }

    public void createPerson(PersonCreateView personCreateView) {
        String insertPersonSQL = "INSERT INTO cinema.person (first_name, last_name, age, email, pwd) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setString(1, personCreateView.getFirstName());
            preparedStatement.setString(2, personCreateView.getSurname());
            preparedStatement.setInt(3, Integer.valueOf(personCreateView.getAge()));
            preparedStatement.setString(4, personCreateView.getEmail());
            preparedStatement.setString(5, String.valueOf(personCreateView.getPwd()));
            int affectedRows = preparedStatement.executeUpdate();
            if (affectedRows == 0) {
                throw new DataAccessException("Person creation  failed, no rows affected.");
            }
        } catch (SQLException e) {
            throw new DataAccessException("Person creation  failed operation on the database failed.");
        }
    }

    public void injectPerson(PersonCreateView personCreateView) {
        String insertPersonSQL = "INSERT INTO cinema.person (first_name, last_name, age, email, pwd) VALUES ('%s', '%s', '%d', '%s', '%s')";
        insertPersonSQL = String.format(insertPersonSQL, personCreateView.getFirstName(), personCreateView.getSurname(),
                Integer.valueOf(personCreateView.getAge()), personCreateView.getEmail(),
                String.valueOf(personCreateView.getPwd())
        );
        System.out.println(insertPersonSQL);
        try (Connection connection = DataSourceConfig.getConnection()) {
             Statement inj = connection.createStatement();
             int injectRows = inj.executeUpdate(insertPersonSQL);
             System.out.println(injectRows);
        } catch (SQLException e) {
            throw new DataAccessException("Person creation  failed operation on the database failed.");
        }
    }

    public void editPerson(PersonEditView personEditView) {
        String insertPersonSQL =
                " begin;" +
                " UPDATE cinema.person p SET email = ?, first_name = ?, age = ?, last_name = ? WHERE p.person_id = ?;" +
                " INSERT INTO cinema.person_has_role (person_id, role_id) VALUES (?,5);" +
                " commit;";

        String checkIfExists = "SELECT email FROM cinema.person p WHERE p.person_id = ?";
        try (Connection connection = DataSourceConfig.getConnection();
             // would be beneficial if I will return the created entity back
             PreparedStatement preparedStatement = connection.prepareStatement(insertPersonSQL, Statement.RETURN_GENERATED_KEYS)) {
            // set prepared statement variables
            preparedStatement.setString(1, personEditView.getEmail());
            preparedStatement.setString(2, personEditView.getFirstName());
            preparedStatement.setInt(3, Integer.valueOf(personEditView.getAge()));
            preparedStatement.setString(4, personEditView.getSurname());
            preparedStatement.setLong(5, personEditView.getId());
            preparedStatement.setLong(6, personEditView.getId());

            try {
                connection.setAutoCommit(false);
                try (PreparedStatement ps = connection.prepareStatement(checkIfExists, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, personEditView.getId());
                    ps.execute();
                } catch (SQLException e) {
                    throw new DataAccessException("This person for edit do not exists.");
                }

                int affectedRows = preparedStatement.executeUpdate();

                if (affectedRows == 0) {
                    throw new DataAccessException("Person edit failed, no rows affected.");
                }

                System.out.println(connection);
                connection.commit();
            } catch (SQLException e) {

                connection.rollback();
            } finally {

                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            throw new DataAccessException("Person edit failed operation on the database failed.");
        }
    }


    private PersonAuthView mapToPersonAuth(ResultSet rs) throws SQLException {
        PersonAuthView person = new PersonAuthView();
        person.setEmail(rs.getString("email"));
        person.setPassword(rs.getString("pwd"));
        return person;
    }

    private PersonBasicView mapToPersonBasicView(ResultSet rs) throws SQLException {
        PersonBasicView personBasicView = new PersonBasicView();
        personBasicView.setId(rs.getLong("person_id"));
        personBasicView.setEmail(rs.getString("email"));
        personBasicView.setGivenName(rs.getString("first_name"));
        personBasicView.setFamilyName(rs.getString("last_name"));
        personBasicView.setAge(rs.getString("age"));
        return personBasicView;
    }

    private PersonDetailView mapToPersonDetailView(ResultSet rs) throws SQLException {
        PersonDetailView personDetailView = new PersonDetailView();
        personDetailView.setId(rs.getLong("person_id"));
        personDetailView.setEmail(rs.getString("email"));
        personDetailView.setGivenName(rs.getString("first_name"));
        personDetailView.setFamilyName(rs.getString("last_name"));
        personDetailView.setAge(rs.getString("age"));
        personDetailView.setCity(rs.getString("city"));
        personDetailView.setRole(rs.getString("role"));
        return personDetailView;
    }

}
