package it.capozxvii.terraformingmars.model.enums.milestone;

import it.capozxvii.terraformingmars.util.Message;
import it.capozxvii.terraformingmars.util.exception.TerraformingMarsException;
import lombok.Getter;
@Getter
public enum MilestoneEnum {
    GARDENER("Gardener"),
    BUILDER("Builder"),
    PLANNER("Planner"),
    TYCOON("Tycoon"),
    BANKER("Banker"),
    MAYOR("Mayor"),
    LANDLORD("Landlord"),
    SCIENTIST("Scientist"),
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
    TERRAFORM_ENGINEER("Terraform Engineer"),
    INDUSTRIALIST("Industrialist"),
    LOGISTICIAN("Logistician"),
    ECOLOGIST_SUPREME("Ecologist Supreme"),
    TERRAFORM_OVERSEER("Terraform Overseer"),
    RESOURCE_MANAGER("Resource Manager"),
    TRADE_MAGNATE("Trade Magnate"),
    CITY_PLANNER("City Planner"),
    RESEARCH_DIRECTOR("Research Director"),
    TERRAFORM_STRATEGIST("Terraform Strategist");

    private final String name;

    MilestoneEnum(final String name) {
        this.name = name;
    }

    public static MilestoneEnum findMilestoneByName(final String name) {
        for (final MilestoneEnum milestone : MilestoneEnum.values()) {
            if (milestone.getName().equals(name)) {
                return milestone;
            }
        }
        throw new TerraformingMarsException(Message.NO_MILESTONE_WAS_FOUND, name);
    }
}
