package it.capozxvii.terraformingmars.model.enums.corporation;

import lombok.Getter;

@Getter
public enum ColoniesCorporations implements Corporation {
    ARKLIGHT("Arklight"),
    ARIDOR("Aridor"),
    POLYPHEMOS("Polyphemos"),
    POSEIDON("Poseidon"),
    STORMCRAFT_INCORPORATED("StormCraft Incorporated");

    public static final String EXPANSION = "Colonies";

    private final String name;

    ColoniesCorporations(final String name) {
        this.name = name;
    }

    @Override
    public String getExpansion() {
        return EXPANSION;
    }
}
