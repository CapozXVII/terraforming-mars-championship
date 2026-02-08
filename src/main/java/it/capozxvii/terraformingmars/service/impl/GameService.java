package it.capozxvii.terraformingmars.service.impl;

import it.capozxvii.terraformingmars.model.dto.CorporationsExpansionPairDto;
import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Game;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.Points;
import it.capozxvii.terraformingmars.model.mapper.GameMapper;
import it.capozxvii.terraformingmars.model.mapper.PointsMapper;
import it.capozxvii.terraformingmars.repository.ChampionshipRepository;
import it.capozxvii.terraformingmars.repository.DraftingRepository;
import it.capozxvii.terraformingmars.repository.GameRepository;
import it.capozxvii.terraformingmars.repository.PlayerRepository;
import it.capozxvii.terraformingmars.repository.PointsRepository;
import it.capozxvii.terraformingmars.service.IGameService;
import it.capozxvii.terraformingmars.util.CorporationFactory;
import it.capozxvii.terraformingmars.util.Utils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GameService implements IGameService {

    private static final Logger LOG = LoggerFactory.getLogger(GameService.class);

    private final ChampionshipRepository championshipRepository;
    private final DraftingRepository draftingRepository;
    private final GameMapper gameMapper;
    private final GameRepository gameRepository;
    private final PointsMapper pointsMapper;
    private final PlayerRepository playerRepository;
    private final PointsRepository pointsRepository;
    private final Utils utils;

    public GameService(final ChampionshipRepository championshipRepository,
            final DraftingRepository draftingRepository,
            final GameMapper gameMapper,
            final GameRepository gameRepository,
            final PlayerRepository playerRepository,
            final PointsMapper pointsMapper,
            final PointsRepository pointsRepository,
            final Utils utils) {
        this.championshipRepository = championshipRepository;
        this.draftingRepository = draftingRepository;
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
        Championship championship =
                utils.checkAndGetEntity(championshipRepository, Championship.class, gameDto.getChampionshipId());
        game.setChampionship(championship);
        Game gameAfterSaving = gameRepository.save(game);
        gameDto.getPoints().forEach(pointsDto -> {
            PlayerDto playerDto = pointsDto.getPlayer();
            Player player = utils.checkAndGetEntity(playerRepository,
                                                    Player.class,
                                                    playerDto.getId());
            Points points = pointsMapper.toEntity(pointsDto);
            points.setGame(gameAfterSaving);
            points.setPlayer(player);
            pointsRepository.save(points);
            draftingRepository.getDraftingsByPlayerAndChampionship(player, championship).ifPresent(drafting -> {
                LOG.info("INSERTING GAME FLOW -- Retrieved drafting for player {} in championship {}",
                         player.getNickname(),
                         championship.getChampionshipId());
                Map<Long, CorporationsExpansionPairDto> drafts = drafting.getDraftings();
                if (drafts != null && drafts.containsKey(game.getGameId())) {
                    LOG.info("INSERTING GAME FLOW -- Added chosen drafting {} for player {} in championship {}",
                             pointsDto.getCorporation(), player.getNickname(),
                             championship.getChampionshipId());
                    drafts.get(game.getGameId()).setChosenExpansion(CorporationFactory.fromName(
                            pointsDto.getCorporation()).getExpansion());
                }
            });

        });
        return gameMapper.toDto(gameAfterSaving);
    }

    @Override
    @Transactional
    public GameDto editGame(final GameDto gameDto) {
        Game game = gameMapper.toEntity(gameDto);
        game.setGameId(gameDto.getId());
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
        return gameRepository.findByChampionshipChampionshipId(championshipId).stream().map(gameMapper::toDto)
                .collect(Collectors.toList());
    }
}
