package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'bran' (ActionDefend).
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionDefendTest {
    private Game game;

    /**
     * Příprava hry před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    /**
     * Testuje obranu v prázdné místnosti (bez nepřátel).
     * Příkaz by měl informovat hráče, že se není před kým bránit.
     */
    @Test
    public void testDefendWithoutEnemy() {
        String result = game.processUserInput("bran se");
        assertTrue(result.contains("Nemáš se před kým bránit"));
        assertEquals(100, game.getGameWorld().getHealth());
    }

    /**
     * Testuje úspěšnou obranu proti nepříteli.
     * Ověřuje, že příkaz proběhne a hráč obdrží redukované poškození.
     */
    @Test
    public void testDefendWithEnemy() {
        game.processUserInput("jdi zahrada");
        game.processUserInput("jdi brana");

        int hpBefore = game.getGameWorld().getHealth();
        String result = game.processUserInput("bran se");

        assertTrue(result.toLowerCase().contains("ubránil") || result.toLowerCase().contains("obranu"));
        assertTrue(result.contains("Utrpěl(a) jsi jen") || result.contains("sníženo"));


        int hpAfter = game.getGameWorld().getHealth();
        assertTrue(hpAfter < hpBefore);
        assertTrue(hpAfter > 0);
    }

    /**
     * Testuje zpracování parametru příkazu.
     * Ověřuje chování při zadání samotného "bran" nebo chybného parametru.
     */
    @Test
    public void testParameterHandling() {
        String result = game.processUserInput("bran");
        assertTrue(result.contains("Nemáš se před kým bránit") || result.contains("ubránil"));

        String resultError = game.processUserInput("bran neco");
        assertTrue(resultError.contains("Myslel(a) jsi 'bran se'?"));
    }
}