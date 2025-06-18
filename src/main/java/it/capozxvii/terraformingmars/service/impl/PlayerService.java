package it.capozxvii.terraformingmars.service.impl;

import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import it.capozxvii.terraformingmars.model.mapper.PlayerMapper;
import it.capozxvii.terraformingmars.repository.PlayerRepository;
import it.capozxvii.terraformingmars.service.IPlayerService;
import it.capozxvii.terraformingmars.util.Utils;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerService implements IPlayerService {

    private static final Logger LOG = LoggerFactory.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    private final Utils utils;

    private final PlayerMapper playerMapper;

    public PlayerService(final PlayerRepository playerRepository, final Utils utils, final PlayerMapper playerMapper) {
        this.playerRepository = playerRepository;
        this.utils = utils;
        this.playerMapper = playerMapper;
    }

    @Override
    @Transactional
    public PlayerDto insertPlayer(final PlayerDto player) {
        return playerMapper.toDto(playerRepository.save(playerMapper.toEntity(player)));
    }

    @Override
    @Transactional
    public PlayerDto updatePlayer(final PlayerDto player) {
        Player pl = playerMapper.toEntity(player);
        pl.setId(player.getId());
        return playerMapper.toDto(playerRepository.save(pl));
    }

    @Override
    @Transactional
    public void deletePlayer(final PlayerID playerID) {
        playerRepository.deleteById(playerID);
    }

    @Override
    @Transactional
    public PlayerDto getPlayerById(final PlayerID playerID) throws TerraformingMarsException {
        return playerMapper.toDto(utils.checkAndGetPlayer(playerRepository, playerID));
    }
}
