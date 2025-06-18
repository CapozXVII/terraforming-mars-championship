package it.capozxvii.terraformingmars.service;

import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;


public interface IPlayerService {
    PlayerDto insertPlayer(PlayerDto player);

    PlayerDto updatePlayer(PlayerDto player);

    void deletePlayer(PlayerID playerID);

    PlayerDto getPlayerById(PlayerID playerID);
}
