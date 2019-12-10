package de.fzj.atlascore.parcellation;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class ParcellationResourceTest {

    private String refSpace;
    private String inputName;

    @Parameterized.Parameters
    public static Collection input() {
        return Arrays.asList(new Object[][] {
                {"ref1", "par1"},
                {"ref1", "par2"}
        });
    }

    public ParcellationResourceTest(String refSpace, String inputName) {
        this.refSpace = refSpace;
        this.inputName = inputName;
    }

    @Test
    public void shouldCreateParcellationResourceAndAddLinks() {
        Parcellation parcellation = new Parcellation(inputName);
        ParcellationResource parcellationResource = new ParcellationResource(parcellation, refSpace);

        assertEquals(inputName, parcellationResource.getParcellation().getName());
        assertEquals(
                "/referencespaces/" + refSpace + "/parcellations/" + inputName,
                parcellationResource.getLink("self").getHref()
        );
        assertEquals(
                "/referencespaces/" + refSpace + "/parcellations/" + inputName + "/regions",
                parcellationResource.getLink("regions").getHref()
        );
        assertThat(parcellationResource.getLinks(), hasSize(2));
    }
}