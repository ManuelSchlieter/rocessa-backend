package org.rocessa.features.persons;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.rocessa.api.PersonsApiDelegate;
import org.rocessa.features.persons.models.Person;
import org.rocessa.model.PersonDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PersonsApiDelegateImpl implements PersonsApiDelegate {

    private ModelMapper modelMapper;
    private PersonsService personService;

    @Autowired
    public PersonsApiDelegateImpl(PersonsService personService, ModelMapper modelMapper) {
        this.personService = personService;
        this.modelMapper = modelMapper;
    }

    @Override
    public ResponseEntity<List<PersonDto>> getAllPersons() {
        List<PersonDto> result = this.personService.getAllPersons()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<PersonDto> getPersonById(Integer id) {
        var person = personService.getPersonById(id);
        if (person == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(convertToDto(person));
    }

    @Override
    public ResponseEntity<List<PersonDto>> getPersonsByColor(String color) {
        List<PersonDto> result = this.personService.getPersonsByColor(color)
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(result);
    }

    public PersonDto convertToDto(Person person) {
        return modelMapper.map(person, PersonDto.class);
    }

}
