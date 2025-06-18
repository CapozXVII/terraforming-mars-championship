package it.capozxvii.terraformingmars.util;

import it.capozxvii.terraformingmars.model.jpa.AbstractEntity;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

@Component
public class Utils {
    public <E extends AbstractEntity> E checkAndGetEntity(final JpaRepository<E, Long> repository,
            final Class<E> objectClass,
            final Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new TerraformingMarsException(Message.NOT_FOUND, objectClass.getSimpleName(), id));
    }

    public <E extends AbstractEntity> Optional<E> checkAndGetEntity(final JpaRepository<E, Long> repository,
            final Long id) {
        return repository.findById(id);
    }

    public Player checkAndGetPlayer(final JpaRepository<Player, PlayerID> repository,
            final PlayerID id) throws TerraformingMarsException {
        return repository.findById(id)
                .orElseThrow(() -> new TerraformingMarsException(Message.NOT_FOUND, Player.class.getSimpleName(),
                                                                 id.getId() + ", nickname " + id.getNickname()));
    }

}
