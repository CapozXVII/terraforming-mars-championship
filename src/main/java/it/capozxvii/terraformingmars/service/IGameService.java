package it.capozxvii.terraformingmars.service;

import it.capozxvii.terraformingmars.model.dto.GameDto;
import java.util.List;

public interface IGameService {
    GameDto insertGame(GameDto gameDto);

    GameDto editGame(GameDto gameDto);

    List<GameDto> findByLocation(String location);
}
