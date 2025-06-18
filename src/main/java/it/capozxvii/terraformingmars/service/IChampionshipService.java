package it.capozxvii.terraformingmars.service;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;

public interface IChampionshipService {
    ChampionshipDto createChampionship(ChampionshipDto championshipDto);

    ChampionshipDto getChampionshipByName(String championshipName);
}
