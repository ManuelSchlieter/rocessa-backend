package org.rocessa.features.persons;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.rocessa.features.persons.models.Person;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

public class CsvPersonsRepositoryTest {

    @Mock
    private Resource resource;

    @Mock
    private ResourceLoader resourceLoader;

    @InjectMocks
    private PersonsRepositoryCsv csvPersonsRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void when_getAllPersons_ok() throws IOException {
        // given
        mockLoadPersons();

        // when
        List<Person> allPersons = csvPersonsRepository.getAllPersons();

        // then
        assertNotNull(allPersons);
        assertEquals(3, allPersons.size());

        Person person = allPersons.get(0);
        assertEquals(1, person.getId());
        assertEquals("Müller", person.getLastname());
        assertEquals("Hans", person.getName());
        assertEquals("67742", person.getZipcode());
        assertEquals("Lauterecken", person.getCity());
        assertEquals("blau", person.getColor());
    }

    @Test
    void when_loadPersonFromCsvEntry_ok() throws IOException {
        // given
        String[] line = "Müller, Hans, 67742 Lauterecken, 1".split(",");

        // when
        Person person = csvPersonsRepository.parsePersonFromCsvEntry(1, line);

        // then
        assertNotNull(person);
        assertEquals(1, person.getId());
        assertEquals("Müller", person.getLastname());
        assertEquals("Hans", person.getName());
        assertEquals("67742", person.getZipcode());
        assertEquals("Lauterecken", person.getCity());
        assertEquals("blau", person.getColor());
    }

    @Test
    void when_loadPersonFromCsvEntry_ofLongCity_ok() throws IOException {
        // given
        String[] line = "Millenium, Milly, 77777 made up too, 4".split(",");

        // when
        Person person = csvPersonsRepository.parsePersonFromCsvEntry(1, line);

        // then
        assertNotNull(person);
        assertEquals(1, person.getId());
        assertEquals("Millenium", person.getLastname());
        assertEquals("Milly", person.getName());
        assertEquals("77777", person.getZipcode());
        assertEquals("made up too", person.getCity());
        assertEquals("rot", person.getColor());
    }

    @Test
    void when_loadPersonFromCsvEntry_withRunes_ok() throws IOException {
        // given
        String[] line = "Andersson, Anders, 32132 Schweden - ☀, 2".split(",");

        // when
        Person person = csvPersonsRepository.parsePersonFromCsvEntry(1, line);

        // then
        assertNotNull(person);
        assertEquals(1, person.getId());
        assertEquals("Andersson", person.getLastname());
        assertEquals("Anders", person.getName());
        assertEquals("32132", person.getZipcode());
        assertEquals("Schweden - ☀", person.getCity());
        assertEquals("grün", person.getColor());
    }

    @Test
    void when_loadPersonFromCsvEntry_withNotEnoughColumns_fail() throws IOException {
        // given
        String[] line = "Bart, Bertram, ".split(",");

        // when
        Person person = csvPersonsRepository.parsePersonFromCsvEntry(1, line);

        // then
        assertNull(person);
    }

    @Test
    void when_loadPersonFromCsvEntry_withNonNumericColor_fail() throws IOException {
        // given
        String[] line = "Klaussen, Klaus, 43246 Hierach, zwei".split(",");

        // when
        Person person = csvPersonsRepository.parsePersonFromCsvEntry(1, line);

        // then
        assertNull(person);
    }

    void mockLoadPersons() throws IOException {
        String csvContent = "Müller, Hans, 67742 Lauterecken, 1\r\nPetersen, Peter, 18439 Stralsund, 2\r\n" + //
                "Johnson, Johnny, 88888 made up, 3";
        InputStream csvInputStream = new ByteArrayInputStream(csvContent.getBytes(StandardCharsets.UTF_8));

        when(resourceLoader.getResource("classpath:sample-input.csv")).thenReturn(resource);
        when(resource.getInputStream()).thenReturn(csvInputStream);

        csvPersonsRepository.loadPersons();
    }
}
