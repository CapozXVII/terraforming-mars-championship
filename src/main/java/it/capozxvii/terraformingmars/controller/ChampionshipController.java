package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.model.dto.ChampionshipDto;
import it.capozxvii.terraformingmars.service.IChampionshipService;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    @GetMapping("/all-championships")
    @Transactional
    public ResponseEntity<CollectionWrapper<ChampionshipDto>> getAllChampionships() {
        return ResponseEntity.ok(CollectionWrapper.<ChampionshipDto>builder()
                                         .responseObject(championshipService.getAllChampionships()).build());
    }

    @GetMapping("/get-by-id")
    @Transactional
    public ResponseEntity<SimpleWrapper<ChampionshipDto>> getChampionshipById(

            @RequestParam("championshipId") final Long championshipId) {
        try {
            return ResponseEntity.ok(SimpleWrapper.<ChampionshipDto>builder()
                                             .responseObject(championshipService.getChampionshipById(championshipId))
                                             .build());
        } catch (TerraformingMarsException e) {
            return ResponseEntity.internalServerError()
                    .body(SimpleWrapper.<ChampionshipDto>builder().message(e.getMessage()).build());
        }

    }

    @GetMapping("get-by-name")
    @Transactional
    public ResponseEntity<SimpleWrapper<ChampionshipDto>> getChampionshipByName(
            @RequestParam("championship-name") final String championshipName) {
        try {
            return ResponseEntity.ok(SimpleWrapper.<ChampionshipDto>builder()
                                             .responseObject(
                                                     championshipService.getChampionshipByName(championshipName))
                                             .build());
        } catch (TerraformingMarsException e) {
            return ResponseEntity.internalServerError()
                    .body(SimpleWrapper.<ChampionshipDto>builder().message(e.getMessage()).build());
        }
    }
}
