package it.capozxvii.terraformingmars.model.enums.prelude;

import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import lombok.Getter;
@Getter
public enum PreludeEnum {
    ALLIED_BANKS("Allied Banks"),
    BIOLAB("Biolab"),
    BIOSPHERE_SUPPORT("Biosphere Support"),
    BUSINESS_EMPIRE("Business Empire"),
    BY_PRODUCT_POWER("By-product Power"),
    DONATION("Donation"),
    EARLY_SETTLEMENT("Early Settlement"),
    ECOLOGICAL_ZONE("Ecological Zone"),
    ENERGY_TAPPING("Energy Tapping"),
    GALILEAN_MINING("Galilean Mining"),
    HOUSE_PRINTING("House Printing"),
    INDUSTRIAL_ZONE("Industrial Zone"),
    LOAN("Loan"),
    MARTIAN_INDUSTRIES("Martian Industries"),
    MARTIAN_MEDIA_CENTER("Martian Media Center"),
    MARTIAN_SETTLERS("Martian Settlers"),
    METAL_RICH_ASTEROID("Metal-Rich Asteroid"),
    METALLIC_COMPOUNDS("Metallic Compounds"),
    ORBITAL_CONSTRUCTION_YARD("Orbital Construction Yard"),
    POLAR_INDUSTRIES("Polar Industries"),
    POWER_GENERATION("Power Generation"),
    RESEARCH_NETWORK("Research Network"),
    ROBOTIC_WORKFORCE("Robotic Workforce"),
    SELF_SUFFICIENT_SETTLEMENT("Self-Sufficient Settlement"),
    SOCIETY_SUPPORT("Society Support"),
    SPACE_HOTELS("Space Hotels"),
    STANDARD_TECHNOLOGY("Standard Technology"),
    SUPPLY_DROP("Supply Drop"),
    TERRAFORMING_CONTRACT("Terraforming Contract"),
    UNMI_CONTRACTOR("UNMI Contractor"),
    VIRTUAL_REALITY("Virtual Reality"),
    WARP_DRIVE("Warp Drive");

    private final String name;

    PreludeEnum(final String name) {
        this.name = name;
    }

    public static PreludeEnum findPreludeByName(final String name) {
        for (final PreludeEnum preludeEnum : PreludeEnum.values()) {
            if (preludeEnum.getName().equals(name)) {
                return preludeEnum;
            }
        }
        throw new TerraformingMarsException(Message.NO_PRELUDE_CARD_FOUND, name);
    }
}
