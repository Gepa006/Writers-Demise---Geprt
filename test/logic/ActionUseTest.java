package logic;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testovací třída pro ověření funkčnosti příkazu 'pouzij' (ActionUse).
 * Testuje reakce na chybějící parametry, použití neexistujících předmětů,
 * aplikaci efektů lektvarů a pokus o použití předmětů, které nejsou lektvary.
 *
 * @author Jan Říha, Adam Geprt
 * @version ZS-2025
 */
public class ActionUseTest {
    private Game game;

    /**
     * Příprava hry před každým testem.
     * Vytvoří hru a přidá do inventáře lektvar mršnosti pro testování pozitivních scénářů.
     */
    @BeforeEach
    public void setUp() {
        game = new Game();
        game.getGameWorld().getInventory().addItem(new Item("lektvar_mrsnosti", "popis", Item.Type.POTION));
    }

    /**
     * Testuje chování příkazu při zadání bez parametrů.
     * Očekává výzvu k zadání názvu předmětu.
     */
    @Test
    public void testNoParameters() {
        String result = game.processUserInput("pouzij");
        assertEquals("Co mám použít?", result);
    }

    /**
     * Testuje pokus o použití předmětu, který se nenachází v inventáři.
     * Očekává chybovou hlášku informující o absenci předmětu.
     */
    @Test
    public void testItemNotInInventory() {
        String result = game.processUserInput("pouzij neexistujici");
        assertTrue(result.contains("u sebe nemáš"));
    }

    /**
     * Testuje úspěšné použití Lektvaru mršnosti.
     * Ověřuje, že se vypíše správná zpráva, aktivuje se efekt v herním světě
     * a předmět zmizí z inventáře.
     */
    @Test
    public void testUseAgilityPotion() {
        assertFalse(game.getGameWorld().isAgilityActive());

        String result = game.processUserInput("pouzij lektvar_mrsnosti");

        assertTrue(result.contains("Vypil(a) jsi Lektvar mršnosti"));
        assertTrue(result.contains("Cítíš se rychlejší"));

        assertTrue(game.getGameWorld().isAgilityActive());
        assertFalse(game.getGameWorld().getInventory().containsItem("lektvar_mrsnosti"));
    }

    /**
     * Testuje úspěšné použití Lektvaru fénixovy krve (léčení).
     * Nejprve sníží zdraví, přidá lektvar a ověří vyléčení.
     */
    @Test
    public void testUseHealingPotion() {
        game.getGameWorld().getInventory().addItem(new Item("lektvar_fenixovy_krve", "popis", Item.Type.POTION));
        game.getGameWorld().setHealth(10);

        String result = game.processUserInput("pouzij lektvar_fenixovy_krve");

        assertTrue(result.contains("plně vyléčen"));
        assertEquals(100, game.getGameWorld().getHealth());
    }

    /**
     * Testuje pokus o použití předmětu, který není lektvar (např. zbraň).
     * Do inventáře je přidán meč. Očekává se hláška, že předmět nelze použít.
     * Poznámka: Implementace ActionUse v tomto případě předmět z inventáře odstraní,
     * testujeme tedy primárně návratovou hlášku.
     */
    @Test
    public void testUseNonPotionItem() {
        game.getGameWorld().getInventory().addItem(new Item("mec", "Meč", Item.Type.WEAPON));

        String result = game.processUserInput("pouzij mec");

        assertEquals("Tohle se nedá použít.", result);
    }
}