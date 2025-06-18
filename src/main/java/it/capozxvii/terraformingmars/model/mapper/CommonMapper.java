package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import org.mapstruct.Named;

public interface CommonMapper {
    @Named("toPlayerID")
    static PlayerID toPlayerID(Player player) {
        return PlayerID.builder().id(player.getId()).nickname(player.getNickname()).build();
    }


}
