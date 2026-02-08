package it.capozxvii.terraformingmars.model.enums.corporation;

import lombok.Getter;
@Getter
public enum TurmoilCorporations implements Corporation {
    LAKEFRONT_RESORTS("Lakefront Resorts"),
    TERRALABS_RESEARCH("Terralabs Research"),
    UTOPIA_INVEST("Utopia Invest"),
    PRISTAR("Pristar"),
    SEPTEM_TRIBUS("Septem Tribus");

    private static final String EXPANSION = "Turmoil";
    
    private final String name;

    TurmoilCorporations(final String name) {
        this.name = name;
    }
    @Override
    public String getExpansion() {
        return EXPANSION;
    }
}
