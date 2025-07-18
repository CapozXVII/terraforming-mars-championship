package it.capozxvii.terraformingmars.service;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import java.util.List;

public interface IChampionshipService {
    ChampionshipDto createChampionship(ChampionshipDto championshipDto);

    ChampionshipDto getChampionshipByName(String championshipName);
    
    List<ChampionshipDto> getAllChampionships();
    
    ChampionshipDto getChampionshipById(Long championshipId);
}
