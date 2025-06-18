package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.model.dto.PointsDto;
import it.capozxvii.terraformingmars.service.IPointsService;
import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Points Controller")
@RequestMapping(value = "/points")
public class PointsController {

    private final IPointsService pointsService;

    public PointsController(final IPointsService pointsService) {
        this.pointsService = pointsService;
    }

    @PostMapping(value = "/insert-points")
    public ResponseEntity<String> insertPoints(@RequestBody final List<PointsDto> pointsDtos,
            @RequestParam final Long gameId,
            @RequestParam final Long championshipId) {
        try {
            pointsService.insertPoints(pointsDtos, gameId, championshipId);
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError().body(terraformingMarsException.getMessage());
        }
        return ResponseEntity.ok(Message.POINTS_SUCCESSFULLY_SAVED);
    }

    @PutMapping(value = "/update-points")
    public ResponseEntity<SimpleWrapper<PointsDto>> updatePoints(@RequestBody final PointsDto pointsDto) {
        try {

            return ResponseEntity.ok()
                    .body(SimpleWrapper.<PointsDto>builder().responseObject(pointsService.updatePoints(pointsDto))
                                  .build());
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError().body(SimpleWrapper.<PointsDto>builder().message(
                    terraformingMarsException.getMessage()).build());
        }
    }

    @GetMapping(value = "/points-of-game")
    public ResponseEntity<CollectionWrapper<PointsDto>> getPointsOfGame(@RequestParam final Long gameId) {
        try {
            return ResponseEntity.ok(
                    CollectionWrapper.<PointsDto>builder().responseObject(pointsService.getPointsOfAGame(gameId))
                            .build());
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError().body(CollectionWrapper.<PointsDto>builder().message(
                    terraformingMarsException.getMessage()).build());
        }
    }
}
