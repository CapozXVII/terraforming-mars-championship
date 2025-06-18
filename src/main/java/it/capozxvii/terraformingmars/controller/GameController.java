package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.model.dto.GameDto;
import it.capozxvii.terraformingmars.service.IGameService;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Game Controller")
@RequestMapping(value = "/game")
public class GameController {

    private final IGameService gameService;

    public GameController(final IGameService gameService) {
        this.gameService = gameService;
    }

    @PostMapping("/insert-game")
    public ResponseEntity<SimpleWrapper<GameDto>> insertGame(@RequestBody final GameDto gameDto) {
        try {
            return ResponseEntity.ok()
                    .body(SimpleWrapper.<GameDto>builder().responseObject(gameService.insertGame(gameDto)).build());
        } catch (TerraformingMarsException e) {
            return ResponseEntity.internalServerError()
                    .body(SimpleWrapper.<GameDto>builder().message(e.getMessage()).build());
        }
    }

    @PutMapping("/edit-game")
    public ResponseEntity<SimpleWrapper<GameDto>> editGame(@RequestBody final GameDto gameDto) {
        try {
            return ResponseEntity.ok()
                    .body(SimpleWrapper.<GameDto>builder().responseObject(gameService.editGame(gameDto)).build());
        } catch (TerraformingMarsException e) {
            return ResponseEntity.internalServerError()
                    .body(SimpleWrapper.<GameDto>builder().message(e.getMessage()).build());
        }
    }

    @GetMapping("/find-by-location")
    public ResponseEntity<CollectionWrapper<GameDto>> findGameByLocation(
            @RequestParam("location") final String location) {

        try {
            return ResponseEntity.ok()
                    .body(CollectionWrapper.<GameDto>builder().responseObject(gameService.findByLocation(location))
                                  .build());
        } catch (TerraformingMarsException e) {
            return ResponseEntity.internalServerError()
                    .body(CollectionWrapper.<GameDto>builder().message(e.getMessage()).build());
        }
    }
}
