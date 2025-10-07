package it.capozxvii.terraformingmars.converter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.CorporateEraCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.VenusNextCorporations;
import it.capozxvii.terraformingmars.model.jpa.converter.DraftingCorporationsForGamesConverter;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DraftingCorporationsForGamesConverterTest {

    private static final DraftingCorporationsForGamesConverter DRAFTING_CORPORATIONS_DECKS_FOR_GAMES =
            new DraftingCorporationsForGamesConverter();

    @Test
    void convertToDatabaseColumnTest() {
        Map<Integer, CorporationsExpansionPairDto> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, CorporationsExpansionPairDto.builder()
                .firstExpansion(VenusNextCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(2, CorporationsExpansionPairDto.builder()
                .firstExpansion(CorporateEraCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        corporationDecksToDraft.put(3, CorporationsExpansionPairDto.builder()
                .firstExpansion(ColoniesCorporations.EXPANSION).secondExpansion(PreludeCorporations.EXPANSION)
                .build());
        String res = DRAFTING_CORPORATIONS_DECKS_FOR_GAMES.convertToDatabaseColumn(corporationDecksToDraft);
        assertEquals("""
                             {"1":{"firstExpansion":"Venus Next","secondExpansion":"Prelude"},""" 
                     + """
                             "2":{"firstExpansion":"Corporate Era","secondExpansion":"Prelude"},""" 
                     + """
                             "3":{"firstExpansion":"Colonies","secondExpansion":"Prelude"}}""",
                     res);
    }

    @Test
    void convertToEntityAttributeTest() {
        Map<Integer, CorporationsExpansionPairDto> draftedCorps =
                DRAFTING_CORPORATIONS_DECKS_FOR_GAMES.convertToEntityAttribute(
                        "{\"1\":{\"firstExpansion\":\"Venus Next\",\"secondExpansion\":\"Prelude\"},"
                        + "\"2\":{\"firstExpansion\":\"Corporate Era\",\"secondExpansion\":\"Prelude\"},"
                        + "\"3\":{\"firstExpansion\":\"Colonies\",\"secondExpansion\":\"Prelude\"}}");
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(VenusNextCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftedCorps.get(1));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(CorporateEraCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftedCorps.get(2));
        assertEquals(CorporationsExpansionPairDto.builder()
                             .firstExpansion(ColoniesCorporations.EXPANSION)
                             .secondExpansion(PreludeCorporations.EXPANSION)
                             .build(), draftedCorps.get(3));
    }

    @Test
    void convertToEntityAttributeExceptionTest() {
        assertEquals(0, assertDoesNotThrow(() -> DRAFTING_CORPORATIONS_DECKS_FOR_GAMES
                .convertToEntityAttribute(
                        "\"1\":[\"VenusNext\",\"Prelude\"],"
                        + "\"2\":[\"CorporateEra\",\"Prelude\"],"
                        + "\"3\":[\"Colonies\",\"Prelude\"]}")).size());
    }

    @Test
    void convertToDatabaseColumnExceptionTest() {
        DraftingCorporationsForGamesConverter converter = mock(DraftingCorporationsForGamesConverter.class);
        Map<Integer, CorporationsExpansionPairDto> draftedCorps = new HashMap<>();
        draftedCorps.put(1, null);
        draftedCorps.put(2, null);
        doThrow(new TerraformingMarsException("Error while converting [{\"1\":null,\"2\":null}] to JSON")).when(
                converter).convertToDatabaseColumn(draftedCorps);
        TerraformingMarsException res = assertThrows(TerraformingMarsException.class,
                                                     () -> converter.convertToDatabaseColumn(
                                                             draftedCorps));
        assertEquals("Error while converting [{\"1\":null,\"2\":null}] to JSON", res.getMessage());
    }

}
