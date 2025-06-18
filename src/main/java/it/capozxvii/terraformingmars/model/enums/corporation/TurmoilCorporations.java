package it.capozxvii.terraformingmars.model.enums.corporation;

import lombok.Getter;
@Getter
public enum TurmoilCorporations implements Corporation {
    ARCADIAN_COMMUNITIES("Arcadian Communities"),
    MONS_INSURANCE("Mons Insurance"),
    PRISTAR("Pristar"),
    RECYCLON("Recyclon"),
    SEPTEM_TRIBUS("Septem Tribus");

    private final String name;

    TurmoilCorporations(final String name) {
        this.name = name;
    }
}
