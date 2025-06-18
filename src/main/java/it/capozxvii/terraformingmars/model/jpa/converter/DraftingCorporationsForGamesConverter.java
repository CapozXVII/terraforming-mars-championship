package it.capozxvii.terraformingmars.model.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.persistence.Converter;
import java.util.List;
import java.util.Map;

@Converter
public class DraftingCorporationsForGamesConverter extends AbstractConverter<Integer, List<String>> {
    public String convertToDatabaseColumn(final Map<Integer, List<String>> draftingCorporations) {
        return super.convertToDatabaseColumn(draftingCorporations);
    }

    public Map<Integer, List<String>> convertToEntityAttribute(final String draftingCorporationsJson) {
        return super.convertToEntityAttribute(draftingCorporationsJson, new TypeReference<>() {
        });
    }
}
