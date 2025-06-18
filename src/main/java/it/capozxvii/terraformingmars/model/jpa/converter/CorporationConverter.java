package it.capozxvii.terraformingmars.model.jpa.converter;

import it.capozxvii.terraformingmars.model.enums.corporation.Corporation;
import it.capozxvii.terraformingmars.util.CorporationFactory;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class CorporationConverter implements AttributeConverter<Corporation, String> {
    @Override
    public String convertToDatabaseColumn(final Corporation corporation) {
        return corporation.getName();
    }

    @Override
    public Corporation convertToEntityAttribute(final String name) {
        return CorporationFactory.fromName(name);
    }

}

