package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.service.IChampionshipService;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Championship Controller")
@RequestMapping(value = "/championship")
public class ChampionshipController {
    private final IChampionshipService championshipService;

    public ChampionshipController(final IChampionshipService championshipService) {
        this.championshipService = championshipService;
    }

    @PostMapping("/insert-championship")
    public ResponseEntity<SimpleWrapper<ChampionshipDto>> insertChampionship(
            @RequestBody final ChampionshipDto championshipDto) {
        try {
            return ResponseEntity.ok(SimpleWrapper.<ChampionshipDto>builder()
                                             .responseObject(championshipService.createChampionship(championshipDto))
                                             .build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(SimpleWrapper.<ChampionshipDto>builder().message(e.getMessage()).build());
        }
    }
}
