package org.rocessa.features.persons;

import java.util.List;

import org.rocessa.features.persons.models.Person;

public interface PersonsService {
    List<Person> getAllPersons();

    Person getPersonById(int id);

    List<Person> getPersonsByColor(String color);

    Person createPerson(Person person);

}
