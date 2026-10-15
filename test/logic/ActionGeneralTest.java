package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření obecných příkazů (napoveda, konec).
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionGeneralTest {
    private Game game;

    /**
     * Inicializace hry.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    /**
     * Ověřuje, že příkaz nápověda vrací text obsahující seznam příkazů.
     */
    @Test
    public void testHelp() {
        String result = game.processUserInput("napoveda");
        assertNotNull(result);
        assertTrue(result.contains("Tvým úkolem je"));
        assertTrue(result.contains("Dostupné příkazy"));
    }

    /**
     * Ověřuje, že příkaz konec ukončí hru.
     */
    @Test
    public void testTerminate() {
        assertFalse(game.isGameOver());

        String result = game.processUserInput("konec");

        assertTrue(game.isGameOver());
        assertTrue(result.contains("Hra byla ukončena"));
    }
}