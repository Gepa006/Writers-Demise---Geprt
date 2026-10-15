package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'poloz' (ActionDrop).
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionDropTest {
    private Game game;
    private Item lektvar;

    /**
     * Příprava hry před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
        // Přidáme předmět do inventáře, abychom ho mohli položit
        lektvar = new Item("lektvar", "popis", Item.Type.POTION);
        game.getGameWorld().getInventory().addItem(lektvar);
    }

    /**
     * Testuje chování při zadání příkazu bez parametrů.
     */
    @Test
    public void testNoParameters() {
        String result = game.processUserInput("poloz");
        assertEquals("Tomu nerozumím, musíš mi říct, co mám položit.", result);
    }

    /**
     * Testuje chování při zadání více parametrů.
     */
    @Test
    public void testTooManyParameters() {
        String result = game.processUserInput("poloz a b");
        assertEquals("Tomu nerozumím, neumím položit více věcí současně.", result);
    }

    /**
     * Testuje pokus o položení předmětu, který hráč nemá v inventáři.
     */
    @Test
    public void testItemNotInInventory() {
        String result = game.processUserInput("poloz neexistujici");
        assertEquals("Předmět 'neexistujici' u sebe nemáš.", result);
    }

    /**
     * Testuje úspěšné položení předmětu.
     * Ověřuje textovou odpověď, odstranění z inventáře a přidání do lokace.
     */
    @Test
    public void testDropSuccess() {
        String result = game.processUserInput("poloz lektvar");

        assertEquals("Položil(a) jsi předmět 'lektvar' na zem.", result);
        assertFalse(game.getGameWorld().getInventory().containsItem("lektvar"));
        assertTrue(game.getGameWorld().getCurrentArea().containsItem("lektvar"));
    }
}