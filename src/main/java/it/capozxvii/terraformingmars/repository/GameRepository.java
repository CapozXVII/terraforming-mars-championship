package it.capozxvii.terraformingmars.repository;

import it.capozxvii.terraformingmars.model.jpa.Game;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GameRepository extends JpaRepository<Game, Long> {
    List<Game> findByLocation(String location);

    List<Game> findByChampionshipChampionshipId(Long championshipId);
}
