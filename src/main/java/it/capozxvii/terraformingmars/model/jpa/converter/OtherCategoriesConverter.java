package it.capozxvii.terraformingmars.model.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import java.util.Map;

@Converter
public class OtherCategoriesConverter extends AbstractConverter<String, Integer> {
    public String convertToDatabaseColumn(final Map<String, Integer> otherCategories) {
        return super.convertToDatabaseColumn(otherCategories);
    }

    public Map<String, Integer> convertToEntityAttribute(final String otherCategoriesJson) {
        return super.convertToEntityAttribute(otherCategoriesJson, new TypeReference<>() {
        });
    }
}
