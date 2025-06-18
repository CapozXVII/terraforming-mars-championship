package it.capozxvii.terraformingmars.repository;

import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Points;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PointsRepository extends JpaRepository<Points, Long> {
    List<Points> findByGame(Game game);
}
