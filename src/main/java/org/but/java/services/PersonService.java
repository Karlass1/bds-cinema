package org.but.java.services;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.but.java.api.*;
import org.but.java.data.PersonRepository;

import java.util.List;


public class PersonService {

    private PersonRepository personRepository;

    public PersonService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public PersonDetailView getPersonDetailView(Long id) {
        return personRepository.findPersonDetailedView(id);
    }

    public List<PersonBasicView> getPersonsBasicView() {
        return personRepository.getPersonsBasicView();
    }
    public List<PersonBasicView> getFirstFilterView() {
        return personRepository.getFirstFilterView();
    }

    public void createPerson(PersonCreateView personCreateView) {
        char[] originalPassword = personCreateView.getPwd();
        char[] hashedPassword = hashPassword(originalPassword);
        personCreateView.setPwd(hashedPassword);

        personRepository.createPerson(personCreateView);
    }

    public void injectPerson(PersonCreateView personCreateView) {
        personRepository.injectPerson(personCreateView);
    }

    public void editPerson(PersonEditView personEditView) {
        personRepository.editPerson(personEditView);
    }

    private char[] hashPassword(char[] password) {
        return BCrypt.withDefaults().hashToChar(12, password);
    }

}
