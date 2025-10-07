package it.capozxvii.terraformingmars.model.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import jakarta.persistence.Converter;
import java.util.Map;

@Converter
public class DraftingCorporationsForGamesConverter extends AbstractConverter<Integer, CorporationsExpansionPairDto> {
    public String convertToDatabaseColumn(final Map<Integer, CorporationsExpansionPairDto> draftingCorporations) {
        return super.convertToDatabaseColumn(draftingCorporations);
    }

    public Map<Integer, CorporationsExpansionPairDto> convertToEntityAttribute(final String draftingCorporationsJson) {
        return super.convertToEntityAttribute(draftingCorporationsJson, new TypeReference<>() {
        });
    }
}
