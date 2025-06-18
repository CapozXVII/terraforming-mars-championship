package it.capozxvii.terraformingmars.model.enums.corporation;

import lombok.Getter;

@Getter
public enum PreludeCorporations implements Corporation {
    CHEUNG_SHING_MARS("Cheung Shing Mars"),
    POINT_LUNA("Point Luna"),
    ROBINSON_INDUSTRIES("Robinson Industries"),
    VITOR("Vitor"),
    VALLEY_TRUST("Valley Trust");

    public static final String EXPANSION = "Prelude";

    private final String name;

    PreludeCorporations(final String name) {
        this.name = name;
    }
}
