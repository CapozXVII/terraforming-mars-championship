package it.capozxvii.terraformingmars.service.impl;

import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.Points;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import it.capozxvii.terraformingmars.model.mapper.GameMapper;
import it.capozxvii.terraformingmars.model.mapper.PointsMapper;
import it.capozxvii.terraformingmars.repository.ChampionshipRepository;
import it.capozxvii.terraformingmars.repository.GameRepository;
import it.capozxvii.terraformingmars.repository.PlayerRepository;
import it.capozxvii.terraformingmars.repository.PointsRepository;
import it.capozxvii.terraformingmars.service.IGameService;
import it.capozxvii.terraformingmars.util.Utils;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService implements IGameService {
    private final ChampionshipRepository championshipRepository;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final PointsMapper pointsMapper;
    private final PlayerRepository playerRepository;
    private final PointsRepository pointsRepository;
    private final Utils utils;

    public GameService(final ChampionshipRepository championshipRepository, final GameMapper gameMapper,
            final GameRepository gameRepository, 
            final PlayerRepository playerRepository,
            final PointsMapper pointsMapper,
            final PointsRepository pointsRepository, 
            final Utils utils) {
        this.championshipRepository = championshipRepository;
        this.gameMapper = gameMapper;
        this.gameRepository = gameRepository;
        this.playerRepository = playerRepository;
        this.pointsMapper = pointsMapper;
        this.pointsRepository = pointsRepository;
        this.utils = utils;
    }

    @Override
    @Transactional
    public GameDto insertGame(final GameDto gameDto) {
        Game game = gameMapper.toEntity(gameDto);
        game.setChampionship(
                utils.checkAndGetEntity(championshipRepository, Championship.class, gameDto.getChampionshipId()));
        Game gameAfterSaving = gameRepository.save(game);
        gameDto.getPoints().forEach(pointsDto -> {
            PlayerDto playerDto = pointsDto.getPlayer();
            Player player = utils.checkAndGetPlayer(playerRepository,
                                                    PlayerID.builder().id(playerDto.getId())
                                                            .nickname(playerDto.getNickname()).build());
            Points points = pointsMapper.toEntity(pointsDto);
            points.setGame(gameAfterSaving);
            points.setPlayer(player);
            pointsRepository.save(points);
        });
        return gameMapper.toDto(gameAfterSaving);
    }

    @Override
    @Transactional
    public GameDto editGame(final GameDto gameDto) {
        Game game = gameMapper.toEntity(gameDto);
        game.setId(gameDto.getId());
        game.setChampionship(
                utils.checkAndGetEntity(championshipRepository, Championship.class, gameDto.getChampionshipId()));
        return gameMapper.toDto(gameRepository.save(game));
    }

    @Override
    @Transactional
    public List<GameDto> findByLocation(final String location) {
        return gameRepository.findByLocation(location).stream().map(gameMapper::toDto).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<GameDto> findByChampionshipId(final Long championshipId) {
        return gameRepository.findByChampionshipId(championshipId).stream().map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
}
