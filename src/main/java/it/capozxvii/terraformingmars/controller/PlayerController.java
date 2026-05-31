package it.capozxvii.terraformingmars.controller;

import it.capozxvii.terraformingmars.model.dto.PlayerDto;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.service.IPlayerService;
import it.capozxvii.terraformingmars.util.Message;
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
    public ResponseEntity<SimpleWrapper<PlayerDto>> insertPlayer(@RequestBody final PlayerDto playerDto) {
        playerService.insertPlayer(playerDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(SimpleWrapper.<PlayerDto>builder().message(
                Message.formatMessage(Message.SUCCESSFULLY_SAVED, Player.class.getSimpleName(),
                                      playerDto.getNickname())).build());
    }

    @PutMapping("/update-player")
    public ResponseEntity<SimpleWrapper<PlayerDto>> updatePlayer(@RequestBody final PlayerDto playerDto) {
        playerService.updatePlayer(playerDto);
        return ResponseEntity.ok(SimpleWrapper.<PlayerDto>builder().message(
                Message.formatMessage(Message.SUCCESSFULLY_UPDATED, Player.class.getSimpleName(),
                                      playerDto.getNickname())).build());
    }

    @DeleteMapping("/delete-player")
    public ResponseEntity<SimpleWrapper<PlayerDto>> deletePlayer(@RequestBody final Long playerId) {
        playerService.deletePlayer(playerId);
        return ResponseEntity.ok(SimpleWrapper.<PlayerDto>builder().message(
                Message.formatMessage(Message.SUCCESSFULLY_DELETED, Player.class.getSimpleName(), playerId)).build());
    }

    @GetMapping
    public ResponseEntity<SimpleWrapper<PlayerDto>> getPlayer(@RequestBody final Long playerId) {
        return ResponseEntity.ok(
                SimpleWrapper.<PlayerDto>builder().responseObject(playerService.getPlayerById(playerId)).build());
    }

    @GetMapping("/all-players")
    public ResponseEntity<CollectionWrapper<PlayerDto>> getAllPlayers() {
        return ResponseEntity.ok(
                CollectionWrapper.<PlayerDto>builder().responseObject(playerService.getAllPlayers()).build());
    }
}
