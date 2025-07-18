package it.capozxvii.terraformingmars.model.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;
class GameTest {
    @Test
    void equalsTest() {
        Game game = new Game();
        game.setGameId(1L);
        assertEquals(game, game);

        Game game2 = new Game();
        game2.setGameId(1L);
        assertEquals(game, game2);
    }

    @Test
    void notEqualsTest() {
        Game game = new Game();
        game.setGameId(1L);
        Game game2 = new Game();
        game2.setGameId(2L);
        assertNotEquals(game, game2);
    }

    @Test
    void hashCodeTest() {
        Game game = new Game();
        game.setGameId(1L);
        Game game2 = new Game();
        game2.setGameId(1L);
        assertEquals(game.hashCode(), game2.hashCode());
    }
}
