package de.fzj.atlascore.parcellation;

import de.fzj.atlascore.referencespace.ReferencespaceRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ParcellationServiceTest {

    private static final HashMap<String, Object> PROPERTIES = new HashMap<>() {{
        put("name", BIGBRAIN_ONE_PARCELLATION);
    }};
    private static final String REF_SPACE_BIGBRAIN = ReferencespaceRepository.BIG_BRAIN;
    private static final String BIGBRAIN_ONE_PARCELLATION = "Cytoarchitectonic Maps";
    private static final String REF_SPACE_COLIN = ReferencespaceRepository.MNI_COLIN_27;
    private static final String REF_SPACE_MIN = ReferencespaceRepository.MNI_152;
    private static final String INVALID_REF_SPACE_NAME = "smallbrain";
    private static final String PARCELLATION_NAME = "par1";

    @Mock
    private ReferencespaceRepository referencespaceRepository;

    @Mock
    private ParcellationRepository parcellationRepository;

    @InjectMocks
    private ParcellationService parcellationService;

    @Before
    public void setUp() {
        when(referencespaceRepository.isValid(anyString())).thenCallRealMethod();
    }

    @Test
    public void shouldReturnParcellationsForBigBrain() {
        when(parcellationRepository.findAllByReferencespace(REF_SPACE_BIGBRAIN)).thenReturn(Arrays.asList(
                new Parcellation(PROPERTIES)
        ));
        List<Parcellation> parcellations = parcellationService.getParcellations(REF_SPACE_BIGBRAIN);

        assertThat(parcellations, not(empty()));
        assertThat(parcellations, hasItem(new Parcellation(PROPERTIES)));
    }

    @Test
    public void shouldReturnParcellationsForColin() {
        when(parcellationRepository.findAllByReferencespace(REF_SPACE_COLIN)).thenReturn(Arrays.asList(
                new Parcellation(PROPERTIES)
        ));
        List<Parcellation> parcellations = parcellationService.getParcellations(REF_SPACE_COLIN);

        assertThat(parcellations, not(empty()));
        assertThat(parcellations, hasItem(new Parcellation(PROPERTIES)));
    }

    @Test
    public void shouldReturnParcellationsForMni() {
        when(parcellationRepository.findAllByReferencespace(REF_SPACE_MIN)).thenReturn(Arrays.asList(
                new Parcellation(PROPERTIES)
        ));
        List<Parcellation> parcellations = parcellationService.getParcellations(REF_SPACE_MIN);

        assertThat(parcellations, not(empty()));
        assertThat(parcellations, hasItem(new Parcellation(PROPERTIES)));
    }

    @Test
    public void shouldThrowNotFoundExceptionForInvalidRefSpace() {
        try {
            parcellationService.getParcellations(INVALID_REF_SPACE_NAME);
            fail("ResponseStatusException expected");
        } catch(ResponseStatusException e) {
            assertEquals(String.format("Referencespace: %s not found", INVALID_REF_SPACE_NAME), e.getReason());
            assertEquals(HttpStatus.NOT_FOUND.value(), e.getStatus().value());
        }
    }

    @Test
    public void shouldReturnParcellationByName() {
        when(parcellationRepository.findOneByReferencespaceAndName(REF_SPACE_COLIN, PARCELLATION_NAME)).thenReturn(
                new Parcellation(PROPERTIES)
        );
        Parcellation parcellation = parcellationService.getParcellationByName(REF_SPACE_COLIN, PARCELLATION_NAME);

        assertEquals(new Parcellation(PROPERTIES), parcellation);
    }

    @Test
    public void shouldThrowNotFoundExceptionIfRefSpaceNotValid() {
        try {
            parcellationService.getParcellationByName(INVALID_REF_SPACE_NAME, PARCELLATION_NAME);
            fail("ResponseStatusException expected");
        } catch(ResponseStatusException e) {
            assertEquals(String.format("Referencespace: %s not found", INVALID_REF_SPACE_NAME), e.getReason());
            assertEquals(HttpStatus.NOT_FOUND.value(), e.getStatus().value());
        }
    }
}
