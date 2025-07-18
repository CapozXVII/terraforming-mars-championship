package it.capozxvii.terraformingmars.repository;

import it.capozxvii.terraformingmars.model.jpa.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> {
}
