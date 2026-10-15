package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'jdi' (ActionMove).
 * Testuje pohyb mezi lokacemi, zpracování předložek, blokaci pohybu nepřáteli (Combat Lock)
 * a speciální interakce (Vrátný).
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionMoveTest {
    private Game game;

    /**
     * Příprava herního světa před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    /**
     * Testuje chování při zadání příkazu bez parametrů.
     */
    @Test
    public void testNoParameters() {
        String result = game.processUserInput("jdi");
        assertEquals("Tomu nerozumím, musíš mi říct, kam mám jít.", result);
    }

    /**
     * Testuje pokus o pohyb do neexistující lokace.
     */
    @Test
    public void testInvalidExit() {
        String result = game.processUserInput("jdi Tramtárie");
        assertTrue(result.contains("se odsud jít nedá (nebo neexistuje)"));
    }

    /**
     * Testuje standardní úspěšný pohyb mezi propojenými lokacemi.
     */
    @Test
    public void testStandardMove() {
        assertEquals("Mlhové_předměstí", game.getGameWorld().getCurrentArea().getName());

        String result = game.processUserInput("jdi zahrada");

        assertTrue(result.contains("Zahrada plná kamenných soch"));
        assertEquals("Zahrada_soch", game.getGameWorld().getCurrentArea().getName());
    }

    /**
     * Testuje zpracování předložek (do, na, k).
     * Podle implementace by měl příkaz 'jdi do zahrada' fungovat stejně jako 'jdi zahrada'.
     */
    @Test
    public void testMoveWithPreposition() {
        String result = game.processUserInput("jdi do zahrada");

        assertTrue(result.contains("Zahrada plná kamenných soch"));
        assertEquals("Zahrada_soch", game.getGameWorld().getCurrentArea().getName());
    }

    /**
     * Testuje Combat Lock (blokování pohybu nepřítelem).
     * Hráč se pokusí odejít z lokace, kde je živý nepřítel (Komorník).
     */
    @Test
    public void testCombatLock() {
        game.processUserInput("jdi zahrada");
        game.processUserInput("jdi brana");

        assertTrue(game.getGameWorld().getCurrentArea().containsCharacter("komornik"));
        assertTrue(game.getGameWorld().getEnemyHealth("komornik") > 0);

        String result = game.processUserInput("jdi hala");

        assertTrue(result.contains("Nemůžeš odejít!"));
        assertTrue(result.contains("V cestě ti stojí komornik"));
        assertEquals("Hlavní_brána", game.getGameWorld().getCurrentArea().getName());
    }

    /**
     * Testuje speciální interakci s Vrátným ve Vstupní hale.
     * Ověřuje, zda se při vstupu do haly vypíše hádanka.
     */
    @Test
    public void testVratnyEvent() {
        game.processUserInput("jdi zahrada");
        game.processUserInput("jdi brana");

        // Odstraníme Komorníka (cheat), abychom mohli projít
        game.getGameWorld().damageEnemy("komornik", 999);

        String result = game.processUserInput("jdi hala");

        assertTrue(result.contains("Vstupní_hala"));
        assertTrue(result.contains("Vrátný ti zastoupil cestu!"));
        assertTrue(result.contains("Kolikátá expedice"));
        assertEquals("Vstupní_hala", game.getGameWorld().getCurrentArea().getName());
    }
}