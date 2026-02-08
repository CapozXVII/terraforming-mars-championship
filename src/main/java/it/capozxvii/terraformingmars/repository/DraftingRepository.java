package it.capozxvii.terraformingmars.repository;

import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Drafting;
import it.capozxvii.terraformingmars.model.jpa.Player;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DraftingRepository extends JpaRepository<Drafting, Long> {
    Optional<Drafting> getDraftingsByPlayerAndChampionship(Player player, Championship championship);

    List<Drafting> getDraftingsByChampionship(Championship championship);
}
