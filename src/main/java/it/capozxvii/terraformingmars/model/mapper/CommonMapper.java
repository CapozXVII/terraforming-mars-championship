package it.capozxvii.terraformingmars.model.mapper;

import it.capozxvii.terraformingmars.model.enums.corporation.Corporation;
import it.capozxvii.terraformingmars.model.enums.prelude.PreludeEnum;
import it.capozxvii.terraformingmars.model.jpa.Player;
import it.capozxvii.terraformingmars.model.jpa.compositekeys.PlayerID;
import it.capozxvii.terraformingmars.util.CorporationFactory;
import org.mapstruct.Named;

public interface CommonMapper {
    @Named("toPlayerID")
    static PlayerID toPlayerID(Player player) {
        return PlayerID.builder().id(player.getId()).nickname(player.getNickname()).build();
    }

    @Named("getCorporation")
    static Corporation getCorporation(final String corporationName) {
        return CorporationFactory.fromName(corporationName);
    }

    @Named("getPrelude")
    static PreludeEnum getPrelude(final String prelude) {
        return PreludeEnum.findPreludeByName(prelude);
    }

    @Named("getCorporationName")
    static String getCorporationName(final Corporation corporation) {
        return corporation.getName();
    }

    @Named("getPreludeName")
    static String getPreludeName(final PreludeEnum prelude) {
        return prelude.getName();
    }
}
