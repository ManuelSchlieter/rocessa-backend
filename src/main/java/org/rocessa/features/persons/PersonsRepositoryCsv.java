package org.rocessa.features.persons;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.rocessa.features.persons.models.Colors;
import org.rocessa.features.persons.models.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Repository;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import jakarta.annotation.PostConstruct;

@Repository
public class PersonsRepositoryCsv implements PersonsRepository {
    private static final Logger logger = LoggerFactory.getLogger(PersonsRepositoryCsv.class);

    ResourceLoader resourceLoader;
    List<Person> persons = new ArrayList<>();

    @Autowired
    public PersonsRepositoryCsv(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    @PostConstruct
    public void init() {
        loadPersons();
    }

    @Override
    public List<Person> getAllPersons() {
        return persons;
    }

    @Override
    public Person getPersonById(int id) {
        return persons.stream()
                .filter(person -> person.getId() == id)
                .findFirst()
                .orElse(null);
    }

    @Override
    public List<Person> getPersonsByColor(String color) {
        return persons.stream()
                .filter(person -> color.equals(person.getColor()))
                .toList();
    }

    @Override
    public void loadPersons() {
        Resource resource = resourceLoader.getResource("classpath:sample-input.csv");
        try (CSVReader reader = new CSVReader(new InputStreamReader(resource.getInputStream()))) {

            int currentId = 0;
            String[] entry;
            while ((entry = reader.readNext()) != null) {
                currentId++;
                Person person = parsePersonFromCsvEntry(currentId, entry);
                if (person != null) {
                    persons.add(person);
                }

            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvValidationException e) {
            e.printStackTrace();
        }
    }

    protected Person parsePersonFromCsvEntry(int id, String[] entry) {
        if (entry.length != 4) {
            logger.warn("Malformed csv entry, skipping " + Arrays.toString(entry));
            return null;
        }

        entry = trimEntries(entry);

        var color = Colors.idToColorMap.get(entry[3]);
        if (color == null) {
            logger.warn("Malformed color entry, skipping " + Arrays.toString(entry));
            return null;
        }

        var location = entry[2].split(" ", 2);
        if (location.length < 2) {
            logger.warn("Malformed location entry, skipping " + Arrays.toString(entry));
            return null;
        }

        var person = new Person();
        person.setId(id);
        person.setLastname(entry[0]);
        person.setName(entry[1]);
        person.setZipcode(location[0]);
        person.setCity(location[1]);
        person.setColor(color);

        return person;
    }

    private String[] trimEntries(String[] entry) {
        return Arrays.stream(entry)
                .map(String::trim)
                .toArray(String[]::new);
    }

    @Override
    public Person createPerson(Person person) {
        person.setId(findNextId());
        persons.add(person);
        return person;
    }

    int findNextId() {
        return persons.stream()
                .map(Person::getId)
                .max(Integer::compareTo)
                .map(id -> id + 1)
                .orElse(1);
    }
}
