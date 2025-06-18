package it.capozxvii.terraformingmars.model.enums.award;

import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import lombok.Getter;

@Getter
public enum AwardEnum {
    LANDLORD("Landlord"),
    BANKER("Banker"),
    SCIENTIST("Scientist"),
    BUILDER("Builder"),
    THERMALIST("Thermalist"),
    MINER("Miner"),
    SPACE_BARON("Space Baron"),
    ECOLOGIST("Ecologist"),
    ARTIST("Artist"),
    EXPLORER("Explorer"),
    WARRIOR("Warrior"),
    INVESTOR("Investor"),
    ENGINEER("Engineer"),
    FARMER("Farmer"),
    INVENTOR("Inventor"),
    TERRAFORMER("Terraformer"),
    MERCHANT("Merchant"),
    COLONIST("Colonist"),
    GEOLOGIST("Geologist"),
    METALLURGIST("Metallurgist"),
    NAVIGATOR("Navigator"),
    ARCHITECT("Architect"),
    TYCOON("Tycoon"),
    PLANNER("Planner"),
    MAYOR("Mayor"),
    INDUSTRIALIST("Industrialist"),
    TERRAFORM_OVERSEER("Terraform Overseer"),
    RESOURCE_MANAGER("Resource Manager"),
    TRADE_MAGNATE("Trade Magnate"),
    CITY_PLANNER("City Planner"),
    RESEARCH_DIRECTOR("Research Director"),
    TERRAFORM_STRATEGIST("Terraform Strategist"),
    TERRAFORM_CHAMPION("Terraform Champion"),
    ECO_GUARDIAN("Eco Guardian"),
    PRODUCTION_MASTER("Production Master"),
    COLONY_FOUNDER("Colony Founder");

    private final String name;

    AwardEnum(final String name) {
        this.name = name;
    }

    public static AwardEnum findAwardByName(final String name) {
        for (final AwardEnum award : AwardEnum.values()) {
            if (award.getName().equals(name)) {
                return award;
            }
        }
        throw new TerraformingMarsException(Message.NO_AWARD_WAS_FOUND, name);
    }
}
