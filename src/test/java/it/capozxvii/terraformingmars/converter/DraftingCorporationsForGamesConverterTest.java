package it.capozxvii.terraformingmars.converter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import it.capozxvii.terraformingmars.model.enums.corporation.ColoniesCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.CorporateEraCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.PreludeCorporations;
import it.capozxvii.terraformingmars.model.enums.corporation.VenusNextCorporations;
import it.capozxvii.terraformingmars.model.jpa.converter.DraftingCorporationsForGamesConverter;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;

class DraftingCorporationsForGamesConverterTest {

    private static final DraftingCorporationsForGamesConverter DRAFTING_CORPORATIONS_DECKS_FOR_GAMES =
            new DraftingCorporationsForGamesConverter();

    @Test
    void convertToDatabaseColumnTest() {
        Map<Integer, List<String>> corporationDecksToDraft = new HashMap<>();
        corporationDecksToDraft.put(1, List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(2, List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        corporationDecksToDraft.put(3, List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION));
        String res = DRAFTING_CORPORATIONS_DECKS_FOR_GAMES.convertToDatabaseColumn(corporationDecksToDraft);
        assertEquals("""
                             {"1":["VenusNext","Prelude"],"2":["CorporateEra","Prelude"],"3":["Colonies","Prelude"]}""",
                     res);
    }

    @Test
    void convertToEntityAttributeTest() {
        Map<Integer, List<String>> predictedChars =
                DRAFTING_CORPORATIONS_DECKS_FOR_GAMES.convertToEntityAttribute(
                        "{\"1\":[\"VenusNext\",\"Prelude\"],"
                        + "\"2\":[\"CorporateEra\",\"Prelude\"],"
                        + "\"3\":[\"Colonies\",\"Prelude\"]}");
        assertEquals(List.of(VenusNextCorporations.EXPANSION, PreludeCorporations.EXPANSION), predictedChars.get(1));
        assertEquals(List.of(CorporateEraCorporations.EXPANSION, PreludeCorporations.EXPANSION), predictedChars.get(2));
        assertEquals(List.of(ColoniesCorporations.EXPANSION, PreludeCorporations.EXPANSION), predictedChars.get(3));
    }

    @Test
    void convertToEntityAttributeExceptionTest() {
        TerraformingMarsException res = assertThrows(TerraformingMarsException.class,
                                                     () -> DRAFTING_CORPORATIONS_DECKS_FOR_GAMES
                                                             .convertToEntityAttribute(
                                                                     "\"1\":[\"VenusNext\",\"Prelude\"],"
                                                                     + "\"2\":[\"CorporateEra\",\"Prelude\"],"
                                                                     + "\"3\":[\"Colonies\",\"Prelude\"]}"));
        assertEquals(
                "Error while converting [\"1\":[\"VenusNext\",\"Prelude\"],"
                + "\"2\":[\"CorporateEra\",\"Prelude\"],"
                + "\"3\":[\"Colonies\",\"Prelude\"]}] to Object",
                res.getMessage());
    }

    @Test
    void convertToDatabaseColumnExceptionTest() {
        DraftingCorporationsForGamesConverter converter = mock(DraftingCorporationsForGamesConverter.class);
        Map<Integer, List<String>> predictedChars = new HashMap<>();
        predictedChars.put(1, null);
        predictedChars.put(2, null);
        doThrow(new TerraformingMarsException("Error while converting [{\"1\":null,\"2\":null}] to JSON")).when(
                converter).convertToDatabaseColumn(predictedChars);
        TerraformingMarsException res = assertThrows(TerraformingMarsException.class,
                                                     () -> converter.convertToDatabaseColumn(
                                                             predictedChars));
        assertEquals("Error while converting [{\"1\":null,\"2\":null}] to JSON", res.getMessage());
    }

}
