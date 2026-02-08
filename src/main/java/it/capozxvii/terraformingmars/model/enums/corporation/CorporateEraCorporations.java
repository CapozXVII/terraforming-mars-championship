package it.capozxvii.terraformingmars.model.enums.corporation;

import lombok.Getter;
@Getter
public enum CorporateEraCorporations implements Corporation {
    ECOLINE("Ecoline"),
    HELION("Helion"),
    THARSIS_REPUBLIC("Tharsis Republic"),
    PHOBOS_LOGISTICS("Phobos Logistics"),
    SATURN_SYSTEMS("Saturn Systems"),
    CREDICOR("Credicor"),
    INTERPLANETARY_CINEMATICS("Interplanetary Cinematics"),
    INVENTRIX("Inventrix"),
    MINING_GUILD("Mining Guild"),
    THORGATE("Thorgate"),
    UNMI("United Nations Mars Initiative (UNMI)"),
    TERACTOR("Teractor");

    public static final String EXPANSION = "Corporate Era";

    private final String name;

    CorporateEraCorporations(final String name) {
        this.name = name;
    }
    
    @Override
    public String getExpansion() {
        return EXPANSION;
    }
}
