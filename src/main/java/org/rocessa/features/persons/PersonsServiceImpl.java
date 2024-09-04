package org.rocessa.features.persons;

import java.util.List;

import org.rocessa.features.persons.models.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PersonsServiceImpl implements PersonsService {

    private PersonsRepository personsRepository;

    @Autowired
    public PersonsServiceImpl(PersonsRepository personsRepository) {
        this.personsRepository = personsRepository;
    }

    @Override
    public List<Person> getAllPersons() {
        return personsRepository.getAllPersons();
    }

    @Override
    public Person getPersonById(int id) {
        return personsRepository.getPersonById(id);
    }

    @Override
    public List<Person> getPersonsByColor(String color) {
        return personsRepository.getPersonsByColor(color);
    }
}
