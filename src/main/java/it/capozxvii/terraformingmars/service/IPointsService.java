package it.capozxvii.terraformingmars.service;

import it.capozxvii.terraformingmars.model.dto.PointsDto;
import java.util.List;

public interface IPointsService {
    void insertPoints(List<PointsDto> pointsDtoList, Long gameId, Long championshipId);

    PointsDto updatePoints(PointsDto pointsDto);

    List<PointsDto> getPointsOfAGame(Long gameId);
}
