package it.capozxvii.terraformingmars.model.jpa.converter;

import com.fasterxml.jackson.core.type.TypeReference;
import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import jakarta.persistence.Converter;
import java.util.Map;

@Converter
public class DraftingCorporationsForGamesConverter extends AbstractConverter<Long, CorporationsExpansionPairDto> {
    public String convertToDatabaseColumn(final Map<Long, CorporationsExpansionPairDto> draftingCorporations) {
        return super.convertToDatabaseColumn(draftingCorporations);
    }

    public Map<Long, CorporationsExpansionPairDto> convertToEntityAttribute(final String draftingCorporationsJson) {
        return super.convertToEntityAttribute(draftingCorporationsJson, new TypeReference<>() {
        });
    }
}
