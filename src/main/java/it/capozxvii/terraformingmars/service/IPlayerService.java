package it.capozxvii.terraformingmars.service;

import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import java.util.List;


public interface IPlayerService {
    PlayerDto insertPlayer(PlayerDto player);

    PlayerDto updatePlayer(PlayerDto player);

    void deletePlayer(Long playerId);

    PlayerDto getPlayerById(Long playerId);
    
    List<PlayerDto> getAllPlayers();
}
