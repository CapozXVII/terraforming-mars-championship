package it.capozxvii.terraformingmars.model.enums.corporation;

import lombok.Getter;

@Getter
public enum VenusNextCorporations implements Corporation {
    APHRODITE("Aphrodite"),
    CELESTIC("Celestic"),
    MANUTECH("Manutech"),
    MORNING_STAR_INC("Morning Star Inc."),
    VENERA_INSTITUTE("Venera Institute");

    public static final String EXPANSION = "VenusNext";
    
    private final String name;


    VenusNextCorporations(final String name) {
        this.name = name;
    }
}
