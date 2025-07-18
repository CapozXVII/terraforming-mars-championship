package it.capozxvii.terraformingmars.model.jpa.converter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import jakarta.persistence.AttributeConverter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class AbstractConverter<K, V> implements AttributeConverter<Map<K, V>, String> {

    private static final Logger LOG = LoggerFactory.getLogger(AbstractConverter.class);

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public String convertToDatabaseColumn(final Map<K, V> otherCategories) {

        String categories;
        try {
            categories = MAPPER.writeValueAsString(otherCategories);
        } catch (final JsonProcessingException e) {
            LOG.error("JSON writing error", e);
            throw new TerraformingMarsException(Message.ERROR_WHILE_CONVERTING_TO_JSON, otherCategories);
        }
        return categories;
    }

    public Map<K, V> convertToEntityAttribute(final String otherCategoriesJson,
            final TypeReference<Map<K, V>> typeReference) {
        try {
            return MAPPER.readValue(otherCategoriesJson, typeReference);
        } catch (final IOException e) {
            LOG.error("JSON reading error", e);
            return new HashMap<>();
        }
    }

}
