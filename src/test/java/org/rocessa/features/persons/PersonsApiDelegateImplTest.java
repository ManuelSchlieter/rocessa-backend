package org.rocessa.features.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.rocessa.features.persons.PersonsApiDelegateImpl;
import org.rocessa.features.persons.PersonsService;
import org.rocessa.features.persons.models.Person;
import org.rocessa.model.PersonDto;
import org.springframework.http.ResponseEntity;

import ch.qos.logback.core.model.Model;

public class PersonsApiDelegateImplTest {

    private ModelMapper modelMapper = new ModelMapper();

    @Mock
    PersonsService personService;

    @Mock
    ModelMapper modelMapperMock;

    @InjectMocks
    PersonsApiDelegateImpl personApiDelegate;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void when_ConvertPersonModelToPersonDto_ok() {
        // given
        var p = new Person();
        p.setId(1);
        p.setName("John");
        p.setLastname("Doe");
        p.setColor("grün");
        p.setCity("city");
        p.setZipcode("01234");

        // when
        var dto = modelMapper.map(p, PersonDto.class);

        // then
        assertEquals(dto.getId(), p.getId());
        assertEquals(dto.getName(), p.getName());
        assertEquals(dto.getLastname(), p.getLastname());
        assertEquals(dto.getColor(), p.getColor());
        assertEquals(dto.getCity(), p.getCity());
        assertEquals(dto.getZipcode(), p.getZipcode());
    }

    @Test
    public void when_GetAllPersons_ok() {
        // given
        Person person1 = createPerson1();
        Person person2 = createPerson2();
        List<Person> persons = Arrays.asList(person1, person2);

        PersonDto personDto1 = createPerson1Dto();
        PersonDto personDto2 = createPerson2Dto();
        List<PersonDto> personDtos = Arrays.asList(personDto1, personDto2);

        // mock
        when(personService.getAllPersons()).thenReturn(persons);
        when(personApiDelegate.convertToDto(person1)).thenReturn(personDto1);
        when(personApiDelegate.convertToDto(person2)).thenReturn(personDto2);

        // when
        ResponseEntity<List<PersonDto>> response = personApiDelegate.getAllPersons();

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals(personDtos, response.getBody());
    }

    @Test
    public void when_GetPersonById_ok() {
        // given
        Person person1 = createPerson1();
        PersonDto personDto1 = createPerson1Dto();

        // mock
        when(personService.getPersonById(1)).thenReturn(person1);
        when(personApiDelegate.convertToDto(person1)).thenReturn(personDto1);

        // when
        ResponseEntity<PersonDto> response = personApiDelegate.getPersonById(1);

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals(personDto1, response.getBody());
    }

    @Test
    public void when_GetPersonById_notFound_fail() {
        // mock
        when(personService.getPersonById(1)).thenReturn(null);

        // when
        ResponseEntity<PersonDto> response = personApiDelegate.getPersonById(1);

        // then
        assertNotNull(response);
        assertEquals(404, response.getStatusCode());
    }

    @Test
    public void when_GetPersonsByColor_grün_ok() {
        // given
        Person person1 = createPerson1();
        Person person2 = createPerson2();
        Person person3 = createPerson3();
        List<Person> persons = Arrays.asList(person1, person2, person3);

        PersonDto personDto1 = createPerson1Dto();
        PersonDto personDto2 = createPerson2Dto();
        PersonDto personDto3 = createPerson3Dto();
        List<PersonDto> personDtos = Arrays.asList(personDto1, personDto2, personDto3);

        // mock
        when(personService.getPersonsByColor("grün")).thenReturn(persons);
        when(personApiDelegate.convertToDto(person1)).thenReturn(personDto1);
        when(personApiDelegate.convertToDto(person2)).thenReturn(personDto2);
        when(personApiDelegate.convertToDto(person3)).thenReturn(personDto3);

        // when
        ResponseEntity<List<PersonDto>> response = personApiDelegate.getPersonsByColor("grün");

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals(personDtos, response.getBody());
    }

    @Test
    public void when_GetPersonsByColor_invalid_fail() {
        // given
        Person person1 = createPerson1();
        Person person2 = createPerson2();
        Person person3 = createPerson3();
        List<Person> persons = Arrays.asList(person1, person2, person3);

        PersonDto personDto1 = createPerson1Dto();
        PersonDto personDto2 = createPerson2Dto();
        PersonDto personDto3 = createPerson3Dto();
        List<PersonDto> personDtos = Arrays.asList(personDto1, personDto2, personDto3);

        // mock
        when(personService.getPersonsByColor("grün")).thenReturn(persons);
        when(personApiDelegate.convertToDto(person1)).thenReturn(personDto1);
        when(personApiDelegate.convertToDto(person2)).thenReturn(personDto2);
        when(personApiDelegate.convertToDto(person3)).thenReturn(personDto3);

        // when
        ResponseEntity<List<PersonDto>> response = personApiDelegate.getPersonsByColor("grün");

        // then
        assertNotNull(response);
        assertEquals(200, response.getStatusCode());
        assertEquals(personDtos, response.getBody());
    }

    Person createPerson1() {
        Person person = new Person();
        person.setId(1);
        person.setName("Hans");
        person.setLastname("Müller");
        person.setZipcode("67742");
        person.setCity("Lauterecken");
        person.setColor("blau");
        return person;
    }

    Person createPerson2() {
        Person person = new Person();
        person.setId(2);
        person.setName("Peter");
        person.setLastname("Petersen");
        person.setZipcode("18439");
        person.setCity("Stralsund");
        person.setColor("grün");
        return person;
    }

    Person createPerson3() {
        Person person = new Person();
        person.setId(7);
        person.setName("Anders");
        person.setLastname("Andersson");
        person.setZipcode("32132");
        person.setCity("Schweden - ☀");
        person.setColor("grün");
        return person;
    }

    PersonDto createPerson1Dto() {
        PersonDto personDto = new PersonDto();
        personDto.setId(1);
        personDto.setName("Hans");
        personDto.setLastname("Müller");
        personDto.setZipcode("67742");
        personDto.setCity("Lauterecken");
        personDto.setColor("blau");
        return personDto;
    }

    PersonDto createPerson2Dto() {
        PersonDto personDto = new PersonDto();
        personDto.setId(2);
        personDto.setName("Peter");
        personDto.setLastname("Petersen");
        personDto.setZipcode("18439");
        personDto.setCity("Stralsund");
        personDto.setColor("grün");
        return personDto;
    }

    PersonDto createPerson3Dto() {
        PersonDto personDto = new PersonDto();
        personDto.setId(7);
        personDto.setName("Anders");
        personDto.setLastname("Andersson");
        personDto.setZipcode("32132");
        personDto.setCity("Schweden - ☀");
        personDto.setColor("grün");
        return personDto;
    }

}
