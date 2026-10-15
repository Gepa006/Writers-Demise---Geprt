package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída ověřující kompletní průchod hrou od začátku do konce (Happy Path).
 * Simuluje optimální průchod hráče včetně řešení úkolů a soubojů.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class GameScenarioTest {
    private Game game;

    /**
     * Inicializace hry před každým testem.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
    }

    /**
     * Ověřuje vítězný scénář hry.
     * Testuje posloupnost kroků: start -> zisk zbraně -> souboj s komorníkem ->
     * řešení hádanky v hale -> souboj se strážným -> finální souboj s bossem.
     */
    @Test
    public void testWinningScenario() {
        assertEquals("Mlhové_předměstí", game.getGameWorld().getCurrentArea().getName());

        game.processUserInput("seber lektvar_mrsnosti");
        assertTrue(game.getGameWorld().getInventory().containsItem("lektvar_mrsnosti"));

        game.processUserInput("jdi zahrada");
        game.processUserInput("jdi kulna");

        assertEquals("Stará_kůlna", game.getGameWorld().getCurrentArea().getName(), "Hráč nedošel do kůlny!");

        game.processUserInput("seber mec");

        assertNotNull(game.getGameWorld().getInventory().getWeapon(), "Hráč nemá zbraň! Sebrání meče selhalo.");
        assertEquals("mec", game.getGameWorld().getInventory().getWeapon().getName());

        game.processUserInput("jdi zahrada");
        game.processUserInput("jdi brana");

        game.getGameWorld().damageEnemy("komornik", 89);
        game.processUserInput("zautoc komornik");

        assertTrue(game.getGameWorld().getEnemyHealth("komornik") <= 0, "Komorník by měl být mrtvý (HP <= 0).");

        game.processUserInput("jdi hala");
        assertTrue(game.getGameWorld().getCurrentArea().containsCharacter("vratny"));


        game.processUserInput("odpovez 33");
        assertFalse(game.getGameWorld().getCurrentArea().containsCharacter("vratny"));

        game.processUserInput("jdi schodiste");

        game.getGameWorld().damageEnemy("strazny", 99);
        game.processUserInput("zautoc strazny");

        game.processUserInput("jdi studovna");

        game.getGameWorld().damageEnemy("spisovatel", 199);
        String finalResult = game.processUserInput("zautoc spisovatel");

        assertTrue(finalResult.contains("DOKÁZAL(A) JSI TO"));
        assertTrue(game.getGameWorld().getGameState() == GameState.WON);
    }
}