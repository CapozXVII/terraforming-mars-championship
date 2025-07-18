package it.capozxvii.terraformingmars.service.impl;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.model.jpa.Championship;
import it.capozxvii.terraformingmars.model.mapper.ChampionshipMapper;
import it.capozxvii.terraformingmars.repository.ChampionshipRepository;
import it.capozxvii.terraformingmars.service.IChampionshipService;
import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ChampionshipService implements IChampionshipService {

    private static final Logger LOG = LoggerFactory.getLogger(ChampionshipService.class);

    private final ChampionshipRepository championshipRepository;

    private final ChampionshipMapper championshipMapper;

    public ChampionshipService(final ChampionshipRepository championshipRepository,
            final ChampionshipMapper championshipMapper) {
        this.championshipRepository = championshipRepository;
        this.championshipMapper = championshipMapper;
    }

    @Override
    @Transactional
    public ChampionshipDto createChampionship(final ChampionshipDto championshipDto) {
        LOG.info("Inserting champ {} into DB", championshipDto);
        return championshipMapper.toDto(championshipRepository.save(championshipMapper.toEntity(championshipDto)));
    }

    @Override
    @Transactional
    public ChampionshipDto getChampionshipByName(final String championshipName) throws TerraformingMarsException {
        Championship championship = championshipRepository.findByName(championshipName).orElseThrow(
                () -> new TerraformingMarsException(Message.NOT_FOUND, Championship.class.getSimpleName(),
                                                    championshipName));
        return championshipMapper.toDto(championship);
    }

    @Override
    public List<ChampionshipDto> getAllChampionships() {
        LOG.info("Retrieving all championships");
        return championshipRepository.getAllNamesAndIds().stream().map(championshipMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ChampionshipDto getChampionshipById(final Long championshipId) throws TerraformingMarsException {
        LOG.info("Retrieving championship with id [{}]", championshipId);
        return championshipRepository.findById(championshipId).stream().map(championshipMapper::toDto).findAny()
                .orElseThrow(() -> new TerraformingMarsException(Message.NOT_FOUND, Championship.class.getSimpleName(),
                                                                 championshipId));
    }
}
