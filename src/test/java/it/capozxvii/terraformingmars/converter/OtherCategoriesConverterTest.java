package it.capozxvii.terraformingmars.converter;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import it.capozxvii.terraformingmars.model.jpa.converter.OtherCategoriesConverter;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.Test;

class OtherCategoriesConverterTest {
    private static final OtherCategoriesConverter OTHER_CATEGORIES_CONVERTER =
            new OtherCategoriesConverter();

    @Test
    void convertToDatabaseColumnTest() {
        Map<String, Integer> otherCategories = new HashMap<>();
        otherCategories.put("category", 15);
        otherCategories.put("category2", 30);
        String res = OTHER_CATEGORIES_CONVERTER.convertToDatabaseColumn(otherCategories);
        assertEquals("""
                             {"category2":30,"category":15}""", res);
    }

    @Test
    void convertToEntityAttributeTest() {
        Map<String, Integer> otherCategories =
                OTHER_CATEGORIES_CONVERTER.convertToEntityAttribute("{\"category2\":30,\"category\":15}");
        assertEquals(15, otherCategories.get("category"));
        assertEquals(30, otherCategories.get("category2"));
    }

    @Test
    void convertToEntityAttributeExceptionTest() {
        assertDoesNotThrow(
                () -> OTHER_CATEGORIES_CONVERTER.convertToEntityAttribute(
                        "\"category2\":30,\"category\":15"));
    }

    @Test
    void convertToDatabaseColumnExceptionTest() {
        OtherCategoriesConverter converter = mock(OtherCategoriesConverter.class);
        Map<String, Integer> otherCategories = new HashMap<>();
        otherCategories.put("category", 15);
        otherCategories.put("category2", 30);
        doThrow(new TerraformingMarsException("Error while converting [{\"category2\":30,\"category\":15}] to JSON"))
                .when(converter).convertToDatabaseColumn(otherCategories);
        TerraformingMarsException res = assertThrows(TerraformingMarsException.class,
                                                     () -> converter.convertToDatabaseColumn(
                                                             otherCategories));
        assertEquals("Error while converting [{\"category2\":30,\"category\":15}] to JSON", res.getMessage());
    }

}
