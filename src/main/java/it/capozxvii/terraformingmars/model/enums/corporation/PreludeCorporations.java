package it.capozxvii.terraformingmars.model.enums.corporation;

import lombok.Getter;

@Getter
public enum PreludeCorporations implements Corporation {
    CHEUNG_SHING_MARS("Cheung Shing Mars"),
    ECOTEC("Ecotec"),
    NIRGAL_ENTERPRISES("Nirgal Enterprises"),
    PALLADIN_SHIPPING("Palladin Shipping"),
    POINT_LUNA("Point Luna"),
    ROBINSON_INDUSTRIES("Robinson Industries"),
    SAGITTA("Sagitta"),
    SPIRE("Spire"),
    VALLEY_TRUST("Valley Trust"),
    VITOR("Vitor");

    public static final String EXPANSION = "Prelude";

    private final String name;

    PreludeCorporations(final String name) {
        this.name = name;
    }
    @Override
    public String getExpansion() {
        return EXPANSION;
    }
}
