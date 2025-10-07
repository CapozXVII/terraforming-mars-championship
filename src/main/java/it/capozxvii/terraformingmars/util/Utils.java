package it.capozxvii.terraformingmars.util;

import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    public <E> E checkAndGetEntity(final JpaRepository<E, Long> repository,
            final Class<E> objectClass,
            final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TerraformingMarsException(Message.NOT_FOUND, objectClass.getSimpleName(), id));
    }

}
