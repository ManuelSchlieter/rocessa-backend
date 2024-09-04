package org.rocessa.features.persons;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class PersonsServiceImplTest {

    @Mock
    PersonsRepository personsRepository;

    @InjectMocks
    PersonsServiceImpl personsService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

}
