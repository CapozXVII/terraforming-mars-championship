package it.capozxvii.terraformingmars.repository;

import it.capozxvii.terraformingmars.model.jpa.Championship;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChampionshipRepository extends JpaRepository<Championship, Long> {
    Optional<Championship> findByName(String name);
    
    @Query("select new championship(ch.championshipId, ch.name) from championship ch")
    List<Championship> getAllNamesAndIds();
}
