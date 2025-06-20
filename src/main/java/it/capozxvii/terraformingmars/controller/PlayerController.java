package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import it.capozxvii.terraformingmars.service.IPlayerService;
import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import it.capozxvii.terraformingmars.util.wrapper.CollectionWrapper;
import it.capozxvii.terraformingmars.util.wrapper.SimpleWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController(value = "Player Controller")
@RequestMapping(value = "/player")
public class PlayerController {

    private final IPlayerService playerService;

    public PlayerController(final IPlayerService playerService) {
        this.playerService = playerService;
    }

    @PostMapping("/insert-player")
    public ResponseEntity<String> insertPlayer(@RequestBody final PlayerDto playerDto) {
        try {
            playerService.insertPlayer(playerDto);
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError().body(terraformingMarsException.getMessage());
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(
                Message.formatMessage(Message.SUCCESSFULLY_SAVED, Player.class.getSimpleName(),
                                      playerDto.getNickname()));
    }

    @PutMapping("/update-player")
    public ResponseEntity<String> updatePlayer(@RequestBody final PlayerDto playerDto) {
        try {
            playerService.updatePlayer(playerDto);
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError().body(terraformingMarsException.getMessage());
        }
        return ResponseEntity.ok(
                Message.formatMessage(Message.SUCCESSFULLY_SAVED, Player.class, playerDto.getNickname()));
    }

    @DeleteMapping("/delete-player")
    public ResponseEntity<String> deletePlayer(@RequestBody final PlayerID playerId) {
        try {
            playerService.deletePlayer(playerId);
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError().body(terraformingMarsException.getMessage());
        }
        return ResponseEntity.ok(
                Message.formatMessage(Message.SUCCESSFULLY_DELETED, Player.class, playerId.getNickname()));
    }

    @GetMapping
    public ResponseEntity<SimpleWrapper<PlayerDto>> getPlayer(@RequestBody final PlayerID playerId) {
        try {
            return ResponseEntity.ok(
                    SimpleWrapper.<PlayerDto>builder().responseObject(playerService.getPlayerById(playerId)).build());
        } catch (TerraformingMarsException terraformingMarsException) {
            return ResponseEntity.internalServerError().body(SimpleWrapper.<PlayerDto>builder().message(
                    terraformingMarsException.getMessage()).build());
        }
    }

    @GetMapping("/all-players")
    public ResponseEntity<CollectionWrapper<PlayerDto>> getAllPlayers() {
        return ResponseEntity.ok(
                CollectionWrapper.<PlayerDto>builder().responseObject(playerService.getAllPlayers()).build());
    }
}
