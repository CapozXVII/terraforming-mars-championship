package it.capozxvii.terraformingmars.model.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
class ChampionshipTest {
    @Test
    void constructorTest() {
        Championship championship = new Championship(1L, "aName");
        assertEquals(1L, championship.getChampionshipId());
        assertEquals("aName", championship.getName());
    }
}
