package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti bojových příkazů.
 * Testuje útok na platné i neplatné cíle a mechaniku obrany.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionAttackTest {
    private Game game;

    /**
     * Inicializace hry a přesun hráče do lokace s nepřítelem (Komorník) před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
        game.getGameWorld().setCurrentArea(game.getGameWorld().getCurrentArea().getExit("Zahrada_soch").getExit("Hlavní_brána"));
    }

    /**
     * Ověřuje chování při útoku na neexistujícího nepřítele.
     */
    @Test
    public void testAttackInvalidTarget() {
        String result = game.processUserInput("zautoc nikdo");
        assertTrue(result.contains("Nikdo takový") || result.contains("tu není"));
    }

    /**
     * Ověřuje, že útok na platného nepřítele sníží jeho životy.
     */
    @Test
    public void testAttackValidTarget() {
        int initialHP = game.getGameWorld().getEnemyHealth("komornik");

        game.processUserInput("zautoc komornik");

        int currentHP = game.getGameWorld().getEnemyHealth("komornik");
        assertTrue(currentHP < initialHP, "Po útoku by měl mít nepřítel méně životů.");
    }

    /**
     * Ověřuje funkčnost příkazu obrana.
     * Testuje, zda příkaz proběhne a zda hráč utrpí poškození (i když snížené).
     */
    @Test
    public void testDefendCommand() {
        int initialPlayerHP = game.getGameWorld().getHealth();

        String result = game.processUserInput("bran se");

        assertTrue(result.contains("ubránil"), "Výstup by měl potvrdit obranu.");
        assertTrue(game.getGameWorld().getHealth() < initialPlayerHP);
    }
}