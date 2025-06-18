package it.capozxvii.terraformingmars.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.jpa.converter.CorporationConverter;
import org.junit.jupiter.api.Test;
public class CorporationConverterTest {
    private static final CorporationConverter CORPORATION_CONVERTER = new CorporationConverter();

    @Test
    void convertToDatabaseColumnTest() {
        assertEquals("Valley Trust", CORPORATION_CONVERTER.convertToDatabaseColumn(PreludeCorporations.VALLEY_TRUST));
    }

    @Test
    void convertToEntityAttributeTest() {
        assertEquals(PreludeCorporations.VALLEY_TRUST, CORPORATION_CONVERTER.convertToEntityAttribute("Valley Trust"));
    }

}
