package it.capozxvii.terraformingmars.service.impl;

import it.capozxvii.terraformingmars.model.dto.DraftingDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.jpa.Drafting;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.mapper.DraftingMapper;
import it.capozxvii.terraformingmars.repository.ChampionshipRepository;
import it.capozxvii.terraformingmars.repository.DraftingRepository;
import it.capozxvii.terraformingmars.repository.PlayerRepository;
import it.capozxvii.terraformingmars.service.IDraftingService;
import it.capozxvii.terraformingmars.util.Utils;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DraftingService implements IDraftingService {

    private final PlayerRepository playerRepository;

    private final ChampionshipRepository championshipRepository;

    private final DraftingRepository draftingRepository;

    private final DraftingMapper draftingMapper;

    private final Utils utils;

    public DraftingService(final PlayerRepository playerRepository,
            final ChampionshipRepository championshipRepository,
            final DraftingRepository draftingRepository,
            final DraftingMapper draftingMapper,
            final Utils utils) {
        this.playerRepository = playerRepository;
        this.championshipRepository = championshipRepository;
        this.draftingRepository = draftingRepository;
        this.utils = utils;
        this.draftingMapper = draftingMapper;
    }

    @Override
    @Transactional
    public List<DraftingDto> insertDrafting(final List<DraftingDto> draftingDtos) {
        return draftingDtos.stream().map(previsionDto -> {
            Player player = utils.checkAndGetEntity(playerRepository, Player.class, previsionDto.getPlayerId());
            Championship championship = utils.checkAndGetEntity(championshipRepository, Championship.class,
                    previsionDto.getChampionshipId());

            return draftingMapper.toDto(draftingRepository.save(
                    draftingMapper.toEntity(player, championship, previsionDto.getDraftings())));

        }).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public List<DraftingDto> editDrafting(final DraftingDto previsionDto) {
        Player player = utils.checkAndGetEntity(playerRepository, Player.class, previsionDto.getPlayerId());
        Championship championship = utils.checkAndGetEntity(championshipRepository, Championship.class,
                previsionDto.getChampionshipId());
        Drafting prevision = utils.checkAndGetEntity(draftingRepository, Drafting.class, previsionDto.getId());
        prevision.getDraftings().putAll(previsionDto.getDraftings());
        return List.of(draftingMapper.toDto(draftingRepository.save(
                draftingMapper.toEntity(player, championship, prevision.getDraftings()))));
    }
}
