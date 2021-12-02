package org.but.java.services;

import org.but.java.data.PersonRepository;

public class AuthService {
    private PersonRepository personRepository;

    public AuthService(PersonRepository personRepository) {
         this.personRepository = personRepository;
    }
}
